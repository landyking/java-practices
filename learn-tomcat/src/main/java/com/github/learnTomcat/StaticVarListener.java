package com.github.learnTomcat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/9/29 15:17
 * note:
 */
public class StaticVarListener implements ServletContextListener {

    private ScheduledExecutorService scheduledExecutorService;
    private String text;
    private static String staticText = "";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        text = servletContextEvent.getServletContext().getInitParameter("text");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                staticText = text;
                System.out.println(text + ": set value is: " + staticText);
                int i = new Random().nextInt(20);
                System.out.println(text + ": will sleep " + i + " second!");
                try {
                    TimeUnit.SECONDS.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(text + ": get value is: " + staticText);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        scheduledExecutorService.shutdownNow();
    }
}
