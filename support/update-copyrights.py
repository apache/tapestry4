#!/bin/perl
# $Id$

# Run this from the support directory, and specify the path of a source directory
# to walk.

import sys, os
from stat import *
import re

updateCount = 0;

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
	
	if updateCopyright(file):
		print "FIXED"
	else:
		print "OK"		
	
def endsWith(s, suffix):
	return s[-len(suffix):] == suffix					
	
def updateCopyright(file):
	'''
Checks that the leading copyright message (if any) of the file (a Java source file)
matches the expected copyright.  If the message is up to date, returns false (indicating
no change to the file).  Otherwise, the file is updated with the new copyright
and true is returned.
'''

	input = open(file, "r")
	line = None
	
	good = 1	
	
	for cline in copyright:
		line = input.readline()
		
		if line != cline:
			good = 0
			break
		
	if good:
		line = input.readline()
				
	# Ignore anything else between the copyright block
	# and the package statement
			
			
	while not packageRe.match(line):
		good = 0
		line = input.readline()
		
	if good:
		input.close()
		return 0

	packageline = line
			
	newFile = file + "~"
	
	out = open(newFile, "w")

	out.writelines(copyright)
	out.write(packageline)
	
	while 1:
		line = input.readline()
		
		if line == "":
			break
			
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
	
	global updateCount
	
	updateCount = updateCount + 1
	
	return 1
								
	
def readCopyright(path):
	'''
Reads the file containing the copyright into an array, which is returned.
'''

	f = open(path, "r")
	
	result = f.readlines()
	
	f.close()
	
	return result
			
			
if __name__ == '__main__':
	global copyright
    
	copyright = readCopyright(sys.argv[1])
	
	for dir in sys.argv[2:]:
		walktree(dir, visitfile)

	print

	if updateCount == 0:
		print "All files up to date."
	else:
		print "Updated %d files." % updateCount
	

