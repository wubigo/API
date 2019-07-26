package com.timon;

import com.timon.stream.Alerts;
import com.timon.stream.FlinkConsumer;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class AlertWebApplication {

    @Autowired
    FlinkConsumer fc;


    public static void main(String[] args) {


        SpringApplication.run(AlertWebApplication.class, args);
    }

    //@Override
    public void run(String... args) throws Exception {

        //Alerts.getAlerts(fc);
    }


    public static class AlertWithCount{

        public String level;
        public long count;

        public AlertWithCount() {}

        public AlertWithCount(String level, long count) {
            this.level = level;
            this.count = count;
        }

        @Override
        public String toString() {
            return level + " : " + count;
        }
    }


}
