# $Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2000, 2001 by Howard Ship and Primix
#
# Primix
# 311 Arsenal Street
# Watertown, MA 02472
# http://www.primix.com
# mailto:hship@primix.com
# 
# This library is free software.
# 
# You may redistribute it and/or modify it under the terms of the GNU
# Lesser General Public License as published by the Free Software Foundation.
#
# Version 2.1 of the license should be included with this distribution in
# the file LICENSE, as well as License.html. If the license is not
# included with this distribution, you may find a copy at the FSF web
# site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
# Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.

 
COMMA := ,
EMPTY :=
SPACE := $(EMPTY) $(EMPTY)
SLASH := $(EMPTY)/$(EMPTY)
BACKSLASH := $(EMPTY)\$(EMPTY)
SEMI := ;
PERIOD := .
DOT := .
DOTDOT := $(PERIOD)$(PERIOD)
COLON := :

SYS_BUILD_DIR_NAME := .build

CONFIG_DIR := $(SYS_MAKEFILE_DIR)/config

# A file which contains the names of all Java files (relative to the
# module directory).  Built by the refresh-catalog rule.
 
MOD_JAVA_CATALOG = $(MOD_BUILD_DIR)/java-catalog

# A file which contains the names of all resource files (relative
# to the module directory).  Built by the refresh-catalog rule.

MOD_RESOURCE_CATALOG = $(MOD_BUILD_DIR)/resource-catalog

# Create a macro for recusively invoking Make.
# We don't want or need any builtin rules.

RECURSE := $(MAKE) --no-builtin-rules --no-print-directory

# Definitions needed by DocBook.mk

DOCBOOK_DIR := $(SYS_MAKEFILE_DIR)/docbook

DOCBOOK_OPENJADE_DIR  := $(DOCBOOK_DIR)/openjade
DOCBOOK_DSSSL_DIR := $(DOCBOOK_DIR)/dsssl/docbook
DOCBOOK_DTD_DIR := $(DOCBOOK_DIR)/dtd

# Find out what the platform is.  Must set a value for SITE_PLATFORM.
# The LocalConfig can set other things (such as locations of tools,
# libraries, etc.)  It is optional.

include $(CONFIG_DIR)/SiteConfig.mk
-include $(CONFIG_DIR)/LocalConfig.mk

# Find out information specific to our platform.  SITE_PLATFORM was
# just set by CONFIG_DIR/SiteConfig.mk

include $(SYS_MAKEFILE_DIR)/Platform.$(SITE_PLATFORM).mk

# Determine the JDK Vendor, which may be specified in the project
# Makefile as JDK_VENDOR, in the module Makefile as MOD_JDK_VENDOR,
# in config/SiteConfig.mk as SITE_JDK_VENDOR or simply default to Sun

FINAL_JDK_VENDOR := $(firstword $(JDK_VENDOR) $(MOD_JDK_VENDOR) $(SITE_JDK_VENDOR) Sun)

# The release is the most likely thing to override.
# In the project Makefile as JDK_RELEASE
# In the module as  MOD_JDK_RELEASE
# As a site default as SITE_JDK_RELEASE in config/SiteConfig.mk
# Or the default is 1.3

FINAL_JDK_RELEASE := $(firstword $(JDK_RELEASE) $(MOD_JDK_RELEASE) \
	$(SITE_JDK_RELEASE) 1.3)

# Now calculate the JDK_DIR

JDK_DIR_VAR := JDK_$(FINAL_JDK_VENDOR)_$(FINAL_JDK_RELEASE)_DIR
JDK_DIR := $($(JDK_DIR_VAR))

# Optionally include it ... if it doesn't exist, we'll catch the error
# in the check-jdk rule defined in CommonRules.mk

SYS_JDK_CONFIG_FILE := $(SYS_MAKEFILE_DIR)/JDK.$(FINAL_JDK_VENDOR)_$(SITE_PLATFORM).mk

# A dummy rule for targets (such as 'install') that don't do anything directly.

DUMMY_RULE = @$(TOUCH) $(MOD_BUILD_DIR)/dummy

-include $(SYS_JDK_CONFIG_FILE)

