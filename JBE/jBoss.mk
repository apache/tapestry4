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

# Always make sure that the version of Crimson (the default XML parser, from Sun)
# shipped with jBoss is in the classpath.

JBOSS_CLASSPATH := \
	$(JBOSS_DIR)/lib/crimson.jar \
	$(OTHER_JBOSS_CLASSPATH)

deploy: setup-catalogs
	@$(RECURSE) POST_SETUP=t inner-deploy
	
inner-deploy: $(DEPLOY_JAR)
	@$(TOUCH) $(DUMMY_FILE)

$(DEPLOY_JAR): $(JAR_FILE)
	$(call NOTE, Deploying $(JAR_FILE) ... )
	@$(MKDIRS) $(JBOSS_DEPLOY_DIR)
	@$(CP) $(CP_FORCE_OPT) $(JAR_FILE) $(JBOSS_DEPLOY_DIR)

#
# Convienience for running the EJX deployment tool

run-ejx:
	$(CD) $(JBOSS_DIR)/bin ; \
	$(call EXEC_JAVA,$(JBOSS_CLASSPATH), -jar ejx.jar)

# 
# Convienience for running jBoss.  Specify JBOSS_OPT in the Makefile
# to set JVM configuration, such as memory allocation.
#
	
run: deploy
	$(call NOTE, Running jBoss ... )
	$(CD) $(JBOSS_DIR)/bin ; \
	$(call EXEC_JAVA,$(JBOSS_CLASSPATH) $(JBOSS_DIR)/bin/run.jar, \
		$(JBOSS_JAVA_OPT) org.jboss.Main $(OTHER_JBOSS_OPT))


.PHONY: deploy inner-deploy run-ejx run