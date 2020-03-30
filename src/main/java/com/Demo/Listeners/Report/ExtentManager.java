package com.Demo.Listeners.Report;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;

    //报告名称
    @SuppressWarnings("unused")
    private String reportName;
    //报告标签
    @SuppressWarnings("unused")
    private String reportTag;
    //服务器ip
    @SuppressWarnings("unused")
    private String host;
    //DB端口
    @SuppressWarnings("unused")
    private int port;
    //项目名称
    @SuppressWarnings("unused")
	private String projectName;
    //链接URL
    @SuppressWarnings("unused")
	private String serverUrl;

    public void setReportName(String reportName) {

        this.reportName = reportName;
    }

    public void setReportTag(String reportTag) {

        this.reportTag = reportTag;
    }

    public void setHost(String host){

        this.host = host;
    }

    public void setPort(int port){

        this.port = port;
    }

    public void setProjectName(String projectName){

        this.projectName = projectName;
    }

    public void setServerUrl(String serverUrl){

        this.serverUrl = serverUrl;
    }

    public static ExtentReports getInstance(String filePath) {
        if (extent == null)
            createInstance(filePath);
        return extent;
    }


    public static void createInstance(String filePath) {
        extent = new ExtentReports();
        extent.setSystemInfo("os", "Linux");
        extent.attachReporter(createHtmlReporter(filePath), (ExtentReporter) createExtentXReporter());
    }

    public static ExtentHtmlReporter createHtmlReporter(String filePath){
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        //报表位置
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        //使报表上的图表可见
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(filePath);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("XXX项目测试报告");
        return htmlReporter;
    }

    public static ExtentXReporter createExtentXReporter() {
        ExtentXReporter extentx = new ExtentXReporter("49.234.62.90",27017);
        extentx.config().setProjectName("test1");
        extentx.config().setReportName("Build-1224");
        extentx.config().setServerUrl("http://49.234.62.90:1337");
        return extentx;
    }
}