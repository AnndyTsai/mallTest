/**
 * create by anndy 2019
 *
 * 实现ITestListener接口
 *
 * @IReporter 报告
 * @ITestListener 重要是方便用户添加测试用例名称 testcase中需要添加private成员变量name来赋值
 *
 * template为html模板，生产静态html数据展示模板
 *
 * testcase引入listener方式为
 *  todo 方法一：@Listeners({ReportListenerWithServer.class})
 *  todo 方法二：也可以使用testng.xml的<listeners><listeners/>引入
 *
 * ***************************************备注*************************************
 * 1：该监听器支持html数据展示和服务端数据存储
 * 2：服务端数据存储需要安装DataBase并且需要启动http对应的服务，否则服务端数据无法被记录或者查看
 * 3：该监听器的使用需要集合 ExtentManager 类
 * ***************************************结束*************************************
 * */
package com.Demo.Listeners.Report;

import org.testng.ITestListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.io.File;

public class ReportListenerWithServer implements ITestListener {

	private static ExtentReports extent = ExtentManager.getInstance("test-output/report.html");
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal test = new ThreadLocal();

	@Override
	public synchronized void onStart(ITestContext context) {
	}

	@Override
	public synchronized void onFinish(ITestContext context) {
		
		extent.flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void onTestStart(ITestResult result) {
		
		test.set(extent.createTest(result.getMethod().getMethodName()));

	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		
		((ExtentTest) test.get()).pass("Test passed");
	}

	@Override
	public synchronized void onTestFailure(ITestResult result) {

		((ExtentTest) test.get()).fail(result.getThrowable());
		File directory = new File("test-output");
		try {
			String screenPath = directory.getCanonicalPath() + "/";
			File file = new File(screenPath);
			if (!file.exists()) {
				file.mkdirs();
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onTestSkipped(ITestResult result) {
		
		((ExtentTest) test.get()).skip(result.getThrowable());
	}

	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}
}
