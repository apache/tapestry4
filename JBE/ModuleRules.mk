# $Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2000, 2001 by Howard Ship and Primix
#
# Primix
# 311 Arsenal Street
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

include $(SYS_MAKEFILE_DIR)/CommonRules.mk

clean: clean-root module-clean

clean-root:
	$(call NOTE, Cleaning ...)
	@$(RM) $(SYS_BUILD_DIR_NAME) $(JAR_FILE)

compile: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-compile
	
copy-resources: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-copy-resources

install: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-install

# Rule to force a rebuild of just the catalogs

catalog: initialize
	@$(RM) $(MOD_JAVA_CATALOG) $(MOD_RESOURCE_CATALOG)
	@$(RECURSE)  SETUP_CATALOGS=t inner-setup-catalogs
	
setup-catalogs: initialize
	@$(RECURSE) SETUP_CATALOGS=t inner-setup-catalogs
	
initialize: module-initialize

# The force rule forces a recompile of all Java classes

force: setup-catalogs
	@$(RM) $(MOD_JAVA_STAMP_FILE)
	@$(RECURSE) POST_SETUP=t inner-compile

# Used to allow the inner targets to do something, even if fully up-to date.

DUMMY_FILE := $(SYS_BUILD_DIR_NAME)/dummy
	
ifdef POST_SETUP

# Build the compile-time classpath.  We need the canonical versions of two
# relative pathnames, but we only want to execute  JBE_CANONICALIZE once.

_ABSOLUTE_DIRS := $(call JBE_CANONICALIZE,$(MOD_BUILD_DIR) $(MOD_CLASS_DIR))

ABSOLUTE_MOD_BUILD_DIR := $(word 1,$(_ABSOLUTE_DIRS))
ABSOLUTE_CLASS_DIR := $(word 2,$(_ABSOLUTE_DIRS))

FINAL_CLASSPATH = $(call JBE_CANONICALIZE, -classpath \
	$(FINAL_SOURCE_DIR) $(MOD_CLASS_DIR) $(MOD_CLASSPATH) $(PROJ_CLASSPATH))
	
FINAL_CLASSPATH_OPTION = -classpath "$(FINAL_CLASSPATH)"
	
FINAL_JAVAC_OPT = $(strip \
	-d $(ABSOLUTE_CLASS_DIR) \
	$(FINAL_CLASSPATH_OPTION) \
	$(MOD_JAVAC_OPT) \
	$(LOCAL_JAVAC_OPT) \
	$(JAVAC_OPT))
	
FINAL_RMIC_OPT = $(strip \
	$(FINAL_JAVAC_OPT) \
	$(MOD_RMIC_OPT) \
	$(LOCAL_RMIC_OPT) \
	$(RMIC_OPT))
	
inner-compile: $(MOD_JAVA_STAMP_FILE) $(RMI_STAMP_FILE)
	@$(TOUCH) $(DUMMY_FILE)
	
inner-copy-resources: $(MOD_META_STAMP_FILE) $(RESOURCE_STAMP_FILE)
	@$(TOUCH) $(DUMMY_FILE)

_JAVA_FILES := $(shell $(CAT) $(MOD_JAVA_CATALOG))

$(MOD_JAVA_STAMP_FILE): $(_JAVA_FILES)
ifneq "$(_JAVA_FILES)" ""
	$(call NOTE, Compiling ...)
	$(JAVAC) $(FINAL_JAVAC_OPT) $?
else
	$(call NOTE, Nothing to compile)
endif
	@$(TOUCH) $@


# Find the name of each RMI implementation class.  This is the
# name of the corresponding .class file, inside the $(MOD_CLASS_DIR).

_RMI_CLASS_FILES := \
	$(addprefix $(MOD_CLASS_DIR)$(SLASH), \
		$(addsuffix .class, \
			$(subst $(DOT),$(SLASH),$(RMI_CLASSES))))

# Here's where it gets real tricky; we need to reverse the prior
# process and get BACK to the class name.

$(RMI_STAMP_FILE): $(_RMI_CLASS_FILES)
ifneq "$(RMI_CLASSES)" ""
	$(call NOTE, Compiling RMI stubs and skeletons ...)
	$(RMIC) $(FINAL_RMIC_OPT) \
		$(subst $(SLASH),$(DOT), \
			$(subst .class,$(EMPTY), \
				$(subst $(MOD_CLASS_DIR)$(SLASH),$(EMPTY), $?)))
endif
	@$(TOUCH) $@

# Whenever an operation changes something inside the Jar staging area, it touches
# a stamp.  Whenever one of those stamps change, we touch the master-stamp (dirty jar)
# to force the actual Jar to be rebuilt.
# Additional pre-jar behaviours can be added by creating more dependencies
# for $(MOD_DIRTY_JAR_STAMP_FILE)

