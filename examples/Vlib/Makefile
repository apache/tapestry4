# $Id$

PROJECT_NAME = Vlib

PACKAGES := \
	com.primix.vlib \
	com.primix.vlib.pages \
	com.primix.vlib.pages.admin \
	com.primix.vlib.components \

INSTALL_DIR := ../../lib

PROJ_CLASSPATH = \
	$(INSTALL_DIR)/ejb.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/com.primix.tapestry.jar \
	$(INSTALL_DIR)/net.sf.tapestry.contrib.jar \
	$(INSTALL_DIR)/VlibBeans.jar \
	$(INSTALL_DIR)/log4j-core.jar

JAVADOC_DIR := javadoc

JAVADOC_OPT := \
	-version -author -windowtitle "Primix Virtual Library - Presentation Layer" \
	-link http://tapestry.primix.com/doc/api \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

CONTEXT_RESOURCES := \
	index.html \
	images \
	scripts

include $(SYS_MAKEFILE_DIR)/War.mk

LOCAL_PROPS_DIR := $(SYS_BUILD_DIR_NAME)/props

EXEC_CLASSPATH := \
	$(FINAL_SOURCE_DIR) \
	$(PROJ_CLASSPATH) \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar \
	$(INSTALL_DIR)/org.mortbay.jetty.jar \
	$(INSTALL_DIR)/log4j-core.jar \
	$(JBOSS_DIR)/client/jboss-client.jar \
	$(JBOSS_DIR)/client/jbosssx-client.jar \
	$(JBOSS_DIR)/client/jnp-client.jar \
	$(LOCAL_PROPS_DIR)
	
# Previously, we just added $(JBOSS_DIR)/conf/default to the classpath,
# but that now contains a log4j.properties file that confuses
# the log4j running inside Jetty.  As a patch, we copy the jndi.properties
# file locally and add the directory it is copied to to the execution
# classpath.

JBOSS_JNDI_PROPS := $(JBOSS_DIR)/conf/default/jndi.properties
LOCAL_JNDI_PROPS := $(LOCAL_PROPS_DIR)/jndi.properties

$(LOCAL_JNDI_PROPS): $(JBOSS_JNDI_PROPS)
	$(call NOTE, Copying jndi.properties ...)
	$(MKDIRS) $(LOCAL_PROPS_DIR)
	$(CP) $(JBOSS_JNDI_PROPS) $(LOCAL_JNDI_PROPS)
	
run: default $(LOCAL_JNDI_PROPS)
	$(call NOTE, Running Primix Virtual Library using Jetty ...)
	$(call EXEC_JAVA,$(EXEC_CLASSPATH), \
		-showversion \
		-Dcom.primix.vlib.debug-enabled=true \
		-Dcom.primix.tapestry.enable-reset-service=true \
		$(OTHER_VLIB_OPTS) \
		org.mortbay.jetty.Server jetty.xml)


