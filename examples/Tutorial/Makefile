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
	$(INSTALL_DIR)/com.primix.tapestry.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/log4j.jar
	
INSTALL_DIR = ../../lib

CONTEXT_RESOURCES = index.html images

include $(SYS_MAKEFILE_DIR)/War.mk

EXEC_CLASSPATH = \
	$(PROJ_CLASSPATH) \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/com.mortbay.jetty.jar \
	$(INSTALL_DIR)/ejb.jar

run: war
	$(call NOTE, Running Jetty for Tutorial ...)
	$(call EXEC_JAVA,$(EXEC_CLASSPATH), \
		-showversion \
		com.mortbay.Jetty.Server jetty.xml)
