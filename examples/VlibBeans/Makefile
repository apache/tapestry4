# $Id$

JAVA_FILES = *.java

PACKAGES = com.primix.vlib.ejb

MODULE_NAME = VlibBeans

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/Tapestry.jar 

INSTALL_DIR = ../../lib

META_RESOURCES = *.xml

JAVADOC_DIR = ../../javadoc

JAVADOC_OPT = \
	-version -author -windowtitle "Primix Virtual Library - Enterprise JavaBeans" \
	-link http://java.sun.com/products/jdk/1.2/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

include $(SYS_MAKEFILE_DIR)/jBoss.mk

# Rule to configure the jBoss server for the VlibBeans
# demo.

configure-jboss: 
ifeq ("$(JBOSS_DIR)","")
	@$(ECHO) "You must set JBOSS_DIR (in $(SYS_MAKEFILE_DIR)/config/LocalConfig.mk)"
	@$(ECHO) "before configuring jBoss."
else
	@$(ECHO) "\n*** Installing Hypersonic SQL database data ... ***\n"
	@$(MKDIRS) $(JBOSS_DIR)/db/hypersonic
	@$(CP) jboss/default.script $(JBOSS_DIR)/db/hypersonic
	@$(ECHO) "\n*** Setting up JNDI properties ... ***\n"
	@$(CP) --force jboss/jndi.properties $(JBOSS_DIR)/conf/default
	@$(ECHO) "\n*** Copying runtime libraries ... ***\n"
	@$(CP) --force $(INSTALL_DIR)/Tapestry.jar $(JBOSS_DIR)/lib/ext
	@$(ECHO) "\n*** jBoss is ready to run ***\n"
endif

run: deploy
	@$(ECHO) "\n*** Running jBoss ... ***\n"
	$(CD) $(JBOSS_DIR)/bin ; $(JAVA) -jar run.jar


