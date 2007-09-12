#!/usr/bin/ruby

DIST_DIR="target"

Dir.glob("#{DIST_DIR}/*.{zip,gz,bz2}") do |filename|
  puts filename
  # ... and you have to provide your passphrase again and again!
  system "gpg --armor --output #{filename}.asc --detach-sig #{filename}"
  system "md5sum #{filename} > #{filename}.md5"
end

puts "Uploading distributions ..."

# system 'scp #{DIST_DIR}/*.* jkuhnert@people.apache.org:public_html/tapestry_releases'
