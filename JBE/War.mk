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

JAR_EXT := war

include $(SYS_MAKEFILE_DIR)/ModuleDefs.mk

# Define the class dir for the War module.  We set up
# a wapp (web application) directory that will contain the
# structure we eventually Jar up as a Web ARchive.

WAR_APP_DIR := $(SYS_BUILD_DIR_NAME)/wapp
WAR_INF_DIR := $(WAR_APP_DIR)/WEB-INF
MOD_CLASS_DIR := $(WAR_INF_DIR)/classes
WAR_LIB_DIR := $(WAR_INF_DIR)/lib
WAR_LIB_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/wapp-libs-stamp
WAR_WEB_INF_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/wapp-web-inf-stamp
WAR_CONTEXT_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/wapp-context-stamp
MOD_META_INF_DIR := $(WAR_APP_DIR)/META-INF

default: war

include $(SYS_MAKEFILE_DIR)/ModuleRules.mk

module-initialize:
	@$(MKDIRS) $(MOD_CLASS_DIR) $(MOD_META_INF_DIR) $(WAR_LIB_DIR)

war: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-war

inner-war: $(JAR_FILE)
	@$(TOUCH) $(DUMMY_FILE)

$(MOD_DIRTY_JAR_STAMP_FILE): $(WAR_CONTEXT_STAMP_FILE) $(WAR_LIB_STAMP_FILE) $(WAR_WEB_INF_STAMP_FILE)

# Here's the main way a War differs from a Jar ... we Jar up
# the entire web application directory.

$(JAR_FILE): $(MOD_DIRTY_JAR_STAMP_FILE)
ifeq "$(PROJECT_NAME)" ""
	$(error Must set PROJECT_NAME in Makefile)
else
	$(call NOTE, Building $(JAR_FILE) ... )
	$(JAR) cf $(JAR_FILE) -C $(WAR_APP_DIR) .
endif

inner-install: $(INSTALL_DIR)/$(JAR_FILE)
	@$(TOUCH) $(DUMMY_FILE)

$(INSTALL_DIR)/$(JAR_FILE): $(JAR_FILE)
ifeq "$(INSTALL_DIR)" ""
	$(error Must define INSTALL_DIR in Makefile)
endif
ifeq "$(PROJECT_NAME)" ""
	$(error Must define PROJECT_NAME in Makefile)
endif
	$(call NOTE, Installing $(JAR_FILE) to $(INSTALL_DIR))
	@$(CP) $(CP_FORCE_OPT) $(JAR_FILE) $(INSTALL_DIR)

$(WAR_LIB_STAMP_FILE): $(INSTALL_LIBRARIES)
ifdef INSTALL_LIBRARIES
	$(call NOTE, Copying runtime libraries ... )
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) $(CP_FORCE_OPT) $? $(WAR_LIB_DIR)
	@$(TOUCH) $@ 
endif

$(WAR_WEB_INF_STAMP_FILE): web.xml $(WEB_INF_RESOURCES)
	$(call NOTE, Copying WEB-INF resources ...)
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) $(CP_FORCE_OPT) $? $(WAR_INF_DIR)
	@$(TOUCH) $@

ifneq "$(CONTEXT_RESOURCES)" ""

# Use find to expand this list of file names and directories to
# a total list of every single file, pruning out CVS directories
# along the way.

FINAL_CONTEXT_RESOURCES := \
	${shell $(FIND) $(CONTEXT_RESOURCES) \
		-name \* ! \( -name CVS -prune \) -type f \
		-print}

$(WAR_CONTEXT_STAMP_FILE): $(FINAL_CONTEXT_RESOURCES)
	$(call NOTE, Copying context resources ...)
	@$(ECHO) Copying: $(notdir $?)
	@$(call COPY_TREE, . , $? , $(WAR_APP_DIR))
	@$(TOUCH) $@ $(MOD_DIRTY_JAR_STAMP_FILE)
else

# No context resources, then no reason to even create the stamp
# file.  Nothing to copy.

$(WAR_CONTEXT_STAMP_FILE):
endif

.PHONY: default war inner-war




