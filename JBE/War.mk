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

include $(SYS_MAKEFILE_DIR)/ModuleRules.mk

default: war

# Initializer, makes sure some directories are there

initialize: war-initialize local-initialize

war-initialize:
	@$(MKDIRS) $(MOD_CLASS_DIR) $(WAR_LIB_DIR) $(MOD_META_INF_DIR)

# To create a War, you need to get everything compiled and copy over
# all resources.  It's still a Jar file, even though the extension is
# different.

web-application: compile copy-resources \
	copy-meta-resources copy-context-resources \
	$(WAR_LIB_STAMP_FILE) local-web-application

# local-web-application can be provided to do any final setup
# of the web application directory after all normal rules
# have processed.  This will happen before the application directory
# is jar'ed up.

local-web-application: compile copy-resources \
	copy-meta-resources copy-context-resources \
	$(WAR_LIB_STAMP_FILE)

war: web-application $(JAR_FILE) local-post-jar

# local-post-jar may be provided to do additional work to the web archive
# file after it has been created, such as signing or sealing it.

local-post-jar: $(JAR_FILE)

# Build the Jar file by compiling into it everything in the classes
# directory.

$(MOD_DIRTY_JAR_STAMP_FILE):: web-application

# Here's the main way a War differs from a Jar ... we Jar up
# the entire web application directory.

$(JAR_FILE): $(MOD_DIRTY_JAR_STAMP_FILE)
ifeq "$(MODULE_NAME)" ""
	@$(ECHO) JBE Error:  Must set MODULE_NAME in Makefile
else
	@$(ECHO) "\n*** Building $(JAR_FILE) ... ***\n"
	$(JAR) cf $(JAR_FILE) -C $(WAR_APP_DIR) .
endif

install: war war-install local-install

war-install:war
ifeq $(INSTALL_DIR)" ""
	@$(ECHO) JBE Error: Must set INSTALL_DIR in Makefile
else
ifneq "$(MODULE_NAME)" ""
	@$(ECHO) "\n*** Installing $(JAR_FILE) to $(INSTALL_DIR) ***\n"
	@$(CP) $(JAR_FILE) --force $(INSTALL_DIR)
endif
endif

# Additional rule that will fire after the WAR is installed.

local-install: war-install

$(WAR_LIB_STAMP_FILE): $(INSTALL_LIBRARIES)
ifdef INSTALL_LIBRARIES
	@$(ECHO) "\n*** Copying runtime libraries ... ***\n"
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) --force $? $(WAR_LIB_DIR)
	@$(TOUCH) $(MOD_DIRTY_JAR_STAMP_FILE)
endif
	@$(TOUCH) $@ 

$(WAR_WEB_INF_STAMP_FILE): web.xml $(WEB_INF_RESOURCES)
	@$(ECHO) "\n*** Copying WEB-INF resources ... ***\n"
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) --force $? $(WAR_INF_DIR)
	@$(TOUCH) $@ $(MOD_DIRTY_JAR_STAMP_FILE)

ifneq "$(CONTEXT_RESOURCES)" ""

# Use find to expand this list of file names and directories to
# a total list of every single file, pruning out CVS directories
# along the way.

FINAL_CONTEXT_RESOURCES := \
	${shell $(FIND) $(CONTEXT_RESOURCES) \
		-name \* ! \( -name CVS -prune \) -type f \
		-print}

$(WAR_CONTEXT_STAMP_FILE): $(FINAL_CONTEXT_RESOURCES)
	@$(ECHO) "\n*** Copying context resources ... ***\n"
	@$(ECHO) Copying: $(notdir $?)
	@$(CP) --force --parents $? $(WAR_APP_DIR)
	@$(TOUCH) $@ $(MOD_DIRTY_JAR_STAMP_FILE)
else

# No context resources, then no reason to even create the stamp
# file.  Nothing to copy.

$(WAR_CONTEXT_STAMP_FILE):
endif

copy-resources: $(WAR_WEB_INF_STAMP_FILE)

copy-context-resources: $(WAR_CONTEXT_STAMP_FILE)

.PHONY: war install initialize clean-root 
.PHONY: web-application copy-meta-resources copy-context-resources

# Rules that may be provided elsewhere.

.PHONY: local-initialize local-post-jar local-web-application local-install



