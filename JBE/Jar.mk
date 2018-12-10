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

JAR_EXT := jar

include $(SYS_MAKEFILE_DIR)/ModuleDefs.mk

# Start of rules.  The default rule is to compile all packages.

default: jar

# Define the class dir for the Jar module

MOD_CLASS_DIR := $(SYS_BUILD_DIR_NAME)/classes
MOD_META_INF_DIR := $(MOD_CLASS_DIR)/META-INF

include $(SYS_MAKEFILE_DIR)/ModuleRules.mk

# Build the final classpath used for execution

FINAL_EXECUTION_CLASSPATH := \
	$(strip $(EXECUTION_CLASSPATH) . $(MOD_CLASS_DIR))

EXECUTION_CLASSPATH_OPTION := \
	-classpath "$(subst $(SPACE),$(CLASSPATHSEP),$(FINAL_EXECUTION_CLASSPATH))"

# Initializer, makes sure some directories are there

initialize: jar-initialize local-initialize

local-initialize: jar-initialize

jar-initialize:
	@$(MKDIRS) $(MOD_CLASS_DIR) $(MOD_META_INF_DIR)

# To create a jar, you need to get everything compiled and copy over
# all resources

jar: compile copy-resources $(JAR_FILE) local-post-jar

# Build the Jar file by compiling into it everything in the classes
# directory.

$(MOD_DIRTY_JAR_STAMP_FILE):: compile copy-resources 

$(JAR_FILE):: compile copy-resources local-pre-jar

# local-pre-jar can perform any final changes to the build directory
# before it is wrapped up as a Jar file

local-pre-jar: compile copy-resources

# local-post-jar may be implemented to perform additional work on
# the jar file, such as signing it.

local-post-jar: $(JAR_FILE)

$(JAR_FILE):: $(MOD_DIRTY_JAR_STAMP_FILE)
ifeq "$(MODULE_NAME)" ""
	@$(ECHO) JBE Error: Must set MODULE_NAME in Makefile
else
	@$(ECHO) "\n*** Building $(JAR_FILE) ... ***\n"
	$(JAR) cf $(JAR_FILE) -C $(MOD_CLASS_DIR) .
endif

install: jar-install local-install

jar-install: jar
ifeq "$(INSTALL_DIR)" ""
	@$(ECHO) JBE Error: Must set INSTALL_DIR in Makefile
else
ifneq "$(MODULE_NAME)" ""
	@$(ECHO) "\n*** Installing $(JAR_FILE) to $(INSTALL_DIR) ***\n"
	@$(CP) $(JAR_FILE) --force $(INSTALL_DIR)
endif
endif

# local-install allows additional installation work to follow the normal
# install.

local-install: jar-install

.PHONY: jar install initialize 
.PHONY: default

# Additional rules that can be implemented elsewhere

.PHONY: local-initialize local-pre-jar local-post-jar local-install

