# Texture Viewer
Adds a debug command that displays the lightmap and gui items atlas for debugging purposes. As of v1.1, also allows the lightmap to be dumped to a texture using the F3 + S keybind

Run `/eg_texture_viewer <viewer name> <boolean>` to enable or disable the viewers, alternatively you can use `/eg_texture_viewer <viewer name> <int>` to change the size. `<viewer name>` can be either `lightmap` or `items_atlas`

The `items_atlas` viewer will only show when an item is currently being rendered in a gui. This is not the same thing as the block/item texture atlas, its mainly just part of this mod as I was curious how it looked ingame
