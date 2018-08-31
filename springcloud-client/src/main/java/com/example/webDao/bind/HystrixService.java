package com.example.webDao.bind;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.webDao.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author chendong
 * @date 2018-08-14
 * @description 核心熔断请求处理类
 */

public class HystrixService {


    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 只有url中包含refresh或者task时,按照task进行刷新
     *
     * @return
     */
    private Boolean justTaskOrRereshLogger(String url) {
        if (url.contains("refresh") || url.contains("task")) {
            return false;
        }
        return true;
    }

    private void info(String body, String url, Object... params) {
        if (justTaskOrRereshLogger(url)) {
            logger.info(body, url, params);
        }
    }

    private Result error1(String json, String url, Throwable e) {
        info("******Error：json格式，URL :{} ，Data：{}，Cause by" + e.toString(), url, json);
        if (url.contains("task")) {
            return new Result(Result.SUCCESS_CODE, "定时任务已经执行");
        }
        return new Result(Result.SERVER_ERROR, "Can‘t reachable service url :" + url + ",Cause by :  " + e.toString());
    }

    private Result error2(String url, Map<String, Object> params, Throwable e) {
        info("******Error：form格式，URL :{} ，Data：{} ，Cause by" + e.toString(), url, toJson(params));
        if (url.contains("task")) {
            return new Result(Result.SUCCESS_CODE, "定时任务已经执行");
        }
        return new Result(Result.SERVER_ERROR, "Can‘t reachable service url :" + url + ",Cause by :  " + e.toString());
    }

//    @HystrixCommand(fallbackMethod = "error1")
    public Result sendJsonRequest(String json, String url) {
        info("= = > > Start：Json格式，URL :{} ，Data：{} ", url, json);
        //  一定要设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        ResponseEntity<Result> response = restTemplate.postForEntity(url, requestEntity, Result.class);
        info("= = > > Complete：form格式，URL :{}，Response：{} ", url, toJson(response.getBody().getMessage()));
        return response.getBody();
    }

//    @HystrixCommand(fallbackMethod = "error2")
    public Result sendParamsRequest(String url, Map<String, Object> params) {
        info("= = > > Start：form格式，URL :{} ，Data：{} ", url, toJson(params));
        if (params == null) {
            params = new HashMap<>();
        }
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            paramMap.add(entry.getKey(), entry.getValue());
        }
        Result response = restTemplate.postForObject(url, paramMap, Result.class);
        info("= = > > Complete：form格式，URL:{}，Response：{}  ", url, toJson(response.getMessage()));
        return response;
    }


    private String toJson(Object obj) {
        return JSON.toJSONString(obj, new SerializeConfig(), SerializerFeature.WriteMapNullValue);
    }
}
