# $Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2000 by Howard Ship and Primix Solutions
#
# Primix Solutions
# One Arsenal Marketplace
# Watertown, MA 02472
# http://www.primix.com
# mailto:hship@primix.com
# 
# This library is free software.
# 
# You may redistribute it and/or modify it under the terms of the GNU
# Lesser General Public License as published by the Free Software Foundation.
#
# Version 2.1 of the license should be included with this distribution in
# the file LICENSE, as well as License.html. If the license is not
# included with this distribution, you may find a copy at the FSF web
# site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
# Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.

clean: clean-root local-clean clean-packages

clean-root:
	@$(ECHO) Cleaning ...
	@$(RMDIRS) $(SYS_BUILD_DIR_NAME) $(JAR_FILE)

clean-packages: clean-root
ifeq "$(PACKAGES)" ""
	@$(ECHO) JBE Error: Must define PACKAGES in Makefile
else
	@for package in $(PACKAGES) ; do \
	  $(RECURSE) PACKAGE="$$package" clean-package ; \
	done
endif

clean-package:
	@$(ECHO) "\n*** Cleaning package $(PACKAGE) ... ***\n"
	@$(MAKE_IN_PACKAGE) clean

setup-catalogs: initialize $(MOD_JAVA_CATALOG) $(MOD_RMI_CLASS_CATALOG) $(MOD_RESOURCE_CATALOG)

$(MOD_JAVA_CATALOG) $(MOD_RMI_CLASS_CATALOG) $(MOD_RESOURCE_CATALOG):
ifeq "$(PACKAGES)" ""
	@$(ECHO) JBE Error: Must define PACKAGES in Makefile
else
	@$(ECHO) -n > $(MOD_JAVA_CATALOG)
	@$(ECHO) -n > $(MOD_RMI_CLASS_CATALOG)
	@$(ECHO) -n > $(MOD_RESOURCE_CATALOG)
	@for package in $(PACKAGES) ; do \
	  $(RECURSE) PACKAGE="$$package" catalog-package ; \
	done
endif
	
catalog-package:
	@$(ECHO) "\n*** Cataloging package $(PACKAGE) ... ***\n"
	@$(MAKE_IN_PACKAGE) catalog
	
compile: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-compile
	
copy-resources: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-copy-resources
	
# Rule to force a rebuild of just the catalogs

catalog:
	@$(RM) --force $(MOD_JAVA_CATALOG) $(MOD_RMI_CLASS_CATALOG) $(MOD_RESOURCE_CATALOG)
	@$(RECURSE) setup-catalogs
	
# The force rule forces a recompile of all Java classes

force: setup-catalogs
	@$(RM) --force $(MOD_JAVA_STAMP_FILE)
	@$(RECURSE) POST_SETUP=t inner-compile
	
ifdef POST_SETUP

# Used to allow the inner targets to do something, even if fully up-to date.

DUMMY_FILE := $(SYS_BUILD_DIR_NAME)/dummy

inner-compile: $(MOD_JAVA_STAMP_FILE) $(RMI_STAMP_FILE)
	@$(TOUCH) $(DUMMY_FILE)
	
inner-copy-resources: $(MOD_META_STAMP_FILE) $(RESOURCE_STAMP_FILE)
	@$(TOUCH) $(DUMMY_FILE)

_JAVA_FILES := $(shell $(CAT) $(MOD_JAVA_CATALOG))

$(MOD_JAVA_STAMP_FILE): $(_JAVA_FILES)
	@$(ECHO) "\n*** Compiling ... ***\n"
	$(JAVAC) $(FINAL_JAVAC_OPT) $?
	@$(TOUCH) $@ $(MOD_DIRTY_JAR_STAMP_FILE)

# Read the catalog file

_RMI_CLASS_NAMES := $(shell $(CAT) $(MOD_RMI_CLASS_CATALOG))

# Find the name of each RMI implementation class.  This is the
# name of the corresponding .class file, inside the $(MOD_CLASS_DIR).

_RMI_CLASS_FILES := \
	$(addprefix $(MOD_CLASS_DIR)$(SLASH), \
		$(addsuffix .class, \
			$(subst $(DOT),$(SLASH),$(_RMI_CLASS_NAMES))))

# Here's where it gets real tricky; we need to reverse the prior
# process and get BACK to the class name.

$(RMI_STAMP_FILE): $(_RMI_CLASS_FILES)
ifneq "$(_RMI_CLASS_NAMES)" ""
	@$(ECHO) "\n*** Compiling RMI stubs and skeletons ... ***\n"
	$(RMIC) $(FINAL_RMIC_OPT) \
		$(subst $(SLASH),$(DOT), \
			$(subst .class,$(EMPTY), \
				$(subst $(MOD_CLASS_DIR)$(SLASH),$(EMPTY), $?)))
	@$(TOUCH) $(MOD_DIRTY_JAR_STAMP_FILE)
endif
	@$(TOUCH) $@

_RESOURCE_FILES := $(shell $(CAT) $(MOD_RESOURCE_CATALOG))

$(RESOURCE_STAMP_FILE): $(_RESOURCE_FILES)
ifneq "$(_RESOURCE_FILES)" ""
	@$(ECHO) "\n*** Copying package resources ...***\n"
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) --force --parents $? $(MOD_CLASS_DIR)
	@$(TOUCH) $(MOD_DIRTY_JAR_STAMP_FILE)
endif
	@$(TOUCH) $@
	
endif
	
FINAL_META_RESOURCES := $(strip $(MOD_META_RESOURCES) $(META_RESOURCES))

$(MOD_META_STAMP_FILE): $(FINAL_META_RESOURCES)
ifneq "$(FINAL_META_RESOURCES)" ""
	@$(ECHO) "\n*** Copying META-INF resources ... ***\n"
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) --force $? $(MOD_META_INF_DIR)
	@$(TOUCH) $(MOD_DIRTY_JAR_STAMP_FILE)
endif
	@$(TOUCH) $@ 

JAVADOC_CLASSPATH = \
	$(shell $(JBE_CANONICALIZE) -classpath $(LOCAL_CLASSPATH) $(MOD_CLASS_DIR))

javadoc:
ifeq "$(JAVADOC_DIR)" ""
	@$(ECHO) JBE Error:  must set JAVADOC_DIR in Makefile
else
ifeq "$(PACKAGES)" ""
	@$(ECHO) JBE Error: Must define PACKAGES in Makefile
else
	@$(ECHO) "\n*** Generating Javadoc ... ***\n"
	@$(MKDIRS) $(FINAL_JAVADOC_DIR)
	$(JAVADOC) -d $(FINAL_JAVADOC_DIR) \
	-sourcepath "."	-classpath "$(JAVADOC_CLASSPATH)" $(JAVADOC_OPT) \
	$(PACKAGES)
endif
endif

# Rule to make sure the JBE Util class is around.  Usually executed just once, the first
# time the JBE is used.

setup-jbe-util: $(SYS_MAKEFILE_DIR)/com/primix/jbe/Util.class

$(SYS_MAKEFILE_DIR)/com/primix/jbe/Util.class: $(SYS_MAKEFILE_DIR)/com/primix/jbe/Util.java
	@$(ECHO) "\n*** Compiling JBE Utility ... ***\n";
	$(CD) $(SYS_MAKEFILE_DIR) ; \
	$(JAVAC) com/primix/jbe/Util.java

# May be implemented

.PHONY: local-clean

.PHONY: inner-compile inner-copy-resources
.PHONY: clean clean-root clean-packages
.PHONY: setup-catalogs catalog-package
.PHONY: compile copy-resources javadoc
.PHONY: setup-jbe-util
