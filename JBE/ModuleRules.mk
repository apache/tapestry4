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
	@$(ECHO) "\n*** Cleaning ... ***\n"
	@$(RMDIRS) $(SYS_BUILD_DIR_NAME) $(JAR_FILE)

compile: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-compile
	
copy-resources: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-copy-resources
	
# Rule to force a rebuild of just the catalogs

catalog: initialize
	@$(RM) --force $(MOD_JAVA_CATALOG) $(MOD_RMI_CLASS_CATALOG) $(MOD_RESOURCE_CATALOG)
	$(RECURSE)  SETUP_CATALOGS=t inner-setup-catalogs
	
setup-catalogs: initialize
	@$(RECURSE) SETUP_CATALOGS=t inner-setup-catalogs
	
# The force rule forces a recompile of all Java classes

force: setup-catalogs
	@$(RM) --force $(MOD_JAVA_STAMP_FILE)
	@$(RECURSE) POST_SETUP=t inner-compile

# Used to allow the inner targets to do something, even if fully up-to date.

DUMMY_FILE := $(SYS_BUILD_DIR_NAME)/dummy
	
ifdef POST_SETUP

# Build the compile-time classpath

_ABSOLUTE_DIRS := $(shell $(JBE_CANONICALIZE) $(MOD_BUILD_DIR) $(MOD_CLASS_DIR))

ABSOLUTE_MOD_BUILD_DIR := $(word 1,$(_ABSOLUTE_DIRS))
ABSOLUTE_CLASS_DIR := $(word 2,$(_ABSOLUTE_DIRS))

FINAL_CLASSPATH = $(shell $(JBE_CANONICALIZE) -classpath \
	$(FINAL_SOURCE_DIR) $(MOD_CLASS_DIR) $(MOD_CLASSPATH) $(SITE_CLASSPATH) $(LOCAL_CLASSPATH))
	
FINAL_CLASSPATH_OPTION = -classpath "$(FINAL_CLASSPATH)"
	
FINAL_JAVAC_OPT = $(strip \
	-d $(ABSOLUTE_CLASS_DIR) \
	$(FINAL_CLASSPATH_OPTION) \
	$(MOD_JAVAC_OPT) \
	$(SITE_JAVAC_OPT) \
	$(LOCAL_JAVAC_OPT) \
	$(JAVAC_OPT))
	
FINAL_RMIC_OPT = $(strip \
	$(FINAL_JAVAC_OPT) \
	$(MOD_RMIC_OPT) \
	$(SITE_RMIC_OPT) \
	$(LOCAL_RMIC_OPT) \
	$(RMIC_OPT))
	
inner-compile: $(MOD_JAVA_STAMP_FILE) $(RMI_STAMP_FILE)
	@$(TOUCH) $(DUMMY_FILE)
	
inner-copy-resources: $(MOD_META_STAMP_FILE) $(RESOURCE_STAMP_FILE)
	@$(TOUCH) $(DUMMY_FILE)

# The Java Catalog stores the names of the Java files, relative to
# the SOURCE_DIR.  We need to get the pathname back by prefixing with
# the SOURCE_DIR.

_JAVA_FILES := $(addprefix $(FINAL_SOURCE_DIR)$(SLASH), \
	$(shell $(CAT) $(MOD_JAVA_CATALOG)))

$(MOD_JAVA_STAMP_FILE): $(_JAVA_FILES)
ifneq "$(_JAVA_FILES)" ""
	@$(ECHO) "\n*** Compiling ... ***\n"
	$(CD) $(FINAL_SOURCE_DIR) ; \
	$(JAVAC) $(FINAL_JAVAC_OPT) $(patsubst $(FINAL_SOURCE_DIR)$(SLASH)%, \
	  	%, $?)
	@$(TOUCH) $@ $(MOD_DIRTY_JAR_STAMP_FILE)
else
	@$(ECHO) "\n*** Nothing to compile ***\n"
endif

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

