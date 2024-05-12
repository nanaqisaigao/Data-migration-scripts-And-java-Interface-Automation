package com.hellobike.pmo.cockpit;

import com.hellobike.pmo.cockpit.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author ly
 * @since 1.0
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class BaseTest extends AbstractTestNGSpringContextTests {


    /**
     * -Denv=fat -Dowl.metrics.kafka.servers=fat-kafka1.ttbike.com.cn:9092,fat-kafka2.ttbike.com.cn:9092,fat-kafka3.ttbike.com.cn:9092 -Dapollo.meta=http://fat-apollometa.hellobike.cn:10080 -DbasicConf.host=https://fat-basicconf.hellobike.cn
     * @throws SQLException
     */
    static {
        System.setProperty("APPID","App-pmo-cockpit");
        System.setProperty("env","fat");
        System.setProperty("apollo.meta","http://fat-apollometa.hellobike.cn:10080");
        System.setProperty("basicConf.host","https://fat-basicconf.hellobike.cn");
        String [] s = {"fat-kafka1.ttbike.com.cn:9092"};
        System.setProperty("owl.metrics.kafka.servers", Arrays.toString(s));
    }

    @Test
    public void t(){
        System.out.println("success");
    }


}