# Note:  for some reason (is this a make bug?), if there are multiple
# rules setting dependencies, then the command gets executed even though
# $? is empty.  This occurs with War.mk and WebLogic.mk that need to
# add additional dependencies to dirty jar stamp (to copy additional resources
# and such).  Go figure.

$(MOD_DIRTY_JAR_STAMP_FILE): $(RMI_STAMP_FILE) $(MOD_JAVA_STAMP_FILE) \
	$(RESOURCE_STAMP_FILE) $(MOD_META_STAMP_FILE)
	@$(if $?, $(TOUCH) $@)

# The catalog file has the path name, including the relative
# path to the source code root directory.  Like the Java files
# above, we change to the source code root directory and need
# to strip a prefix off of the name before it is useful.

_RESOURCE_FILES := $(shell $(CAT) $(MOD_RESOURCE_CATALOG))

$(RESOURCE_STAMP_FILE): $(_RESOURCE_FILES)
ifneq "$(_RESOURCE_FILES)" ""
	$(call NOTE, Copying package resources ...)
	@$(ECHO) Copying: $(notdir $?)
	@$(call COPY_TREE,$(FINAL_SOURCE_DIR), \
					  $(subst $(FINAL_SOURCE_DIR)$(SLASH),$(EMPTY),$?), \
					  $(ABSOLUTE_CLASS_DIR))
endif
	@$(TOUCH) $@

# End of the POST_SETUP block
	
endif

ifdef SETUP_CATALOGS

inner-setup-catalogs: $(MOD_JAVA_CATALOG) $(MOD_RESOURCE_CATALOG)
	$(DUMMY_RULE)
	
ABSOLUTE_MOD_BUILD_DIR := $(call JBE_CANONICALIZE, $(MOD_BUILD_DIR))

# Rules for rebuilding the catalog by visiting each
# Package.  Certain types of modules have no Java source (no PACKAGES are defined)
# but that's OK.

_PACKAGE_DIRS := \
	$(foreach package,$(PACKAGES), \
		$(FINAL_SOURCE_DIR)$(SLASH)$(subst $(DOT),$(SLASH),$(package))$(SLASH))

ifndef RESOURCE_EXTENSIONS
RESOURCE_EXTENSIONS := html application jwc script properties
endif

		
$(MOD_JAVA_CATALOG) $(MOD_RESOURCE_CATALOG):
	@$(ECHO) > $(MOD_JAVA_CATALOG)
	@$(ECHO) > $(MOD_RESOURCE_CATALOG)
ifneq "$(PACKAGES)" ""
	$(call NOTE, Cataloging packages ...)
	@$(ECHO) \
		$(wildcard $(addsuffix *.java,$(_PACKAGE_DIRS))) \
		 > $(MOD_JAVA_CATALOG)
	@$(ECHO) \
		$(wildcard \
			$(foreach ext,$(RESOURCE_EXTENSIONS), \
				$(addsuffix *.$(ext),$(_PACKAGE_DIRS)))) \
		> $(MOD_RESOURCE_CATALOG)
endif

# End of SETUP_CATALOGS block

endif

javadoc:
ifeq "$(JAVADOC_DIR)" ""
	$(error Must define JAVADOC_DIR in Makefile)
endif
ifeq "$(PACKAGES)" ""
	$(error Must define PACKAGES in Makefile)
endif
	$(call NOTE, Generating Javadoc ...)
	@$(MKDIRS) $(JAVADOC_DIR)
	$(JAVADOC) -d $(JAVADOC_DIR) -sourcepath $(FINAL_SOURCE_DIR) \
	-classpath "$(call JBE_CANONICALIZE,-classpath $(MOD_CLASSPATH) $(PROJ_CLASSPATH) $(MOD_CLASS_DIR))" \
	$(JAVADOC_OPT) $(PACKAGES)

	
FINAL_META_RESOURCES := $(strip $(MOD_META_RESOURCES) $(META_RESOURCES))

$(MOD_META_STAMP_FILE): $(FINAL_META_RESOURCES)
ifneq "$(FINAL_META_RESOURCES)" ""
	$(call NOTE, Copying META-INF resources ...)
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) $? $(MOD_META_INF_DIR)
	@$(TOUCH) $(MOD_DIRTY_JAR_STAMP_FILE)
endif
	@$(TOUCH) $@ 

initialize: setup-jbe-util
	@$(MKDIRS) $(MOD_BUILD_DIR)

# May be implemented

.PHONY: module-clean

.PHONY: inner-compile inner-copy-resources
.PHONY: clean clean-root
.PHONY: setup-catalogs catalog-package
.PHONY: compile copy-resources javadoc
.PHONY: setup-jbe-util
.PHONY: inner-setup-catalogs
.PHONY: install
.PHONY: initialize module-initialize
