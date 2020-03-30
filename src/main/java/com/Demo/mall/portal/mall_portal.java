/**
 * mall系统portal服务的接口自动化测试脚本
 * 
 * create：2020-3-30
 * */
package com.Demo.mall.portal;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.Demo.mall.baseInfo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class mall_portal extends baseInfo{
	
	//private static final Logger log = LogManager.getLogger(mall_portal.class.getName());
	
    public mall_portal(String baseURI, String basePath) {

        super(baseURI, basePath);
    }

    /**
     * path:/home/content
     * 
     * 首页内容管理
     * 
     * @param null
     * */
    public Response getHomeContent_portal() {
    	
    	//String requestBody = "";
    	//log.info("请求body为："+ requestBody);
    	
		return RestAssured.given()
		       // .spec(requestSpecification)
		        .contentType(ContentType.JSON)
		    .when().log().all()
		    	.get("/home/content");
    }
    
    /**
     * path:/sso/getAuthCode
     * 
     * 获取验证码
     * 
     * @param telephone：电话号码
     * */
    public Response getAuthCode_portal(String telephone) {
    	
		return RestAssured.given()
		        //.spec(requestSpecification)
		        .contentType(ContentType.JSON)
		    .when().log().all()
		    	.queryParam("telephone", telephone)
		    	.get("/sso/getAuthCode");
    }
    
    
    /**
     * path:/sso/register
     * 
     * 注册账号
     * 
     * @param telephone：电话号码
     * @param username：用户名
     * @param password：密码
     * @param authCode：验证吗
     * */
    public Response register_portal(String telephone,String username,String password,String authCode) {
    	
		return RestAssured.given()
		        //.spec(requestSpecification)
		        .contentType(ContentType.JSON)
		    .when().log().all()
		    	.queryParam("telephone", telephone)
		    	.queryParam("username", username)
		    	.queryParam("password", password)
		    	.queryParam("authCode", authCode)
		    	.post("/sso/getAuthCode");
    }
}
