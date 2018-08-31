package com.example.webDao;

import com.example.webDao.annotation.InnerService;
import com.example.webDao.annotation.WebDaoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDiscoveryClient
@SpringBootApplication
@WebDaoScan(
        basePackages = "com.example.springcloudclient.client",
        annotationClass = InnerService.class)
public class SpringcloudClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudClientApplication.class, args);
    }
}
