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

# Makefile that defines additional rules for creating WebLogic EJBs.  This
# was built for WebLogic 5.1.
#
# For WebLogic 6.0, this doesn't appear to be necessary, the server will automatically
# run ejbc internally as it deploys files.  It may still be possible and desirable
# to do this during development rather than at deployment. 
#
# Works just like a Jar project, except that it then produces a
# deployable version of the Jar file as well, named $(JAR_NAME)-deploy.$(JAR_EXT)
#
# Overrides a number of local- rules: local-clean, module-install
#
#
# You may specify options for ejbc using LOCAL_EJBC_OPT or EJBC_OPT.
# -keepgenerated is pretty useful.

# WebLogic should always be compiled using Sun's JDK 1.2.2

MOD_JDK_VENDOR = Sun
MOD_JDK_RELEASE = 1.2.2

# All WebLogic module musthave these two files, which will be installed
# into the  META-INF directory of the JAR.

MOD_META_RESOURCES = ejb-jar.xml weblogic-ejb-jar.xml

include $(SYS_MAKEFILE_DIR)/Jar.mk

DEPLOY_JAR_FILE := $(PROJECT_NAME)-deploy.$(JAR_EXT)

local-clean:
	@$(RM) $(DEPLOY_JAR_FILE)

module-install: $(INSTALL_DIR)/$(DEPLOY_JAR_FILE)

$(INSTALL_DIR)/$(DEPLOY_JAR_FILE): $(DEPLOY_JAR_FILE)
ifeq "$(INSTALL_DIR)" ""
	$(error Must define INSTALL_DIR in Makefile)
endif
	$(call NOTE, Installing $(DEPLOY_JAR_FILE) to $(INSTALL_DIR) ... )
	@$(CP) $(CP_FORCE_OPT) $(DEPLOY_JAR_FILE) $(INSTALL_DIR)

# Add a dependency to inner-jar that causes the deployable jar file to
# be created.

inner-jar: $(DEPLOY_JAR_FILE)

WEBLOGIC_CLASSPATH := \
	$(WEBLOGIC_DIR)/lib/weblogicaux.jar \
	$(WEBLOGIC_DIR)/classes

# MOD_CLASSPATH will be added to the classpath of any module compiled
# using this makefile.

MOD_CLASSPATH := $(WEBLOGIC_CLASSPATH)

EJBC_CLASSPATH = $(WEBLOGIC_CLASSPATH) $(PROJ_CLASSPATH)

FINAL_EJBC_OPT := $(strip $(LOCAL_EJBC_OPT) $(EJBC_OPT))

# After building the initial Jar, build the deployment jar too.

# Make the deployable JAR dependent on any .jar or .zip in the classpath.
# EJBC leaves lots of garbage around, so we'll switch to the
# build directory

$(DEPLOY_JAR_FILE): $(JAR_FILE) $(filter %.jar %.zip,$(PROJ_CLASSPATH))
	$(call NOTE, Creating $(DEPLOY_JAR_FILE) ...)
	$(CD) $(MOD_BUILD_DIR) ; \
	$(call EXEC_JAVA,$(EJBC_CLASSPATH), \
		weblogic.ejbc $(FINAL_EJBC_OPT) ../$(JAR_FILE) ../$(DEPLOY_JAR_FILE))
