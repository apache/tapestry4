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

# This Makefile works with jBoss 2.0.
#
# It requires that the environment variable JBOSS_DIR be set.  It should
# be set to the root directory into which JBOSS was installed (this
# is typically C:\jboss under Windows).  This variable
# may be set as an environment variable, but is more typically set
# inside $(SYS_MAKEFILE_DIR)/config/LocalConfig.mk.  

# jBoss is easier to deal with than WebLogic 5.1 since it doesn't require
# that EJB jars be run through a postcompiler (EJBC).  Also, it's hot deploy
# is as simple as copying the necessary file to the proper directory.

# If you are using jBoss JAWS or a jboss.xml deployment descriptor (remember,
# it's optional under jBoss) then add those files to META_RESOURCES.

MOD_META_RESOURCES = ejb-jar.xml 

include $(SYS_MAKEFILE_DIR)/Jar.mk

# Automatically add a few entries to the classpath when compiling
# for jBoss

MOD_CLASSPATH := \
	$(JBOSS_DIR)/lib/ext/ejb.jar \
	$(JBOSS_DIR)/lib/ext/jndi.jar \
	$(JBOSS_DIR)/lib/jdbc2_0-stdext.jar

JBOSS_DEPLOY_DIR := $(JBOSS_DIR)/deploy

DEPLOY_JAR := $(JBOSS_DEPLOY_DIR)/$(JAR_FILE)

deploy: $(DEPLOY_JAR)

$(DEPLOY_JAR): $(JAR_FILE)
	@$(ECHO) "\n*** Deploying $(JAR_FILE) ... ***\n"
	@$(MKDIRS) $(JBOSS_DEPLOY_DIR)
	@$(CP) -f $(JAR_FILE) $(JBOSS_DEPLOY_DIR)

#
# Convienience for running the EJX deployment tool

run-ejx:
	$(CD) $(JBOSS_DIR)/bin ; $(JAVA) -jar ejx.jar 