package com.github.landyking.learnSpringPlugin.master;

import org.springframework.plugin.core.OrderAwarePluginRegistry;
import org.springframework.plugin.metadata.PluginMetadata;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/11 11:36
 * note:
 */
public class MasterContainer {
    private OrderAwarePluginRegistry<MyPlugin,PluginMetadata> myPlugins;

    public void setMyPlugins(OrderAwarePluginRegistry<MyPlugin, PluginMetadata> myPlugins) {
        this.myPlugins = myPlugins;
    }
    public void showPlugins(){
        for (MyPlugin one : myPlugins) {
            System.out.println("name: "+one.getMetadata().getName()+", version: "+one.getMetadata().getVersion());
            one.sayHello();
        }
    }
}
