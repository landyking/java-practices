package springel;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Created by landy on 2018/7/5.
 */
public class ApplicationContextFactory {
    public static ApplicationContext createInstance() {
        GenericApplicationContext app = new GenericApplicationContext();
        app.registerBeanDefinition("SpElUtil", new RootBeanDefinition(SpElUtil.class));
        app.refresh();
        return app;
    }
}
