package com.mall.portal;

import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.Demo.Listeners.Assert.AssertListener;
import com.Demo.Listeners.Failretry.MyRetryAnalyzer;
import com.Demo.Listeners.Report.ReportListener;
import com.Demo.mall.portal.mall_portal;

@Listeners({ReportListener.class,AssertListener.class})
public class testMallPortal {
	
	private mall_portal port;
	
    //测试用例名称 不添加此成员变量默认按照时间命名测试用例名称
    @SuppressWarnings("unused")
	private String testCaseName;
    //测试报告title名称
    @SuppressWarnings("unused")
	private String testReportName;
    //测试用例执行人
    @SuppressWarnings("unused")
	private String tester;
    
    //初始化 所有基本信息参数通过TestNG.xml配置文件传入，持续集成后通过Jenkins参数化
    @Parameters({"baseURL","basePath","testCaseName","testReportName","tester"})
    @BeforeClass
    public void init(String baseURL, String basePath , String caseName,String reportName,String owner){

        testCaseName = caseName;
        testReportName = reportName;
        tester = owner;
        
        port = new mall_portal(baseURL,basePath);
    }
    
    @Test(description="注册用户",groups = "mall_portal",retryAnalyzer = MyRetryAnalyzer.class)
    public void addDomain_9901_rootNode(){
    	
    	System.out.println("++++++++++++++执行mall_portal成功，输出测试结果++++++++++++++");
    }
}
