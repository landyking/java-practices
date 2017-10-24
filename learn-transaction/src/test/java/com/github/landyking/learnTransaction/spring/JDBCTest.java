package com.github.landyking.learnTransaction.spring;

import com.google.common.base.Throwables;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/23 9:21
 * note:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jdbc.xml")
public class JDBCTest {
    private Logger logger = LoggerFactory.getLogger(JDBCTest.class);
    @Resource
    private DataSource dataSource;
    @Resource
    private PlatformTransactionManager transactionManager;

    @Before
    public void before() throws Exception {
        initTables();
        showAllData();
    }

    private void initTables() throws SQLException {
        logger.info("初始化表结构开始");
        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();
        logger.info("检查删除表 t_data");
        statement.execute("DROP TABLE IF EXISTS `t_data`;");
        logger.info("创建表 t_data");
        statement.execute("create table t_data(" +
                "id varchar(50) not null," +
                "count int not null," +
                "name varchar(100) not null," +
                "PRIMARY  key (id)" +
                ");");
        logger.info("开始向表 t_data 中插入测试数据");
        int i = statement.executeUpdate("insert into t_data values('11111',100,'hello');");
        logger.info("向表 t_data 中插入" + i + "条测试数据");
        statement.close();
        conn.close();
        logger.info("初始化表结构成功");
    }

    public void showAllData() throws SQLException {
        showAllData(null);
    }

