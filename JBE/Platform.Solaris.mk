#$Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2001 by Howard Ship and Primix Solutions
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

# Defines commands and options used on Solaris.
# The Solaris install requires GNU tar and the gzip utilities
# be installed as well.

# Here we define a bunch of command line tools used in the rest of the system.

TOOLS_DIR := /usr/bin

MV := $(TOOLS_DIR)/mv
ECHO := $(TOOLS_DIR)/echo
MKDIRS := $(TOOLS_DIR)/mkdir -p
TOUCH := $(TOOLS_DIR)/touch
RM := $(TOOLS_DIR)/rm -fR

CP := $(TOOLS_DIR)/cp
CP_FORCE_OPT := -f

CAT := $(TOOLS_DIR)/cat
FIND := $(TOOLS_DIR)/find

# Expect that cd from /bin/sh will always work (troublesome)

CD := cd

GNUTAR := /usr/local/bin/gtar
GNUTAR_CREATE_OPT := --create
GNUTAR_EXTRACT_OPT := --extract
GNUTAR_GZIP_OPT := --gzip

PWD := $(TOOLS_DIR)/pwd

SED := $(TOOLS_DIR)/sed
SED_QUIET_OPT := --quiet
SED_EXPRESSION_OPT := -e 

CHMOD := $(TOOLS_DIR)/chmod

