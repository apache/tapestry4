#!/bin/perl
# $Id$

import sys, os
from stat import *
import re

packageRe = re.compile(r'^\s*package\s+');

def walktree(dir, callback):
    '''recursively descend the directory rooted at dir,
       calling the callback function for each regular file'''

    for f in os.listdir(dir):
        pathname = '%s/%s' % (dir, f)

        # Files may occasionally be deleted out
        # from under the sequence returned by listdir
		
        if not os.access(pathname, os.F_OK):
            continue
		
        mode = os.stat(pathname)[ST_MODE]
        if S_ISDIR(mode):
            # It's a directory, recurse into it
            walktree(pathname, callback)
        elif S_ISREG(mode):
            # It's a file, call the callback function
            callback(pathname)
        else:
            # Unknown file type, print a message
            print 'Skipping %s' % pathname

def visitfile(file):
	if not endsWith(file, ".java"):
		return
		
	print file, " ... ",
	
	if stripFile(file):
		print "FIXED"
	else:
		print "OK"		
	
def endsWith(s, suffix):
	return s[-len(suffix):] == suffix					
	
def stripFile(file):
	'''Strips leading comments (before the 'package') from a file, returning 1
if the file is updated, 0 if the file is already stripped.'''

	input = open(file, "r")
	
	line = input.readline()
		
	if packageRe.match(line):
		input.close()
		return 0

	newFile = file + "~"
	
	out = open(newFile, "w")
	foundPackage = 0
	
	while 1:
		line = input.readline()
		
		if line == "":
			break
			
		if foundPackage:
			out.write(line)
		elif packageRe.match(line):
			foundPackage = 1
			out.write(line)
			
	out.close()
	input.close()
		
	# A careful rename.  Save the original, rename the new file to replace
	# the original, then delete the renamed original
		
	saveFile = file + ".save"
	
	if os.access(saveFile, os.F_OK):
		os.remove(saveFile)
	
	os.rename(file, saveFile)
	os.rename(newFile, file)
	
	os.remove(saveFile)
	
	return 1
								
	
			
if __name__ == '__main__':
    walktree(sys.argv[1], visitfile)

	

