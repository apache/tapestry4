# $Id$

PROJECT_NAME = net.sf.tapestry.contrib

PACKAGES = \
	net.sf.tapestry.contrib.form \
	net.sf.tapestry.contrib.palette
	
PROJ_CLASSPATH = \
    $(INSTALL_DIR)/com.primix.tapestry.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar \
	$(INSTALL_DIR)/ejb.jar \
	$(INSTALL_DIR)/log4j-core.jar

RESOURCE_EXTENSIONS := html jwc gif script js

INSTALL_DIR = ../lib

JAVADOC_DIR = api

JAVADOC_OPT = \
	-version -author -splitindex -windowtitle "Tapestry Contrib API" \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api \
	-link http://jakarta.apache.org/log4j/docs/api \
	-link http://java.sun.com/xml/jaxp-docs-1.1/docs/api \
	-link http://tapestry.primix.com/doc/api

include $(SYS_MAKEFILE_DIR)/Jar.mk

