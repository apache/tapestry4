# $Id$

JAVA_FILES = *.java

PACKAGES = com.primix.vlib.ejb

MODULE_NAME = VlibBeans

EJBC_OPT = -keepgenerated

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/j2ee.jar \
	$(INSTALL_DIR)/PrimixFoundation.jar

INSTALL_DIR = ../../lib

META_RESOURCES = *.xml

JAVADOC_DIR = ../../javadoc

JAVADOC_OPT = \
	-version -author -windowtitle "Primix Virtual Library - Enterprise JavaBeans" \
	-link http://java.sun.com/products/jdk/1.2/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

include $(SYS_MAKEFILE_DIR)/WebLogic.mk





