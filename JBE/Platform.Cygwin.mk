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

# Defines stuff specific to using the JBE with Cygnus and NT.

# Here we define a bunch of command line tools used in the rest of the system.
# Cygwin creates a kind of "virtual mount" that redirects /usr/bin to the
# correct directory (generally, c:/cgywin/bin).

MV := /usr/bin/mv.exe

# Recent version of echo in cygwin require -e to interpret
# escape characters in the echo string.

ECHO := /usr/bin/echo.exe -e 
MKDIRS := /usr/bin/mkdir.exe --parents
TOUCH := /usr/bin/touch.exe
RM := /usr/bin/rm.exe --force --recursive

CP := /usr/bin/cp.exe
CP_FORCE_OPT := --force

CAT := /usr/bin/cat.exe
FIND := /usr/bin/find.exe

GNUTAR := /usr/bin/tar.exe
GNUTAR_CREATE_OPT := --create
GNUTAR_EXTRACT_OPT := --extract
GNUTAR_GZIP_OPT := --gzip

PWD := /usr/bin/pwd.exe

# SED can be used to process a template file
# and perform various edits and substitutions.
# This is very handy with WebLogic, since you
# can easily convert hard-coded pathnames, usernames,
# and passwords.

SED := /usr/bin/sed.exe
SED_QUIET_OPT := --quiet
SED_EXPRESSION_OPT := -e 

CHMOD := /usr/bin/chmod.exe

# Provided internally by Cygnus shell

CD := cd

# Used with DocBook.

OPENJADE := $(OPENJADE_DIR)/openjade.exe
ONSGMLS :=  $(OPENJADE_DIR)/onsgmls.exe





