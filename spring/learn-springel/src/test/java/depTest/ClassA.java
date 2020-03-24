package depTest;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created by landy on 2018/12/15.
 */
public class ClassA implements InitializingBean{
    private ClassB b;

    public ClassB getB() {
        return b;
    }

    public void setB(ClassB b) {
        this.b = b;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init a...");
    }
}
