# $Id$

PROJECT_NAME = wap

PACKAGES = \
	wap.hello 

PROJ_CLASSPATH = \
	$(INSTALL_DIR)/com.primix.tapestry.jar \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/log4j-core.jar \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar
	
INSTALL_DIR = ../../lib

CONTEXT_RESOURCES = index.html

include $(SYS_MAKEFILE_DIR)/War.mk

# Include FINAL_SOURCE_DIR first on the classpath, 
# so that resources in the working directory
# take precedence over resources packaged into the .WAR

EXEC_CLASSPATH = \
	$(FINAL_SOURCE_DIR) \
	$(PROJ_CLASSPATH) \
	$(INSTALL_DIR)/org.mortbay.jetty.jar 

run: war
	$(call NOTE, Running Jetty for wap ...)
	$(call EXEC_JAVA,$(EXEC_CLASSPATH), \
		-showversion \
		org.mortbay.jetty.Server jetty.xml)

