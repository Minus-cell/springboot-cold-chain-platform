package com.coldchain.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.coldchain.server.mapper")
public class ColdChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColdChainApplication.class, args);
    }

}
