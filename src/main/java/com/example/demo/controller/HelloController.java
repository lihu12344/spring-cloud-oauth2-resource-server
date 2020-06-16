package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class HelloController {

    @Resource
    private RestTemplate restTemplate;

    private String accessToken;

    @RequestMapping("/redirect")
    public JSONObject redirect(String code){
        Map<String,String> params=new HashMap<>();

        params.put("client_id","user");
        params.put("client_secret","123456");
        params.put("grant_type","authorization_code");
        params.put("redirect_uri","http://localhost:8082/redirect");
        params.put("code",code);

        String url="http://localhost:8081/oauth/token"+"?client_id={client_id}"
                + "&client_secret={client_secret}" +"&grant_type={grant_type}"+
                "&redirect_uri={redirect_uri}"+"&code={code}";

        String result=restTemplate.postForObject(url,null,String.class,params);
        System.out.println(result);

        JSONObject object= JSONObject.parseObject(result);
        System.out.println("map："+object.getInnerMap());

        accessToken=object.getString("access_token");
        System.out.println("access_token："+object.getString("access_token"));

        System.out.println("refresh_token："+object.getString("refresh_token"));

        String scope=object.getString("scope");
        Set<String> scopes = new HashSet<>(Arrays.asList(scope.split(" ")));
        System.out.println(scopes);

        System.out.println("scope："+object.getString("scope"));
        System.out.println("created_at："+object.getString("created_at"));
        System.out.println("token_type："+object.getString("token_type"));
        System.out.println("expires_in："+object.getString("expires_in"));

        return object;
    }

    @RequestMapping("/getUser")
    public JSONObject getUser(){
        Map<String,String> params=new HashMap<>();
        params.put("access_token",accessToken);

        String url="http://localhost:8081/user?access_token={access_token}";

        return JSONObject.parseObject(restTemplate.getForObject(url,String.class,params));
    }
}
