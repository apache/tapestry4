# $Id$

PROJECT_NAME = Vlib

PACKAGES := \
	com.primix.vlib \
	com.primix.vlib.pages \
	com.primix.vlib.components \

INSTALL_DIR := ../../lib

PROJ_CLASSPATH = \
	$(JBOSS_DIR)/client/ejb.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/com.primix.tapestry.jar \
	$(INSTALL_DIR)/VlibBeans.jar

JAVADOC_DIR := javadoc

JAVADOC_OPT := \
	-version -author -windowtitle "Primix Virtual Library - Presentation Layer" \
	-link http://tapestry.primix.com/doc/api \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

CONTEXT_RESOURCES := index.html

include $(SYS_MAKEFILE_DIR)/War.mk

EXEC_CLASSPATH := \
	$(PROJ_CLASSPATH) \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/com.mortbay.jetty.jar \
	$(INSTALL_DIR)/log4j-core.jar \
	$(JBOSS_DIR)/conf/default \
	$(JBOSS_DIR)/client/jboss-client.jar

run: default
	$(call NOTE, Running Primix Virtual Library using Jetty ...)
	$(call EXEC_JAVA,$(EXEC_CLASSPATH), \
		-showversion \
		-Dcom.primix.tapestry.enable-reset-service=true \
		$(OTHER_EXEC_OPTS) \
		com.mortbay.Jetty.Server jetty.xml)





