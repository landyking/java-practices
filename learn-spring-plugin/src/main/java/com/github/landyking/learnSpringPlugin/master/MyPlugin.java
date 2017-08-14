package com.github.landyking.learnSpringPlugin.master;

import org.springframework.plugin.core.Plugin;
import org.springframework.plugin.metadata.MetadataProvider;
import org.springframework.plugin.metadata.PluginMetadata;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/11 11:43
 * note:
 */
public interface MyPlugin extends Plugin<PluginMetadata>,MetadataProvider {
    public void sayHello();
}
