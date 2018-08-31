package com.example.webDao.controller;

import com.example.webDao.client.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author chendong
 * @date 2018-05-31
 * @description
 */
@RestController
public class DcController {

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    private IUserService userService;

    @GetMapping("/dc")
    public String dc() {
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }


    @GetMapping("/get")
    public String queryUserInfo() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("test", "hahha");
        String s = userService.queryUserInfo(map);
        System.out.println("s = " + s);
        return s;
    }


}