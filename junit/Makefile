# $Id$

PACKAGES := net.sf.tapestry.junit.parse

PROJECT_NAME := net.sf.tapestry.junit

INSTALL_DIR := ../lib

PROJ_CLASSPATH := \
	../framework/.build/classes \
	$(INSTALL_DIR)/javax.servlet.jar \
	$(INSTALL_DIR)/javax.xml.jaxp.jar \
	$(INSTALL_DIR)/org.apache.crimson.jar \
	$(INSTALL_DIR)/log4j-core.jar \
	$(INSTALL_DIR)/junit.jar \
	$(INSTALL_DIR)/com.primix.tapestry.jar
	
include $(SYS_MAKEFILE_DIR)/Jar.mk

junit: jar
	$(call NOTE, Running JUnit ...)
	$(call EXEC_JAVA, $(JAR_FILE) $(PROJ_CLASSPATH), \
		junit.swingui.TestRunner)