    public void showAllData(Connection oldConn) throws SQLException {
        logger.info("############ 当前表 t_data 中所有数据  ########################");
        Connection connection;
        if (oldConn != null) {
            connection = oldConn;
        } else {
            connection = dataSource.getConnection();
        }
        ResultSet resultSet = connection.createStatement().executeQuery("select * from t_data");
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            int count = resultSet.getInt("count");
            String name = resultSet.getString("name");
            logger.info("****\t" + "\t" + id + "\t" + count + "\t" + name);
        }
        resultSet.close();
        if (oldConn == null) {
            connection.close();
        }
        logger.info("@@@@@@@@@@@@ 当前表 t_data 中所有数据  @@@@@@@@@@@@@@@@@@@@@@@@");
    }

    /**
     * 代码中不体现事务操作。
     * 此时使用数据库默认的事务设定，自动提交。
     *
     * @throws Exception
     */
    @Test
    public void testIgnoreTransaction() throws Exception {
        Connection connection = dataSource.getConnection();
        int count = connection.createStatement().executeUpdate("update t_data set count=222 where id='11111'");
        logger.info("将count修改为222，执行影响行数:" + count);
        connection.close();
        showAllData();
        Assert.assertEquals(222, getCurrentCount());
    }

    /**
     * sql执行者自己从datasource获取connection，自己设定事务，执行操作。正常提交
     *
     * @throws Exception
     */
    @Test
    public void testSelfTransaction() throws Exception {
        Connection connection = dataSource.getConnection();
        boolean oldAutoCommit = connection.getAutoCommit();
        logger.info("记录原始的事务自动提交设置:" + oldAutoCommit);
        connection.setAutoCommit(false);
        logger.info("设置事务为非自动提交");
        try {
            int count = connection.createStatement().executeUpdate("update t_data set count=222 where id='11111'");
            logger.info("将count修改为222，执行影响行数:" + count);
            showAllData(connection);
            connection.commit();
            logger.info("执行成功，提交事务");
        } catch (Exception e) {
            logger.error("执行出现异常，将回滚事务", e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(oldAutoCommit);
            logger.info("还原事务自动提交设置");
            connection.close();
        }
        showAllData();
        Assert.assertEquals(222, getCurrentCount());
    }

    private int getCurrentCount() throws SQLException {
        int count = new JdbcTemplate(dataSource).queryForObject("select count from t_data where id='11111'", Number.class).intValue();
        logger.info("获取当前11111的count为：" + count);
        return count;
    }

    /**
     * sql执行者自己从datasource获取connection，自己设定事务，执行操作。回滚操作有效。
     *
     * @throws Exception
     */
    @Test
    public void testSelfTransactionRollbackSuccess() throws Exception {
        Connection connection = dataSource.getConnection();
        boolean oldAutoCommit = connection.getAutoCommit();
        logger.info("记录原始的事务自动提交设置:" + oldAutoCommit);
        connection.setAutoCommit(false);
        logger.info("设置事务为非自动提交");
        try {
            int count = connection.createStatement().executeUpdate("update t_data set count=222 where id='11111'");
            logger.info("将count修改为222，执行影响行数:" + count);
            showAllData(connection);
            if (1 + 1 > 0) {
                throw new IllegalStateException("故意异常");
            }
            connection.commit();
            logger.info("执行成功，提交事务");
        } catch (Exception e) {
            logger.error("执行出现异常，将回滚事务: " + e.getMessage());
            connection.rollback();
        } finally {
            connection.setAutoCommit(oldAutoCommit);
            logger.info("还原事务自动提交设置");
            connection.close();
        }
        showAllData();
        Assert.assertEquals(100, getCurrentCount());
    }

    /**
     * transactionTemplate获取connection，绑定到线程
     * sql执行者自己从datasource获取connection，执行操作。
     * 此时，使用的不是同一个connection，所以也就不是同一个事务，所以transactionTemplate无效。
     *
     * @throws Exception
     */
    @Test
    public void testManagerTransactionRollbackFailure() throws Exception {
        try {
            new TransactionTemplate(transactionManager).execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    Connection connection = null;
                    try {
                        connection = dataSource.getConnection();
                        int count = connection.createStatement().executeUpdate("update t_data set count=222 where id='11111'");
                        logger.info("将count修改为222，执行影响行数:" + count);
                        showAllData(connection);
                        if (1 + 1 > 0) {
                            throw new IllegalStateException("故意异常");
                        }
                    } catch (Exception e) {
                        Throwables.propagate(e);
                    } finally {
                        if (connection != null) {

                            try {
                                connection.close();
                            } catch (SQLException e) {
                                Throwables.propagate(e);
                            }
                        }
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("使用事务模板执行异常：" + e.getMessage());
        }

        showAllData();
        Assert.assertEquals(222, getCurrentCount());
    }

    /**
     * transactionTemplate获取connection，绑定到线程， DataSourceUtils.getConnection通过线程获取绑定的connection进行使用。
     * 此时释放仍要调用DataSourceUtils进行释放。
     *
     * @throws Exception
     */
    @Test
    public void testManagerTransactionRollbackSuccess() throws Exception {

        try {
            new TransactionTemplate(transactionManager).execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    Connection connection = DataSourceUtils.getConnection(dataSource);
                    try {
                        int count = connection.createStatement().executeUpdate("update t_data set count=222 where id='11111'");
                        logger.info("将count修改为222，执行影响行数:" + count);
                        showAllData(connection);
                        if (1 + 1 > 0) {
                            throw new IllegalStateException("故意异常");
                        }
                    } catch (Exception e) {
                        Throwables.propagate(e);
                    } finally {
                        DataSourceUtils.releaseConnection(connection, dataSource);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("使用事务模板执行异常：" + e.getMessage());
        }
        showAllData();
        Assert.assertEquals(100, getCurrentCount());
    }

    /**
     * transactionTemplate获取connection，绑定到线程，jdbctemplate通过线程获取绑定的connection进行使用。
     *
     * @throws Exception
     */
    @Test
    public void testManagerTransactionRollbackSuccess2() throws Exception {
        try {
            new TransactionTemplate(transactionManager).execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus transactionStatus) {
                    new JdbcTemplate(dataSource).execute(new ConnectionCallback<Integer>() {
                        @Override
                        public Integer doInConnection(Connection con) throws SQLException, DataAccessException {

                            int count = con.createStatement().executeUpdate("update t_data set count=222 where id='11111'");
                            logger.info("将count修改为222，执行影响行数:" + count);
                            showAllData(con);
                            if (1 + 1 > 0) {
                                throw new IllegalStateException("故意异常");
                            }
                            return count;
                        }
                    });
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("使用事务模板执行异常：" + e.getMessage());
        }
        showAllData();
        Assert.assertEquals(100, getCurrentCount());
    }
}
