package org.music_encoding.workspace;

import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;

/**
 * MeiOxygenWorkspace access plugin.
 */
public class MeiOxygenWorkspaceAccessPlugin extends Plugin {
  /**
   * The static plugin instance.
   */
  private static MeiOxygenWorkspaceAccessPlugin instance = null;

  /**
   * Constructs the plugin.
   *
   * @param descriptor The plugin descriptor
   */
  public MeiOxygenWorkspaceAccessPlugin(PluginDescriptor descriptor) {
    super(descriptor);

    if (instance != null) {
      throw new IllegalStateException("Already instantiated!");
    }
    instance = this;
  }

  /**
   * Get the plugin instance.
   *
   * @return the shared plugin instance.
   */
  public static MeiOxygenWorkspaceAccessPlugin getInstance() {
    return instance;
  }
}
