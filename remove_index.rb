#!/usr/bin/env ruby

# Rename Illustrator-exported xxx_01.png to xxx.png
# Usage: scripts/remove_index.rb android/assets

require 'fileutils'

cwd = ARGV[0]
Dir[cwd+'/**/*.png'].each do |filename|
  new_filename = filename.gsub(/(.*)-\d+\.png/, '\1.png')
  if filename != new_filename
    FileUtils.mv(filename, new_filename)
    puts "Rename #{filename} to #{new_filename}"
  end
end
