# $Id$

JAVA_FILES = *.java

# For each component, ship the corresponding Java file as well 
# (to support the Inspector).

RESOURCE_FILES = *.jwc *.html \
	$(patsubst %.jwc,%.java,$(wildcard *.jwc))

include $(SYS_MAKEFILE_DIR)/Package.mk