# Like the Java files above, the resource files also are relative
# to the SOURCE_DIR.  We have to be very careful when copying
# because we need the relative pathnames if the files are
# to end up in the right place.

_RESOURCE_FILES := $(addprefix $(FINAL_SOURCE_DIR)$(SLASH), \
	$(shell $(CAT) $(MOD_RESOURCE_CATALOG)))

$(RESOURCE_STAMP_FILE): $(_RESOURCE_FILES)
ifneq "$(_RESOURCE_FILES)" ""
	@$(ECHO) "\n*** Copying package resources ...***\n"
	@$(ECHO) Copying: $(notdir $?)
	@$(CD) $(FINAL_SOURCE_DIR) ; \
	$(CP) --force --parents $? $(ABSOLUTE_CLASS_DIR)
	@$(TOUCH) $(MOD_DIRTY_JAR_STAMP_FILE)
endif
	@$(TOUCH) $@

# End of the POST_SETUP block
	
endif

ifdef SETUP_CATALOGS

inner-setup-catalogs: $(MOD_JAVA_CATALOG) $(MOD_RMI_CLASS_CATALOG) $(MOD_RESOURCE_CATALOG)
	@$(TOUCH) $(DUMMY_FILE)
	
ABSOLUTE_MOD_BUILD_DIR := $(shell $(JBE_CANONICALIZE) $(MOD_BUILD_DIR))

# Rules for rebuilding the catalog by visiting each
# Package.  Certain types of modules have no Java source (no PACKAGES are defined)
# but that's OK.

$(MOD_JAVA_CATALOG) $(MOD_RMI_CLASS_CATALOG) $(MOD_RESOURCE_CATALOG):
	@$(ECHO) -n > $(MOD_JAVA_CATALOG)
	@$(ECHO) -n > $(MOD_RMI_CLASS_CATALOG)
	@$(ECHO) -n > $(MOD_RESOURCE_CATALOG)
ifneq "$(PACKAGES)" ""
	@for package in $(PACKAGES) ; do \
	  $(RECURSE) PACKAGE_RECURSE=t PACKAGE="$$package" \
	  ABSOLUTE_MOD_BUILD_DIR="$(ABSOLUTE_MOD_BUILD_DIR)" catalog-package ; \
	done
endif

# End of SETUP_CATALOGS block

endif


ifdef PACKAGE_RECURSE

# A few rules used with recursion.  Recursion works by re-invoking
# make in the Module directory, by specifying a value for PACKAGE on
# the command line (in addition to a target).

# Convert each '.' to a path seperator

PACKAGE_DIR := $(subst $(PERIOD),$(SLASH),$(PACKAGE))

# Create a relative project directory by counting the number of
# periods. This is tricky, because $(foreach) like to add spaces, which we have
# to convert to slashes.

_PACKAGE_TERMS := $(subst $(PERIOD),$(SPACE),$(PACKAGE))
_RELATIVE_MOD_DIR := $(strip $(foreach foo,$(_PACKAGE_TERMS),$(DOTDOT)))

catalog-package:
	@$(ECHO) "\n*** Cataloging package $(PACKAGE) ... ***\n"
	@$(MAKE) -C $(FINAL_SOURCE_DIR)$(SLASH)$(PACKAGE_DIR) \
		MOD_BUILD_DIR="$(ABSOLUTE_MOD_BUILD_DIR)" \
		MOD_PACKAGE_DIR="$(PACKAGE_DIR)"
	
# End of PACKAGE_RECURSE block

endif


JAVADOC_CLASSPATH = \
	$(shell $(JBE_CANONICALIZE) -classpath \
		$(MOD_CLASSPATH) $(LOCAL_CLASSPATH) $(MOD_CLASS_DIR))

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
	-sourcepath $(FINAL_SOURCE_DIR)	-classpath "$(JAVADOC_CLASSPATH)" $(JAVADOC_OPT) \
	$(PACKAGES)
endif
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

initialize: setup-jbe-util
	@$(MKDIRS) $(MOD_BUILD_DIR)

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
.PHONY: inner-setup-catalogs