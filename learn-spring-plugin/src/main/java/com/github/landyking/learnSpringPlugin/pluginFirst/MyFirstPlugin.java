package com.github.landyking.learnSpringPlugin.pluginFirst;

import com.github.landyking.learnSpringPlugin.master.MyPlugin;
import org.springframework.plugin.metadata.AbstractMetadataBasedPlugin;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/11 11:37
 * note:
 */
public class MyFirstPlugin extends AbstractMetadataBasedPlugin implements MyPlugin {
    public MyFirstPlugin() {
        super("my first plugin", "1.0.1");
    }

    @Override
    public void sayHello() {
        System.out.println("hello world!!!!");
    }
}
