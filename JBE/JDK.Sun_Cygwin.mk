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

# Defines the Sun JDK environment for Cygwin (Win32)

# JDK_DIR is specified indirectly.  We determine the vendor, release and
# platform (see CommonDefs.mk), then look for a variable that defines
# the local directory for the vendor and release, that becomes JDK_DIR.

JDK_BIN_DIR := $(JDK_DIR)/bin

JAVAC := $(JDK_BIN_DIR)/javac
JAVA := $(JDK_BIN_DIR)/java
JAR := $(JDK_BIN_DIR)/jar
JAVADOC := $(JDK_BIN_DIR)/javadoc
RMIC := $(JDK_BIN_DIR)/rmic
JDB := $(JDK_BIN_DIR)/jdb

# NT uses a semi-colon to seperate items in the classpath (Unix
# uses a colon).

CLASSPATHSEP := $(SEMI)


