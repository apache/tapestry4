# $Id$

PROJECT_NAME = com.primix.tapestry

PACKAGES = \
	com.primix.foundation \
	com.primix.foundation.exception \
	com.primix.foundation.io \
	com.primix.foundation.prop \
	com.primix.foundation.jdbc \
	com.primix.foundation.ejb \
	com.primix.foundation.pool \
	com.primix.foundation.xml \
	com.primix.tapestry \
	com.primix.tapestry.asset \
	com.primix.tapestry.binding \
	com.primix.tapestry.components \
	com.primix.tapestry.components.html \
	com.primix.tapestry.components.html.link \
	com.primix.tapestry.components.html.form \
	com.primix.tapestry.components.html.valid \
	com.primix.tapestry.engine \
	com.primix.tapestry.event \
	com.primix.tapestry.pageload \
	com.primix.tapestry.pages \
	com.primix.tapestry.parse \
	com.primix.tapestry.record \
	com.primix.tapestry.spec \
	com.primix.tapestry.inspector \
	com.primix.tapestry.script \
	com.primix.tapestry.wml \
	com.primix.tapestry.callback

PROJ_CLASSPATH = \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar \
	$(INSTALL_DIR)/ejb.jar \
	$(INSTALL_DIR)/log4j-core.jar

INSTALL_DIR = ../lib

JAVADOC_DIR = ../doc/api

JAVADOC_OPT = \
	-version -author -splitindex -windowtitle "Tapestry API" \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api \
	-link http://jakarta.apache.org/log4j/docs/api \
	-link http://java.sun.com/xml/jaxp-docs-1.1/docs/api

include $(SYS_MAKEFILE_DIR)/Jar.mk

