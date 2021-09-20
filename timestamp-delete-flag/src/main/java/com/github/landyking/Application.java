package com.github.landyking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        final ConfigurableApplicationContext app = SpringApplication.run(Application.class);
        app.getBean(TestExecutor.class).runTest();
    }

    @Service
    public class TestExecutor {
        @Resource
        private JdbcTemplate jdbcTemplate;

        public void runTest() {
            final Duration durationNormalInsert = timeMonitor(() -> {
                this.insertNormal(10, 1_000);
            });
            final Duration durationFlagDateInsert = timeMonitor(() -> {
                this.insertFlagDate(10, 1_000);
            });
            final Duration durationFlagLongInsert = timeMonitor(() -> {
                this.insertFlagLong(10, 1_000);
            });

            System.out.printf("time monitor[%s] used %s ms\r\n", "insert normal", durationNormalInsert.toMillis());
            System.out.printf("time monitor[%s] used %s ms\r\n", "insert flag date", durationFlagDateInsert.toMillis());
            System.out.printf("time monitor[%s] used %s ms\r\n", "insert flag long", durationFlagLongInsert.toMillis());
        }

        private void insertNormal(int tenantCount, int count) {
            for (int i = 1; i <= tenantCount; i++) {
                for (int j = 0; j < count; j++) {
                    int cur = j + 1;
                    jdbcTemplate.update(
                            "insert into t_user_normal (tenant_id,username,gender,industry,address,country,province,city) values(?,?,?,?,?,?,?,?)",
                            i, "user-" + cur, (cur % 2) + 1, "industry-" + cur, "address-" + cur, "country-" + cur, "province-" + cur, "city-" + cur
                    );
                }
                System.out.printf("done tenant[%s] count[%s]\r\n", i, count);
            }
        }

        private void insertFlagDate(int tenantCount, int count) {
            final Date zeroDate = new Date(0);
            for (int i = 1; i <= tenantCount; i++) {
                for (int j = 0; j < count; j++) {
                    int cur = j + 1;
                    jdbcTemplate.update(
                            "insert into t_user_flag_date (tenant_id,username,gender,industry,address,country,province,city,deleted_at) values(?,?,?,?,?,?,?,?,?)",
                            i, "user-" + cur, (cur % 2) + 1, "industry-" + cur, "address-" + cur, "country-" + cur, "province-" + cur, "city-" + cur,zeroDate
                    );
                }
                System.out.printf("done tenant[%s] count[%s]\r\n", i, count);
            }
        }

        private void insertFlagLong(int tenantCount, int count) {
            for (int i = 1; i <= tenantCount; i++) {
                for (int j = 0; j < count; j++) {
                    int cur = j + 1;
                    jdbcTemplate.update(
                            "insert into t_user_flag_long (tenant_id,username,gender,industry,address,country,province,city) values(?,?,?,?,?,?,?,?)",
                            i, "user-" + cur, (cur % 2) + 1, "industry-" + cur, "address-" + cur, "country-" + cur, "province-" + cur, "city-" + cur
                    );
                }
                System.out.printf("done tenant[%s] count[%s]\r\n", i, count);
            }
        }

        public Duration timeMonitor(Runnable run) {
            final Instant now = Instant.now();
            run.run();
            return Duration.between(now, Instant.now());
        }
    }

}
