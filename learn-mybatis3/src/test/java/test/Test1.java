package test;

import com.github.learnMybatis3.MyConfiguration;
import com.github.learnMybatis3.MyXMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * @author: landy
 * @date: 2018-01-20 14:28
 */
public class Test1 {
    @org.junit.Test
    public void test1() throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        MyConfiguration config = (MyConfiguration) new MyXMLConfigBuilder(inputStream).parse();
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
        SqlSession session = sqlSessionFactory.openSession();
        System.out.println("open session");

        List<Object> objects = null;
        try {
            System.out.println("第一次调用");
            objects = session.selectList("mapper.BlogMapper.selectBlog2", 1);
        } catch (Exception e) {
            System.out.println("第一次调用,第一次调用失败");
            System.out.println("动态插入xml语句：");
            addXmlToConfig(config);
            System.out.println("第二次调用");
            objects = session.selectList("mapper.BlogMapper.selectBlog2", 1);
            System.out.println("########");
            objects = session.selectList("mapper.BlogMapper.selectBlog2", 1);
            System.out.println("第二次调用成功");
            System.out.println("移除xml语句");
            config.removeXml("mapper.BlogMapper.selectBlog2");
            System.out.println("第三次调用，预期失败");
            try {
                objects = session.selectList("mapper.BlogMapper.selectBlog2", 1);
                System.out.println("第三次调用不符合预期");
            } catch (Exception e1) {
                System.out.println("第三次调用符合预期");
            }
        }
        System.out.println(objects.size());
        if (!objects.isEmpty()) {
            System.out.println(objects.get(0).getClass().getName());
        }
        /*for (Object one : objects) {
            System.out.println(one);
        }*/
    }

    private void addXmlToConfig(MyConfiguration config) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper\n" +
                "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\"mapper.BlogMapper\">\n" +
                "    <select id=\"selectBlog\" resultType=\"hashmap\" parameterType=\"int\">\n" +
                "        select t.*,u.* from t_department t,t_app_user u where t.id = u.dep_id\n" +
                "        <if test=\"_parameter >=1\">\n" +
                "            and 1=#{id}\n" +
                "        </if>\n" +
                "        <if test=\"_parameter >=2\">\n" +
                "            and 2=#{id}\n" +
                "        </if>\n" +
                "    </select>\n" +
                "</mapper>";

        config.addSelectXml(xml);
        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper\n" +
                "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\"mapper.BlogMapper\">\n" +
                "    <select id=\"selectBlog2\" resultType=\"hashmap\" parameterType=\"int\">\n" +
                "        select t.*,u.* from t_department t,t_app_user u where t.id = u.dep_id\n" +
                "        <if test=\"_parameter >=1\">\n" +
                "            and 1=#{id}\n" +
                "        </if>\n" +
                "        <if test=\"_parameter >=2\">\n" +
                "            and 2=#{id}\n" +
                "        </if>\n" +
                "    </select>\n" +
                "</mapper>";
        config.addSelectXml(xml);
    }
}
