# $Id$

PROJECT_NAME = Tutorial

PACKAGES = \
	tutorial.hello \
	tutorial.simple \
	tutorial.adder \
	tutorial.border \
	tutorial.survey \
	tutorial.hangman \
	tutorial.locale

PROJ_CLASSPATH = \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/log4j.jar

INSTALL_DIR = ../../lib

CONTEXT_RESOURCES = index.html images

include $(SYS_MAKEFILE_DIR)/War.mk

EXEC_CLASSPATH = \
	$(PROJ_CLASSPATH) \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/com.mortbay.jetty.jar \
	$(INSTALL_DIR)/ejb.jar

run: war
	$(call NOTE, Running Jetty for Tutorial ...)
	$(call EXEC_JAVA,$(EXEC_CLASSPATH), \
		-showversion \
		-Dorg.xml.sax.parser=org.apache.xerces.parsers.SAXParser \
		com.mortbay.Jetty.Server jetty.xml)
