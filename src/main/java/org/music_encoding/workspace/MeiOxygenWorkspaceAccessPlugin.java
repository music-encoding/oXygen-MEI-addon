package org.music_encoding.oxygen.plugin;

import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;

/**
 * Hello MEI!
 */

public class MeiOxygenPlugin extends Plugin {
  /**
   * The static plugin instance.
   */
    private static MeiOxygenPlugin instance = null;

    /**
     * Construct the plugin.
     *
     * @param descriptor The plugin descriptor
     */
    public MeiOxygenPlugin(PluginDescriptor descriptor) {

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
  public static MeiOxygenPlugin getInstance() {
    return instance;
  }
}
