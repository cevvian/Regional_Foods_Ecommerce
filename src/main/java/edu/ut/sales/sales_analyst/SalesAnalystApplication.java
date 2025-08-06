package edu.ut.sales.sales_analyst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class SalesAnalystApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesAnalystApplication.class, args);
    }

}
