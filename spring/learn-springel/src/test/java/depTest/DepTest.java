package depTest;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by landy on 2018/12/15.
 */
public class DepTest {
    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("test1.xml");
//        Object a = app.getBean("a");
        Object b = app.getBean("b");

    }
}
