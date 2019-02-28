package depTest;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created by landy on 2018/12/15.
 */
public class ClassB implements InitializingBean{
    private ClassA a;

    public ClassA getA() {
        return a;
    }

    public void setA(ClassA a) {
        this.a = a;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init b");
    }
}
