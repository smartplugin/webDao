package com.example.webDao.client;

import com.example.webDao.annotation.InnerService;
import com.example.webDao.annotation.Json;

import java.util.Map;

/**
 * @author chendong
 * @date 2018-08-30
 * @description  使用demo
 */
@InnerService("innner-user-api")
public interface IUserService {

    @Json("/test/")
    String queryUserInfo(Map<String,Object> map);
}
