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

 
# Makes sure that the JBE utility classes are compiled and upto date.
# A stamp file is used to determine whether any of the Java sources have changed; 
# if so ALL of them are recompiled.

JBE_UTIL_STAMP = $(SYS_MAKEFILE_DIR)/com/primix/jbe/.build_stamp

check-jdk:
ifeq "$(JAVAC)" ""
	$(error JAVAC is not defined.  This is usually a configuration error in \
		your choice of JDK Vendor ('$(FINAL_JDK_VENDOR)') \
		or JDK Platform ('$(FINAL_JDK_PLATFORM)'), or you must provide \
		a JDK configuration file $(SYS_JDK_CONFIG_FILE))
endif
ifeq "$(JDK_DIR)" ""
	$(error The JDK_DIR is not defined.  You must provide a definition for \
		variable $(JDK_DIR_VAR) in config/LocalConfig.mk)
endif

setup-jbe-util: check-jdk $(JBE_UTIL_STAMP)

$(JBE_UTIL_STAMP): $(SYS_MAKEFILE_DIR)/com/primix/jbe/*.java
	@$(ECHO) "\n*** Compiling JBE Utility ... ***\n";
	@$(RM) -f $(SYS_MAKEFILE_DIR)/com/primix/jbe/*.class
	$(CD) $(SYS_MAKEFILE_DIR) ; \
	$(JAVAC) com/primix/jbe/*.java
	@$(TOUCH) $@

# Note, for this to work, SYS_MAKEFILE_DIR must use only forward slashes. Either
# GNU Make or JAVA is eating the backslashes under NT.

JBE_UTIL := $(JAVA) -classpath $(SYS_MAKEFILE_DIR) com.primix.jbe.Util 

# Command for accessing the JBE utility.
# Usage:
#	$(call JBE_CANONICALIZE,options)
#
# Typically, something like
# $(call JBE_CANONICALIZE,-classpath $(CLASSPATH)) 
#
# Runs the JBE_UTIL in a shell and captures the output.

JBE_CANONICALIZE = $(shell $(JBE_UTIL) canonicalize $(strip $(1)))

# Splice command
# Usage:
#   $(call JBE_SPLICE,tag,target file,source file[,other options])

JBE_SPLICE = $(JBE_UTIL) splice -tag $(1) -target $(2) -source $(3) $(4)
 
# Command for running a Java command
# Usage
#	$(call EXEC_JAVA,classpath,options)
#
# classpath is a space seperated list of stuff for the classpath.
# options is passed as is to $(JAVAC), it should include JVM options,
# the main class and and program options.

EXEC_JAVA = $(JAVA) -classpath "$(call JBE_CANONICALIZE,-classpath $(1))" $(2)

.PHONY: setup-jbe-util check-jdk