# $Id$

PACKAGES := com.primix.vlib.ejb

PROJECT_NAME := VlibBeans

INSTALL_DIR := ../../lib

PROJ_CLASSPATH := \
	$(INSTALL_DIR)/com.primix.tapestry.jar

META_RESOURCES := *.xml

JAVADOC_DIR := javadoc

JAVADOC_OPT := \
	-version -author \
	-windowtitle "Primix Virtual Library - Enterprise JavaBeans" \
	-link http://tapestry.primix.com/doc/api \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

JBOSS_JAVA_OPT := -Xms128m -Xmx128m

include $(SYS_MAKEFILE_DIR)/jBoss.mk

JBOSS_CONF_DIR = $(JBOSS_DIR)/conf/default

# At one time there were more libraries, but they are now already packaged in JBoss 2.2.1.

RUNTIME_LIBRARIES := \
	$(PROJ_CLASSPATH)
	
# Rule to configure the jBoss server for the VlibBeans
# demo.

configure-jboss:  setup-jbe-util
ifeq ("$(JBOSS_DIR)","")
	$(error You must set JBOSS_DIR (in $(SYS_MAKEFILE_DIR)/config/LocalConfig.mk) \
	before configuring jBoss)
endif
	@$(RECURSE) setup-db	
	$(call NOTE, Splicing changes to jboss.jcml ...)
	$(call JBE_SPLICE, \
		PrimixVlib, \
		$(JBOSS_CONF_DIR)/jboss.jcml, \
		jboss/jboss.jcml.splice, \
		-before "J2EE deployment")
	$(call NOTE, Setting up JNDI properties ...)
	$(CP) $(CP_FORCE_OPT) jboss/jndi.properties $(JBOSS_CONF_DIR)
	$(call NOTE, Copying runtime libraries ...)
	$(CP) $(CP_FORCE_OPT) $(RUNTIME_LIBRARIES) $(JBOSS_DIR)/lib/ext
	$(call NOTE, jBoss is ready to run)

# Provide a do-nothing command for the setup-db rule (to prevent
# a "nothing to do" warning).

setup-db:  $(JBOSS_CONF_DIR)/vlib.properties
	@$(TOUCH) $(SYS_BUILD_DIR)/dummy

# Only install the vlib database if the vlib.properties file doesn't exist.
# The database is in two parts:  the vlib.properties file, and a directory
# containg the InstantDB tables, indexes & etc.

$(JBOSS_CONF_DIR)/vlib.properties:
	$(call NOTE, Installing Primix Vlib database ...)
	$(CP) jboss/vlib.properties $(JBOSS_CONF_DIR)
	$(CAT) jboss/vlib.tar.gz | \
		($(CD) $(JBOSS_DIR)/db && $(GNUTAR) $(GNUTAR_EXTRACT_OPT) $(GNUTAR_GZIP_OPT))

.PHONY: setup-db configure-jboss

