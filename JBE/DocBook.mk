# $Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2001 by Howard Ship and Primix
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

# Allows for the creation of HTML from a DocBook XML file.
# Automatically handles installation of the necessary DocBook DTDs, the
# XSL stylesheets and the various processors.

	
default: documentation

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk

DOCBOOK_XSL_DIR := $(DOCBOOK_DIR)/docbook-xsl-1.44
DOCBOOK_DTD_DIR := $(DOCBOOK_DIR)/docbookx412

HTML_DIR := html

DOCUMENT_RESOURCE_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/document-resources
HTML_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/html
HTML_PACKAGE_FILE := $(basename $(MAIN_DOCUMENT)).tar.gz

DISTRO_STAMP_FILE := $(DOCBOOK_DIR)/.distro-stamp

DOCBOOK_DISTROS := \
	$(DOCBOOK_DIR)/docbook-xsl-1.44.tar.gz \
	$(DOCBOOK_DIR)/docbkx412.tar.gz

initialize: setup-jbe-util $(DISTRO_STAMP_FILE)
ifeq "$(XALAN_DIR)" ""
	$(error You must set XALAN_DIR in LocalConfig.mk)
endif
	@$(MKDIRS) $(SYS_BUILD_DIR_NAME) $(HTML_DIR)

$(DISTRO_STAMP_FILE): $(DOCBOOK_DISTROS)
	$(call NOTE, Unarchiving DocBook distributions ...)
	@for archive in $? ; do \
		$(ECHO) Extracting: $$archive ; \
		$(CAT) $$archive | \
		($(CD) $(DOCBOOK_DIR) && \
		 $(GNUTAR) $(GNUTAR_EXTRACT_OPT) $(GNUTAR_GZIP_OPT)) ; \
	done
	@$(TOUCH) $@

html: initialize $(HTML_STAMP_FILE)

XSL_CLASSPATH := \
 	$(XALAN_DIR)/bin/xml.jar \
 	$(XALAN_DIR)/bin/xerces.jar \
 	$(XALAN_DIR)/bin/xalan.jar
 	
MOD_HTML_OPTS := \
  -param use.id.as.filename 1
  
ifdef USE_STANDARD_IMAGES

STANDARD_IMAGES_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/standard-images

$(STANDARD_IMAGES_STAMP_FILE):
	$(call NOTE, Copying standard images ...)
	@$(MKDIRS) $(HTML_DIR)/standard-images
	$(call COPY_TREE, \
		$(DOCBOOK_XSL_DIR)/images,\
		*.png callouts, $(HTML_DIR)/standard-images)
	@$(TOUCH) $(STANDARD_IMAGES_STAMP_FILE)

html: copy-standard-images

copy-standard-images: initialize $(STANDARD_IMAGES_STAMP_FILE)

# Add rules to use the standard graphics in admonitions and callouts

MOD_HTML_OPTS += \
	-param admon.graphics 1 \
	-param admon.graphics.path standard-images/ \
	-param callout.graphics 1 \
	-param callout.graphics.path standard-images/callouts/
endif

# If the user specifies a stylesheet, then we copy and edit it

ifneq "$(HTML_STYLESHEET)" ""

MARKED_STYLESHEET := $(SYS_BUILD_DIR_NAME)/marked.xsl

FINAL_HTML_STYLESHEET := $(MARKED_STYLESHEET)

$(MARKED_STYLESHEET): $(HTML_STYLESHEET)
	$(call NOTE, Generating $@ from $? ...)
	@$(SED) \
	  $(SED_EXPRESSION_OPT) "s/##DOCBOOK_XSL_DIR##/$(subst $(SLASH),$(BACKSLASH)$(SLASH),$(DOCBOOK_XSL_DIR))/g" \
	 $? > $@
	 
else
FINAL_HTML_STYLESHEET := $(DOCBOOK_XSL_DIR)/html/chunk.xsl
endif

$(HTML_STAMP_FILE): $(DOCUMENT_RESOURCE_STAMP_FILE)  \
	$(MAIN_DOCUMENT) $(FINAL_HTML_STYLESHEET) $(OTHER_DOC_FILES)
	$(call NOTE, Generating HTML from $(MAIN_DOCUMENT) ...)
	$(call EXEC_JAVA, \
		$(XSL_CLASSPATH), \
		-Xms256mb -Xmx512mb \
		org.apache.xalan.xslt.Process -HTML \
			-in $(MAIN_DOCUMENT)  \
			-xsl $(FINAL_HTML_STYLESHEET)  \
			-param base.dir $(HTML_DIR)/ \
			-param root.filename $(basename $(MAIN_DOCUMENT)) \
			$(MOD_HTML_OPTS) $(HTML_XSLT_OPTS))
	@$(TOUCH) $@

clean:
	$(call  NOTE, "Cleaning ...")
	@$(RM) $(HTML_DIR) $(SYS_BUILD_DIR_NAME) $(HTML_PACKAGE_FILE)
	
FINAL_HTML_INSTALL_DIR := \
	$(firstword $(HTML_INSTALL_DIR) $(INSTALL_DIR))
	
install-html: html
ifeq "$(FINAL_HTML_INSTALL_DIR)" ""
	$(error You must set a value for HTML_INSTALL_DIR or INSTALL_DIR)
endif
	$(call NOTE, Installing HTML documentation to $(FINAL_HTML_INSTALL_DIR) ...)
	@$(MKDIRS) $(FINAL_HTML_INSTALL_DIR)
	@$(call COPY_TREE, $(HTML_DIR), . , $(FINAL_HTML_INSTALL_DIR))

package-html: $(HTML_PACKAGE_FILE)

$(HTML_PACKAGE_FILE): html
	$(call NOTE, Packaging HTML as $(HTML_PACKAGE_FILE) ...)
	$(GNUTAR) czf $(HTML_PACKAGE_FILE) --directory=$(HTML_DIR) .

# Rules for dealing with DOCUMENT_RESOURCES, generally images (or directories of images)
# that should be included with the generated HTML.

ifneq "$(DOCUMENT_RESOURCES)" ""
FINAL_DOCUMENT_RESOURCES := \
	${shell $(FIND) $(DOCUMENT_RESOURCES) \
		-name \* ! \( -name CVS -prune \) -type f \
		-print}
endif

$(DOCUMENT_RESOURCE_STAMP_FILE): $(FINAL_DOCUMENT_RESOURCES)
ifneq "$(FINAL_DOCUMENT_RESOURCES)" ""
	$(call NOTE, Copying document resources ...)
	@$(ECHO) Copying: $(notdir $?)
	@$(call COPY_TREE, . , $? , $(HTML_DIR))
endif
	@$(TOUCH) $@

FINAL_FORMATS := $(if $(FORMATS),$(FORMATS),html)

documentation: $(FINAL_FORMATS)

install: $(addprefix install-,$(FINAL_FORMATS))

.PHONY: html install-html package-html
.PHONY: default documentation initialize clean
.PHONY: install
