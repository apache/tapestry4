# $Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2000 by Howard Ship and Primix Solutions
#
# Primix Solutions
# One Arsenal Marketplace
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

# A file which contains the complete class names of all RMI implementations,
# ready for RMIC

MOD_RMI_CLASS_CATALOG = $(MOD_BUILD_DIR)/rmi-class-catalog

# Create a macro for recusively invoking Make.  --unix seems
# to tame things under Windows.  We don't want or need any builtin
# rules.

RECURSE := $(MAKE) --unix --no-builtin-rules --no-print-directory

# Find out what out platform is.  Must set a value for SITE_PLATFORM.
# The LocalConfig can set other things (such as locations of tools,
# libraries, etc.)  It is optional.

include $(CONFIG_DIR)/SiteConfig.mk
-include $(CONFIG_DIR)/LocalConfig.mk

# Find out information specific to our platform.  SITE_PLATFORM was
# just set by CONFIG_DIR/SiteConfig.mk

include $(SYS_MAKEFILE_DIR)/Platform.$(SITE_PLATFORM).mk

# Find out the information specific to the JDK.   SITE_JDK was just
# set by CONFIG_DIR/SiteConfig.mk.

include $(SYS_MAKEFILE_DIR)/JDK.$(SITE_JDK).mk

-include $(CONFIG_DIR)/Common.mk

