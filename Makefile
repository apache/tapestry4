# Master Makefile for the source tree
# $Id$

# Important make targets
#
#  dist: Builds (from scratch) the entire Tapestry distibution and packages
#    it as three tar balls
#
#  run-tutorial:  Starts up Jetty to serve up the Tapestry tutorial
#

default:
	$(error You must specify a make target (run-tutorial or dist))


include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk

# The names of the different modules to invoke make in. Order is important, frameworks
# should come first.

MODULES := \
	src \
	examples/Tutorial \
	examples/VlibBeans \
	examples/Vlib \
	doc/src/JBE \
	doc/src/DevelopersGuide \
	doc/src/Tutorial

JAVADOC_MODULES := \
	src \
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

# Get the last piece of the current directory, which will be
# something like Tapestry-x.y.z

RELEASE_DIR := $(notdir $(shell $(CD) .. && $(PWD)))

# The small release contains the precompiled JAR and javadoc, but
# virtually nothing else.  We include gnu-regexp because its very
# hard to find and tiny to boot.

SMALL_RELEASE := \
	$(RELEASE_DIR)/ChangeLog \
	$(RELEASE_DIR)/LICENSE.html \
	$(RELEASE_DIR)/Readme.html \
	$(RELEASE_DIR)/images \
	$(RELEASE_DIR)/doc/DevelopersGuide \
	$(RELEASE_DIR)/doc/api \
	$(RELEASE_DIR)/lib/com.primix.tapestry.jar \
	$(RELEASE_DIR)/lib/gnu-regexp.jar

# The medium release adds the JBE, JBE documentation
# and the Tapestry source code.
	
MEDIUM_RELEASE := \
	$(SMALL_RELEASE) \
	$(RELEASE_DIR)/JBE \
	$(RELEASE_DIR)/doc/JBE \
	$(RELEASE_DIR)/src

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
	$(RELEASE_DIR)/src \
	$(RELEASE_DIR)/JBE \
	$(RELEASE_DIR)/examples
	
create-archives:
	$(call NOTE, Creating full distribution archive ...)
	@$(RM) -f ..\$(RELEASE_DIR)-*.gz 
	@$(CD) ../.. ; $(GNUTAR) czf $(RELEASE_DIR)-full.tar.gz $(FULL_RELEASE)
	$(call NOTE, Creating small distribution archive ...)
	@$(CD) ../.. ; $(GNUTAR) czf $(RELEASE_DIR)-small.tar.gz $(SMALL_RELEASE)
	$(call NOTE, Creating medium distribution archive ...)
	@$(CD) ../.. ; $(GNUTAR) czf $(RELEASE_DIR)-medium.tar.gz $(MEDIUM_RELEASE)


dist: clean install javadoc
	$(call NOTE, Building Tapestry distributions ...)
	@$(RECURSE) clean create-archives

		
TUTORIAL_CLASSPATH := \
	$(LIB_DIR)/com.primix.tapestry.jar \
	$(LIB_DIR)/javax.servlet.jar \
	$(LIB_DIR)/log4j.jar \
	$(LIB_DIR)/javax.xml.jaxp.jar \
	$(LIB_DIR)/org.apache.crimson.jar \
	$(LIB_DIR)/gnu-regexp.jar \
	$(LIB_DIR)/com.mortbay.jetty.jar \
	$(LIB_DIR)/ejb.jar


# Quick start for folks who download the full distribution (this Makefile
# is only included in the full distro).  Allows users to easily and quickly
# run the tutorial.

run-tutorial: setup-jbe-util
	$(call NOTE, Running the Tapestry Tutorial on port 8080 ...)
	$(call EXEC_JAVA, $(TUTORIAL_CLASSPATH), \
		-showversion \
		-Dorg.xml.sax.parser=org.apache.xerces.parsers.SAXParser \
		com.mortbay.Jetty.Server config/jetty-tutorial.xml)

.PHONY: javadoc create-archives reinvoke run-tutorial
.PHONY: clean install dist default