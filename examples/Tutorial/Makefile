# $Id$

PROJECT_NAME = Tutorial

PACKAGES = \
	tutorial.hello \
	tutorial.simple \
	tutorial.adder \
	tutorial.border \
	tutorial.survey \
	tutorial.hangman \
	tutorial.locale \
	tutorial.valid \
	tutorial.portal

PROJ_CLASSPATH = \
	$(INSTALL_DIR)/com.primix.tapestry.jar \
	$(INSTALL_DIR)/net.sf.tapestry.contrib.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/log4j-core.jar \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar
	
INSTALL_DIR = ../../lib

CONTEXT_RESOURCES = *.html images

RESOURCE_EXTENSIONS = application properties html jwc script gif

include $(SYS_MAKEFILE_DIR)/War.mk

# Include FINAL_SOURCE_DIR first on the classpath, 
# so that resources in the working directory
# take precedence over resources packaged into the .WAR

EXEC_CLASSPATH = \
	$(FINAL_SOURCE_DIR) \
	$(PROJ_CLASSPATH) \
	$(INSTALL_DIR)/org.mortbay.jetty.jar \
	$(INSTALL_DIR)/ejb.jar

run: war
	$(call NOTE, Running Jetty for Tutorial ...)
	$(call EXEC_JAVA,$(EXEC_CLASSPATH), \
		-showversion \
		$(OTHER_TUTORIAL_JAVA_OPT) \
		org.mortbay.jetty.Server jetty.xml)

