#!/usr/bin/env ruby

# Pack images into texture atlas
# Usage: scripts/pack_atlas.rb ~/AndroidStudioProjects/tools/libgdx/ android/assets/img/ui/overlay/unpacked ui

tool_dir = ARGV[0]
input_dir = File.expand_path(ARGV[1])
output_dir = File.join(input_dir.split('/')[0...-1].join('/'), ARGV[2])

packer_config = <<CONFIG
{
  paddingX: 0,
  paddingY: 0,
  edgePadding: false,
  alias: false
}
CONFIG

Dir.chdir(tool_dir)
pack_config_filepath = File.join(input_dir, 'pack.json')
File.open(pack_config_filepath, 'w') { |f| f.write(packer_config) }

puts `java -cp gdx.jar:extensions/gdx-tools/gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker #{input_dir} #{output_dir}`

File.delete(pack_config_filepath)
