# Master Makefile for the source tree
# $Id$

distribution: clean copy-external-jars install javadoc
	@$(RECURSE) clean prepare-for-packaging create-archive

# The job of this Makefile is to re-invoke make in various subdirectories.

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk

# The names of the different modules to invoke make in. Order is important, frameworks
# should come first.

MODULES = \
	Tapestry \
	Examples/Tutorial \
	Examples/VlibBeans \
	Examples/Vlib

JAVADOC_MODULES = \
	Tapestry \
	Examples/VlibBeans

# A list of Jar files redistributed with Tapestry.  xerces, gnu-regexp
# and j2ee are needed to build Tapestry; the rest are needed
# for the tutorials and demos.

EXTERNAL_JARS = \
	xerces.jar \
	gnu-regexp.jar \
	com.mortbay.jetty.jar \
	javax.servlet.jar \
	org.apache.jasper.jar \
	j2ee.jar

# This reflects my personal build work area structure, where
# I create a subdirectory and checkout all the files.

EXTERNAL_LIB_DIR = ../../lib

LOCAL_LIB_DIR = lib

copy-external-jars:
	@$(ECHO) "\n*** Copying external JARs ... ***\n"
	@$(MKDIRS) $(LOCAL_LIB_DIR)
	@$(CP) $(foreach _jar,$(EXTERNAL_JARS),$(EXTERNAL_LIB_DIR)/$(_jar)) $(LOCAL_LIB_DIR)

# REINVOKE needs to specify a TARGET (used by the reinvoke
# rule).

REINVOKE := \
	@$(RECURSE) reinvoke
	

prepare-for-packaging:
	@$(ECHO) "\n*** Removing Word documents from distribution ... ***\n"
	@$(FIND) . \( -name \*.doc -o -name \*.vsd \) -print -exec $(RMDIRS) {} \;
	@$(ECHO) "\n*** Removing non-distributable JARs ... ***\n"
	@$(RM) $(LOCAL_LIB_DIR)/j2ee.jar

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
	@$(RMDIRS) javadoc
	@for module in $(JAVADOC_MODULES) ; do \
		$(ECHO) "\n*** Making javadoc in $$module ... ***\n" ; \
       $(RECURSE) -C $$module javadoc || exit 2 ; \
	done

# Get the last piece of the current directory, which will be
# something like Tapestry-x.y.z

RELEASE_DIR = $(notdir $(CURDIR))

ARCHIVE_NAME = $(RELEASE_DIR).tar.gz

# Create the archive in the directory above this one.

create-archive: prepare-for-packaging
	@$(ECHO) "\n*** Creating distribution archive $(ARCHIVE_NAME) ... ***\n"
	@$(RM) -f ..\$(ARCHIVE_NAME)
	$(CD) .. ; $(TAR) czf $(ARCHIVE_NAME) $(RELEASE_DIR)

.PHONY: javadoc create-archive prepare-for-packaging reinvoke
.PHONY: clean install copy-external-jars distribution