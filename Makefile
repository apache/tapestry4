# Master Makefile for the source tree
# $Id$

distribution: clean copy-external-jars install javadoc
	@$(RECURSE) clean create-archives

# The job of this Makefile is to re-invoke make in various subdirectories.

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

# A list of Jar files redistributed with Tapestry.  xerces, gnu-regexp
# ejb and log4j are needed to build Tapestry; the rest are needed
# for the tutorials and demos.  Note that org.apache.jasper.jar is
# ony needed to run the JSP portion of the Primix Virtual Library
# demo.

EXTERNAL_JARS := \
	xerces.jar \
	gnu-regexp.jar \
	com.mortbay.jetty.jar \
	javax.servlet.jar \
	org.apache.jasper.jar \
	ejb.jar \
	log4j.jar

# This reflects my personal build work area structure.
# C:/Work/Tapestry is my work area and I create new releases
# by checking out the Tapestry module to
# C:/Work/Export/Tapestry-x-x-x

EXTERNAL_LIB_DIR = ../../Tapestry/lib

LOCAL_LIB_DIR = lib

copy-external-jars:
	$(call NOTE, Copying external JARs ...)
	@$(MKDIRS) $(LOCAL_LIB_DIR)
	$(CP) $(foreach _jar,$(EXTERNAL_JARS),$(EXTERNAL_LIB_DIR)/$(_jar)) $(LOCAL_LIB_DIR)

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
	$(RELEASE_DIR)/lib/Tapestry.jar \
	$(RELEASE_DIR)/lib/gnu-regexp.jar

# The medium release adds the JBE, JBE documentation
# and the Tapestry source code.
	
MEDIUM_RELEASE := \
	$(SMALL_RELEASE) \
	$(RELEASE_DIR)/JBE \
	$(RELEASE_DIR)/doc/JBE \
	$(RELEASE_DIR)/src

# The full release adds all the documentation and documentation source,
# plus all the examples, and bundles xerces, Jetty and etc.

FULL_RELEASE = \
	$(RELEASE_DIR)/ChangeLog \
	$(RELEASE_DIR)/LICENSE* \
	$(RELEASE_DIR)/images \
	$(RELEASE_DIR)/Readme.html \
	$(RELEASE_DIR)/lib \
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


.PHONY: javadoc create-archives reinvoke
.PHONY: clean install copy-external-jars distribution