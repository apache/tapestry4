# $Id$

PROJECT_NAME = com.primix.tapestry

PACKAGES = \
	com.primix.tapestry \
	com.primix.tapestry.asset \
	com.primix.tapestry.bean \
	com.primix.tapestry.binding \
	com.primix.tapestry.components \
	com.primix.tapestry.html \
	com.primix.tapestry.link \
	com.primix.tapestry.form \
	com.primix.tapestry.valid \
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
	com.primix.tapestry.callback \
	com.primix.tapestry.util \
	com.primix.tapestry.util.exception \
	com.primix.tapestry.util.io \
	com.primix.tapestry.util.prop \
	com.primix.tapestry.util.jdbc \
	com.primix.tapestry.util.ejb \
	com.primix.tapestry.util.pool \
	com.primix.tapestry.util.xml \
	com.primix.tapestry.listener

RESOURCE_EXTENSIONS = \
	jwc html gif dtd script properties css

PROJ_CLASSPATH = \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar \
	$(INSTALL_DIR)/ejb.jar \
	$(INSTALL_DIR)/log4j-core.jar

INSTALL_DIR = ../lib

JAVADOC_DIR = ../doc/api

JAVADOC_OPT = \
	-version -author -splitindex -windowtitle "Tapestry API 1.0.5" \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api \
	-link http://jakarta.apache.org/log4j/docs/api \
	-link http://java.sun.com/xml/jaxp-docs-1.1/docs/api

include $(SYS_MAKEFILE_DIR)/Jar.mk


# This is a convienince for me (hship) to install latest copies of the DTDs.

install-dtds:
	/usr/bin/scp `$(FIND) $(FINAL_SOURCE_DIR) -name \*.dtd -print` \
	hship@shell.sf.net:/home/groups/t/ta/tapestry/htdocs/dtd
	
