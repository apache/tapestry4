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

# Start of rules.  The default rule is to build the JAR.

default: jar

# Define the class dir for the Jar module

MOD_CLASS_DIR := $(SYS_BUILD_DIR_NAME)/classes
MOD_META_INF_DIR := $(MOD_CLASS_DIR)/META-INF

include $(SYS_MAKEFILE_DIR)/ModuleRules.mk

# Initializer, makes sure some directories are there

module-initialize:
	@$(MKDIRS) $(MOD_CLASS_DIR) $(MOD_META_INF_DIR)

jar: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-jar

# To create a jar, you need to get everything compiled and copied over
# all resources.  This inner rule is invoked in a recursive make, after
# the cataloging has been done.

inner-jar: $(JAR_FILE)
	@$(TOUCH) $(DUMMY_FILE)

# Rebuild the JAR file when its contents (from the staging area in $(MOD_CLASS_DIR))
# has changed, as identified by the dirty jar stamp.

$(JAR_FILE): $(MOD_DIRTY_JAR_STAMP_FILE)
ifeq "$(MODULE_NAME)" ""
	$(error Must define MODULE_NAME in Makefile)
endif
	@$(ECHO) "\n*** Building $(JAR_FILE) ... ***\n"
	$(JAR) cf $(JAR_FILE) -C $(MOD_CLASS_DIR) .

# Another rule invoked in the recursive make.

inner-install: jar-install module-install
	@$(TOUCH) $(DUMMY_FILE)

jar-install: $(INSTALL_DIR)/$(JAR_FILE)

$(INSTALL_DIR)/$(JAR_FILE): $(JAR_FILE)
ifeq "$(INSTALL_DIR)" ""
	$(error Must define INSTALL_DIR in Makefile)
endif
ifeq "$(MODULE_NAME)" ""
	$(error Must define MODULE_NAME in Makefile)
endif
	@$(ECHO) "\n*** Installing $(JAR_FILE) to $(INSTALL_DIR) ***\n"
	@$(CP) $(JAR_FILE) -f $(INSTALL_DIR)

# module-install allows additional installation work to follow the normal
# install.

module-install: jar-install

.PHONY: jar install initialize jar-install module-install
.PHONY: default inner-jar

