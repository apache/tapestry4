# $Id$

MODULE_NAME = Vlib

PACKAGES = \
	com.primix.servlet \
	com.primix.vlib \
	com.primix.vlib.pages \
	com.primix.vlib.components \
	com.primix.vlib.jsp

LOCAL_CLASSPATH = \
	$(JBOSS_DIR)/client/ejb.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/VlibBeans.jar

INSTALL_DIR = ../../lib

JAVADOC_DIR = ../../javadoc

JAVADOC_OPT = \
	-version -author -windowtitle "Primix Virtual Library - Presentation Layer" \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

CONTEXT_RESOURCES = index.html jsp

include $(SYS_MAKEFILE_DIR)/War.mk

EXEC_CLASSPATH = \
	$(LOCAL_CLASSPATH) \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/com.mortbay.jetty.jar \
	$(INSTALL_DIR)/org.apache.jasper.jar \
	$(JBOSS_DIR)/conf/default \
	$(JBOSS_DIR)/client/jboss-client.jar \
	$(JDK_DIR)/lib/tools.jar

run: default
	$(JAVA) -classpath "$(call JBE_CANONICALIZE,-classpath $(EXEC_CLASSPATH))" \
	-Dcom.primix.tapestry.enable-reset-service=true \
	-Dorg.xml.sax.parser=org.apache.xerces.parsers.SAXParser \
	com.mortbay.Jetty.Server jetty.xml





