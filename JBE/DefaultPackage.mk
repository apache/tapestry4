# $Id$

# Default package makefile used when a package doesn't have its own
# Makefile.

JAVA_FILES = *.java

RESOURCE_FILES = *.jwc *.application *.html *.properties

include $(SYS_MAKEFILE_DIR)/Package.mk
