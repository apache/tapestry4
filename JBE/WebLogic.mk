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

# Makefile that defines additional rules for creating WebLogic EJBs.  This
# was built against WebLogic 5.1.
#
# Works just like a Jar project, except that it then produces a
# deployable version of the Jar file as well, named $(JAR_NAME)-deploy.$(JAR_EXT)
#
# Overrides a number of local- rules: local-clean, local-install
#
#
# You may specify options for ejbc using SITE_EJBC_OPT or EJBC_OPT.
# -keepgenerated is pretty useful.

MOD_META_RESOURCES = ejb-jar.xml weblogic-ejb-jar.xml

include $(SYS_MAKEFILE_DIR)/Jar.mk

DEPLOY_JAR_FILE := $(MODULE_NAME)-deploy.$(JAR_EXT)

local-clean:
	@$(RMDIRS) $(DEPLOY_JAR_FILE)

local-install: 
	@$(ECHO) "\n*** Installing $(DEPLOY_JAR_FILE) to $(INSTALL_DIR) ... ***\n"
	@$(CP) --force $(DEPLOY_JAR_FILE) $(INSTALL_DIR)

local-post-jar: $(DEPLOY_JAR_FILE)

WEBLOGIC_CLASSPATH := $(WEBLOGIC_DIR)/lib/weblogicaux.jar $(WEBLOGIC_DIR)/classes

MOD_CLASSPATH := $(WEBLOGIC_CLASSPATH)

EJBC_CLASSPATH =  $(shell $(JBE_CANONICALIZE) -classpath \
						$(WEBLOGIC_CLASSPATH) $(SITE_CLASSPATH) $(LOCAL_CLASSPATH))

FINAL_EJBC_OPT := $(strip $(SITE_EJBC_OPT) $(EJBC_OPT))

# After building the initial Jar, build the deployment jar too.

# EJBC leaves lots of garbage around, so we'll switch to the
# build directory

$(DEPLOY_JAR_FILE): $(JAR_FILE)
	@$(ECHO) "\n*** Creating $(DEPLOY_JAR_FILE) ... ***\n"
	$(CD) $(MOD_BUILD_DIR) ; \
	$(JAVA) -classpath "$(EJBC_CLASSPATH)" \
	weblogic.ejbc $(FINAL_EJBC_OPT) ../$(JAR_FILE) ../$(DEPLOY_JAR_FILE) 
