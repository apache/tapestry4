# Master Makefile for the source tree
# $Id$

# Important make targets
#
#  dist: Builds (from scratch) the entire Tapestry distibution and packages
#    it as three tar balls
#
#  run-tutorial:  Starts up Jetty to serve up the Tapestry tutorial
#

# Preparing for a distribution:
# 1) Get an export of the Tapestry module
# 2) Store the files in a directory name Tapestry-x.x.x where x.x.x is the release number
# 3) Change into the new directory and make dist
#
# Example:
#
#  cvs -z3 -d:pserver:anonymous@cvs.tapestry.sf.net:/cvsroot/tapestry export -r HEAD -d Tapestry-x.x.x Tapestry
#  cd Tapestry-x.x.x
#  make dist


default:
	$(error You must specify a make target (run-tutorial or dist))


include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk

# The names of the different modules to invoke make in, order is important.

MODULES := \
	framework \
	contrib \
	junit \
	examples/Tutorial \
	examples/VlibBeans \
	examples/Vlib \
	doc/src/JBE \
	doc/src/DevelopersGuide \
	doc/src/Tutorial

JAVADOC_MODULES := \
	framework \
	examples/VlibBeans \
	examples/Vlib

LIB_DIR := lib

# REINVOKE needs to specify a TARGET (used by the reinvoke
# rule).

REINVOKE := \
	@$(RECURSE) reinvoke
	
install: 
	$(REINVOKE) TARGET=install

clean: 
	$(REINVOKE) TARGET=clean

reinvoke: 
	@for module in $(MODULES) ; do \
		$(ECHO) "\n*** Making target $(TARGET) in $$module ... ***\n" ; \
       $(RECURSE) -C $$module $(TARGET) JAVAC_OPT=-g || exit 2 ; \
	done

javadoc:
	$(call NOTE, Rebuilding Javadoc ...)
	@for module in $(JAVADOC_MODULES) ; do \
		$(ECHO) "\n*** Making javadoc in $$module ... ***\n" ; \
       $(RECURSE) -C $$module javadoc || exit 2 ; \
	done

RELEASE_DIR := $(notdir $(shell $(PWD)))

# The small release contains the precompiled JAR and javadoc, but
# virtually nothing else. 

SMALL_RELEASE := \
	$(RELEASE_DIR)/ChangeLog \
	$(RELEASE_DIR)/LICENSE.html \
	$(RELEASE_DIR)/Readme.html \
	$(RELEASE_DIR)/images \
	$(RELEASE_DIR)/doc/DevelopersGuide \
	$(RELEASE_DIR)/doc/api \
	$(RELEASE_DIR)/lib/com.primix.tapestry.jar

# The medium release adds the JBE, JBE documentation
# and the Tapestry source code.
	
MEDIUM_RELEASE := \
	$(SMALL_RELEASE) \
	$(RELEASE_DIR)/JBE \
	$(RELEASE_DIR)/doc/JBE \
	$(RELEASE_DIR)/framework

# The full release adds all the documentation and documentation source,
# plus all the examples, and bundles JAXP, Crimson, Jetty and etc.

FULL_RELEASE = \
	$(RELEASE_DIR)/ChangeLog \
	$(RELEASE_DIR)/LICENSE* \
	$(RELEASE_DIR)/images \
	$(RELEASE_DIR)/Readme.html \
	$(RELEASE_DIR)/lib \
	$(RELEASE_DIR)/Makefile \
	$(RELEASE_DIR)/config \
	$(RELEASE_DIR)/doc/*.pdf \
	$(RELEASE_DIR)/doc/DevelopersGuide \
	$(RELEASE_DIR)/doc/JBE \
	$(RELEASE_DIR)/doc/Tutorial \
	$(RELEASE_DIR)/doc/api \
	$(RELEASE_DIR)/doc/src \
	$(RELEASE_DIR)/framework \
	$(RELEASE_DIR)/JBE \
	$(RELEASE_DIR)/examples \
	$(RELEASE_DIR)/junit \
	$(RELEASE_DIR)/web
	
create-archives:
	$(call NOTE, Creating full distribution archive ...)
	@$(RM) -f ../$(RELEASE_DIR)-*.gz 
	@$(CD) .. ; $(GNUTAR) czf $(RELEASE_DIR)-full.tar.gz $(FULL_RELEASE)
	$(call NOTE, Creating small distribution archive ...)
	@$(CD) .. ; $(GNUTAR) czf $(RELEASE_DIR)-small.tar.gz $(SMALL_RELEASE)
	$(call NOTE, Creating medium distribution archive ...)
	@$(CD) .. ; $(GNUTAR) czf $(RELEASE_DIR)-medium.tar.gz $(MEDIUM_RELEASE)


dist: clean install javadoc
	$(call NOTE, Building Tapestry distributions ...)
	@$(RECURSE) clean create-archives

		
TUTORIAL_CLASSPATH := \
	$(LIB_DIR)/com.primix.tapestry.jar \
	$(LIB_DIR)/net.sf.tapestry.contrib.jar \
	$(LIB_DIR)/javax.servlet.jar \
	$(LIB_DIR)/log4j-core.jar \
	$(LIB_DIR)/javax.xml.jaxp.jar \
	$(LIB_DIR)/org.apache.crimson.jar \
	$(LIB_DIR)/com.mortbay.jetty.jar \
	$(LIB_DIR)/ejb.jar

# Quick start for folks who download the full distribution (this Makefile
# is only included in the full distro).  Allows users to easily and quickly
# run the tutorial.

run-tutorial: setup-jbe-util
	$(call NOTE, Running the Tapestry Tutorial on port 8080 ...)
	$(call EXEC_JAVA, $(TUTORIAL_CLASSPATH), \
		-showversion \
		com.mortbay.Jetty.Server config/jetty-tutorial.xml)

VLIB_CLASSPATH := \
	$(TUTORIAL_CLASSPATH) \
	lib/VlibBeans.jar \
	examples/VlibBeans/jboss \
	$(JBOSS_DIR)/client/jboss-client.jar \
	$(JBOSS_DIR)/client/jbosssx-client.jar \
	$(JBOSS_DIR)/client/jnp-client.jar
	
run-intranet: setup-jbe-util
	$(call NOTE, Running Intranet on port 80 ...)
	@$(RM) .build
	@$(MKDIRS) .build ../TapestryLogs
	$(call EXEC_JAVA, $(VLIB_CLASSPATH), \
	  -showversion \
	  com.mortbay.Jetty.Server config/jetty-intranet.xml)
	  
run-extranet: setup-jbe-util
	$(call NOTE, Running Extranet on port 80 ...)
	@$(RM) .build
	@$(MKDIRS) .build ../TapestryLogs
	$(call EXEC_JAVA, $(VLIB_CLASSPATH), \
	  -showversion \
	  com.mortbay.Jetty.Server config/jetty-extranet.xml)
	  
.PHONY: javadoc create-archives reinvoke run-tutorial
.PHONY: clean install dist default
