# $Id$

MODULE_NAME = Tapestry

PACKAGES = \
	com.primix.foundation \
	com.primix.foundation.exception \
	com.primix.foundation.io \
	com.primix.foundation.prop \
	com.primix.foundation.jdbc \
	com.primix.foundation.ejb \
	com.primix.tapestry \
	com.primix.tapestry.app \
	com.primix.tapestry.asset \
	com.primix.tapestry.binding \
	com.primix.tapestry.components \
	com.primix.tapestry.components.validating \
	com.primix.tapestry.event \
	com.primix.tapestry.pageload \
	com.primix.tapestry.pages \
	com.primix.tapestry.parse \
	com.primix.tapestry.record \
	com.primix.tapestry.spec \
	com.primix.tapestry.inspector \
	com.primix.tapestry.script

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/j2ee.jar

INSTALL_DIR = ../lib

JAVADOC_DIR = ../javadoc

JAVADOC_OPT = \
	-version -author -windowtitle "Tapestry API" \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

include $(SYS_MAKEFILE_DIR)/Jar.mk

