/**
 * baseInfo interfaces下所有的接口需要继承该类
 * 
 * 模式复用 RequestSpecBuilder ResponseSpecBuilder 
 * */
package com.Demo.mall;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;


public class baseInfo {

    public static RequestSpecification requestSpecification;

    /**
     * @author yangbin
     * 
     * 所有接口实现类都需要继承baseInfo类，实现对URL，Path的初始化，注入sessionID
     * 
     * @param baseURI
     * @param basePath
     * */
    public baseInfo(String baseURI,String basePath){

        //通用请求
    	RequestSpecBuilder builder=new RequestSpecBuilder();
    	builder.setBaseUri(baseURI);
		builder.setBasePath(basePath);
        //判断baseURl中是否有https或者http来断定使用P的协议是https还是http协议
        if(baseURI.contains("https")){
        	//RestAssured.proxy(8888);
        	builder.setPort(443);
            RestAssured.useRelaxedHTTPSValidation();
        }else{
        	builder.setPort(80);
        }
        requestSpecification = builder.build();
        requestSpecification.cookie("token","1f117146938f1d3fc3ca3d78ede68a9d");
    }
}
