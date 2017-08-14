package com.github.landyking.learnSpringPlugin.master;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/14 11:18
 * note:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:master.xml")
public class MyPluginTest {
    @Resource
    private MasterContainer masterContainer;

    @Test
    public void test222() throws Exception {
        masterContainer.showPlugins();
    }
}