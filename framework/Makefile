# $Id$
#

MODULE_NAME = Tapestry

PACKAGES = \
	com.primix.tapestry \
	com.primix.tapestry.app \
	com.primix.tapestry.asset \
	com.primix.tapestry.binding \
	com.primix.tapestry.components \
	com.primix.tapestry.event \
	com.primix.tapestry.pageload \
	com.primix.tapestry.pages \
	com.primix.tapestry.parse \
	com.primix.tapestry.record \
	com.primix.tapestry.spec

RESOURCE_PACKAGES = \
	com.primix.tapestry.components \
	com.primix.tapestry.pages

LOCAL_CLASSPATH = \
	../lib/gnu-regexp.jar \
	../lib/jaxp.jar \
	../lib/parser.jar \
	../lib/j2ee.jar \
	../lib/PrimixFoundation.jar

INSTALL_DIR := ../lib

JAVADOC_DIR = ../javadoc

JAVADOC_OPT = \
	-version -author -windowtitle "Tapestry API" \
	-link http://java.sun.com/products/jdk/1.2/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api \
	-link ../javadoc/PrimixFoundation


include $(SYS_MAKEFILE_DIR)/Jar.mk

