# $Id$

JAVA_FILES = *.java

PACKAGES = com.primix.vlib.ejb

MODULE_NAME = VlibBeans

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/Tapestry.jar 

INSTALL_DIR = ../../lib

META_RESOURCES = *.xml

JAVADOC_DIR = javadoc

JAVADOC_OPT = \
	-version -author \
	-windowtitle "Primix Virtual Library - Enterprise JavaBeans" \
	-link http://tapestry.primix.com/doc/api \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

include $(SYS_MAKEFILE_DIR)/jBoss.mk

JBOSS_CONF_DIR = $(JBOSS_DIR)/conf/default

RUNTIME_LIBRARIES := \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/log4j.jar
	
# Rule to configure the jBoss server for the VlibBeans
# demo.

configure-jboss:  setup-jbe-util
ifeq ("$(JBOSS_DIR)","")
	$(error You must set JBOSS_DIR (in $(SYS_MAKEFILE_DIR)/config/LocalConfig.mk) \
	before configuring jBoss.)
endif	
	@$(ECHO) "\n*** Installing Primix Vlib database ... ***\n"
	$(CAT) jboss/vlib.tar.gz | \
		($(CD) $(JBOSS_DIR)/db && $(TAR) $(TAR_EXTRACT_OPT) $(TAR_GZIP_OPT))
	$(CP) jboss/vlib.properties $(JBOSS_CONF_DIR)
	@$(ECHO) "\n*** Splicing changes to jboss.conf ... ***\n"
	$(call JBE_SPLICE,PrimixVlib,$(JBOSS_CONF_DIR)/jboss.conf,jboss/jboss.conf.splice)
	@$(ECHO) "\n*** Splicing changes to jboss.jcml ... ***\n"
	$(call JBE_SPLICE,PrimixVlib,$(JBOSS_CONF_DIR)/jboss.jcml,jboss/jboss.jcml.splice, \
		-before \</server\>)
	@$(ECHO) "\n*** Setting up JNDI properties ... ***\n"
	$(CP) $(CP_FORCE_OPT) jboss/jndi.properties $(JBOSS_CONF_DIR)
	@$(ECHO) "\n*** Copying runtime libraries ... ***\n"
	$(CP) $(CP_FORCE_OPT) $(RUNTIME_LIBRARIES) $(JBOSS_DIR)/lib/ext
	@$(ECHO) "\n*** jBoss is ready to run ***\n"



