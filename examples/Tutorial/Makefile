# $Id$

PACKAGES = \
	tutorial.hello \
	tutorial.simple \
	tutorial.adder \
	tutorial.border \
	tutorial.survey \
	tutorial.hangman

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/javax.servlet.jar

INSTALL_DIR = ../../lib

MODULE_NAME = Tutorial

CONTEXT_RESOURCES = index.html

include $(SYS_MAKEFILE_DIR)/War.mk

EXEC_CLASSPATH = \
	$(LOCAL_CLASSPATH) \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/com.mortbay.jetty.jar \
	$(INSTALL_DIR)/j2ee.jar

run: setup-jbe-util
	@$(ECHO) "\n*** Running Jetty for Tutorial ... ***\n"
	$(call EXEC_JAVA,$(EXEC_CLASSPATH), \
		-Dorg.xml.sax.parser=org.apache.xerces.parsers.SAXParser \
		com.mortbay.Jetty.Server jetty.xml)



