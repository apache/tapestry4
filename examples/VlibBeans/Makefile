# $Id$

PACKAGES := \
	com.primix.vlib.ejb \
	com.primix.vlib.ejb.impl

PROJECT_NAME := VlibBeans

INSTALL_DIR := ../../lib

PROJ_CLASSPATH = \
	$(INSTALL_DIR)/com.primix.tapestry.jar \
	$(JBOSS_DIR)/lib/ext/jms.jar \
	$(JBOSS_DIR)/lib/ext/mail.jar \
	$(INSTALL_DIR)/log4j-core.jar

META_RESOURCES := *.xml

JAVADOC_DIR := javadoc

JAVADOC_OPT := \
	-version -author \
	-windowtitle "Primix Virtual Library - Enterprise JavaBeans" \
	-link http://tapestry.primix.com/doc/api \
	-link http://java.sun.com/j2se/1.3/docs/api \
	-link http://java.sun.com/j2ee/j2sdkee/techdocs/api

JBOSS_JAVA_OPT := -Xms128m -Xmx128m

include $(SYS_MAKEFILE_DIR)/jBoss.mk

JBOSS_CONF_DIR = $(JBOSS_DIR)/conf/default

INSTALLED_VLIB_PROPERTIES = $(JBOSS_CONF_DIR)/vlib.properties

# At one time there were more libraries, but they are now already packaged in JBoss 2.2.1.

RUNTIME_LIBRARIES := \
	$(INSTALL_DIR)/com.primix.tapestry.jar
	
# Rule to configure the jBoss server for the VlibBeans
# demo.

configure-jboss:  setup-jbe-util
ifeq ("$(JBOSS_DIR)","")
	$(error You must set JBOSS_DIR (in $(SYS_MAKEFILE_DIR)/config/LocalConfig.mk) \
	before configuring jBoss)
endif
	@$(RECURSE) setup-db	
	$(call NOTE, Splicing changes to jboss.jcml ...)
	$(call JBE_SPLICE, \
		PrimixVlib, \
		$(JBOSS_CONF_DIR)/jboss.jcml, \
		jboss/jboss.jcml.splice, \
		-before "J2EE deployment")
	$(call NOTE, Splicing changes to jbossmq.xml ...)
	$(call JBE_SPLICE, PrimixVlib, \
		$(JBOSS_CONF_DIR)/jbossmq.xml, \
		jboss/jbossmq.xml.splice, \
		-before "Queue")
	@$(RM) $(JBOSS_CONF_DIR)/jboss-auto.jcml
	$(call NOTE, Setting up JNDI and Mail properties ...)
	$(CP) $(CP_FORCE_OPT) jboss/vlib-mail.properties jboss/jndi.properties $(JBOSS_CONF_DIR)
	$(call NOTE, Copying runtime libraries ...)
	$(CP) $(CP_FORCE_OPT) $(RUNTIME_LIBRARIES) $(JBOSS_DIR)/lib/ext
	$(call NOTE, jBoss is ready to run)

# Provide a do-nothing command for the setup-db rule (to prevent
# a "nothing to do" warning).

setup-db:  $(INSTALLED_VLIB_PROPERTIES)
	@$(TOUCH) $(SYS_BUILD_DIR)/dummy

# Only install the vlib database if the vlib.properties file doesn't exist.
# The database is in two parts:  the vlib.properties file, and a directory
# containg the InstantDB tables, indexes & etc.

$(INSTALLED_VLIB_PROPERTIES):
	$(call NOTE, Installing Primix Vlib database ...)
	$(CP) jboss/vlib.properties $(JBOSS_CONF_DIR)
	$(GNUTAR) $(GNUTAR_EXTRACT_OPT) $(GNUTAR_GZIP_OPT) \
	  -f jboss/vlib.tar.gz -C $(JBOSS_DIR)/db


# Command to run the InstantDB example command line tool.  This is used
# to make simple modifications to the Vlib database.

run-idb:
	$(call NOTE, Running InstantDB command line tool ...)
	@$(ECHO) "Use URL: jdbc:idb:$(INSTALLED_VLIB_PROPERTIES)\n"
	$(call EXEC_JAVA, \
		$(JBOSS_DIR)/lib/ext/idb.jar \
		$(JBOSS_DIR)/lib/ext/jta-spec1_0_1.jar \
		idbexmpl.jar, \
		org.enhydra.instantdb.commsql)
	
snapshot-database:
	$(GNUTAR) $(GNUTAR_CREATE_OPT) $(GNUTAR_GZIP_OPT) \
		-f jboss/vlib.tar.gz -C $(JBOSS_DIR)/db vlib

.PHONY: setup-db configure-jboss

# New columns
# Added in 1.0.5:
#
# alter table person add last_access timestamp default null
# alter table book add date_added timestamp default null

