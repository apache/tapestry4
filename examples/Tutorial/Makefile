# $Id$

PACKAGES = \
	tutorial.hello \
	tutorial.simple \
	tutorial.adder \
	tutorial.border \
	tutorial.survey

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/j2ee.jar

INSTALL_DIR = ../../lib

MODULE_NAME = Tutorial

CONTEXT_RESOURCES = index.html

include $(SYS_MAKEFILE_DIR)/War.mk

EXEC_CLASSPATH = \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/com.mortbay.jetty.jar \
	$(INSTALL_DIR)/javax.servlet.jar


_EXEC_CLASSPATH := $(strip $(shell $(JBE_CANONICALIZE) -classpath $(EXEC_CLASSPATH)))

run:
	$(JAVA) -classpath "$(_EXEC_CLASSPATH)" \
	-Dorg.xml.sax.parser=org.apache.xerces.parsers.SAXParser \
	com.mortbay.Jetty.Server jetty.xml


