# Master Makefile for the source tree
# $Id$

distribution: clean copy-external-jars install javadoc
	@$(RECURSE) clean prepare-for-packaging create-archives

# The job of this Makefile is to re-invoke make in various subdirectories.

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk

# The names of the different modules to invoke make in. Order is important, frameworks
# should come first.

MODULES = \
	../Tapestry \
	../Examples/Tutorial \
	../Examples/VlibBeans \
	../Examples/Vlib

JAVADOC_MODULES = \
	../Tapestry \
	../Examples/VlibBeans \
	../Examples/Vlib

# A list of Jar files redistributed with Tapestry.  xerces, gnu-regexp
# ejb and log4j are needed to build Tapestry; the rest are needed
# for the tutorials and demos.  Note that org.apache.jasper.jar is
# ony needed to run the JSP portion of the Primix Virtual Library
# demo.

EXTERNAL_JARS = \
	xerces.jar \
	gnu-regexp.jar \
	com.mortbay.jetty.jar \
	javax.servlet.jar \
	org.apache.jasper.jar \
	ejb.jar \
	log4j.jar

# This reflects my personal build work area structure, where
# I create a subdirectory and checkout all the files.

EXTERNAL_LIB_DIR = ../../../lib

LOCAL_LIB_DIR = ../lib

copy-external-jars:
	@$(ECHO) "\n*** Copying external JARs ... ***\n"
	@$(MKDIRS) $(LOCAL_LIB_DIR)
	@$(CP) $(foreach _jar,$(EXTERNAL_JARS),$(EXTERNAL_LIB_DIR)/$(_jar)) $(LOCAL_LIB_DIR)

# REINVOKE needs to specify a TARGET (used by the reinvoke
# rule).

REINVOKE := \
	@$(RECURSE) reinvoke
	
prepare-for-packaging:
	@$(ECHO) "\n*** Copying licenses and Readme to root ...***\n"
	$(TAR) --create LICENSE* *.html ChangeLog images | \
		($(CD) .. && $(TAR) --extract)

install: 
	$(REINVOKE) TARGET=install JAVAC_OPT=-g

clean: 
	$(REINVOKE) TARGET=clean

reinvoke: 
	@for module in $(MODULES) ; do \
		$(ECHO) "\n*** Making target $(TARGET) in $$module ... ***\n" ; \
       $(RECURSE) -C $$module $(TARGET) || exit 2 ; \
	done

javadoc:
	@$(ECHO) "\n*** Rebuilding Javadoc ... ***\n"
	@for module in $(JAVADOC_MODULES) ; do \
		$(ECHO) "\n*** Making javadoc in $$module ... ***\n" ; \
       $(RECURSE) -C $$module javadoc || exit 2 ; \
	done

# Get the last piece of the current directory, which will be
# something like Tapestry-x.y.z

RELEASE_DIR := $(notdir $(shell $(CD) .. && $(PWD)))

# The small release contains the precompiled JAR and javadoc, but
# virtually nothing else.

SMALL_RELEASE = \
	$(RELEASE_DIR)/ChangeLog \
	$(RELEASE_DIR)/LICENSE.html \
	$(RELEASE_DIR)/Readme.html \
	$(RELEASE_DIR)/images \
	$(RELEASE_DIR)/doc/Tapestry.pdf \
	$(RELEASE_DIR)/doc/api \
	$(RELEASE_DIR)/lib/Tapestry.jar \
	$(RELEASE_DIR)/lib/gnu-regexp.jar

# The medium release adds the JBE and the Tapestry source code.
	
MEDIUM_RELEASE = \
	$(SMALL_RELEASE) \
	$(RELEASE_DIR)/JBE \
	$(RELEASE_DIR)/doc/JBE.pdf \
	$(RELEASE_DIR)/Tapestry

# The full release adds all the documentation (in PDF format),
# plus all the Examples, and bundles xerces, Jetty and etc.

FULL_RELEASE = \
	$(RELEASE_DIR)/ChangeLog \
	$(RELEASE_DIR)/LICENSE* \
	$(RELEASE_DIR)/images \
	$(RELEASE_DIR)/Readme.html \
	$(RELEASE_DIR)/lib \
	$(RELEASE_DIR)/doc/*.pdf \
	$(RELEASE_DIR)/doc/api \
	$(RELEASE_DIR)/Tapestry \
	$(RELEASE_DIR)/JBE \
	$(RELEASE_DIR)/Examples

create-archives: prepare-for-packaging
	@$(ECHO) "\n*** Creating full distribution archive ... ***\n"
	@$(RM) -f ..\$(RELEASE_DIR)-*.gz 
	$(CD) ../.. ; $(TAR) czf $(RELEASE_DIR)-full.tar.gz $(FULL_RELEASE)
	@$(ECHO) "\n*** Creating small distribution archive ... ***\n"
	$(CD) ../.. ; $(TAR) czf $(RELEASE_DIR)-small.tar.gz $(SMALL_RELEASE)
	@$(ECHO) "\n*** Creating medium distribution archive ... ***\n"
	$(CD) ../.. ; $(TAR) czf $(RELEASE_DIR)-medium.tar.gz $(MEDIUM_RELEASE)


.PHONY: javadoc create-archives prepare-for-packaging reinvoke
.PHONY: clean install copy-external-jars distribution