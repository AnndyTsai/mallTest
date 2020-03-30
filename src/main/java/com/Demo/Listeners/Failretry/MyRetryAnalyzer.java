/**
 * create by anndy 2019
 *
 *  实现Assert断言失败后重跑失败的测试用例
 *
 *  todo 引入方法：@Test注解引入 示例：@Test(retryAnalyzer = MyRetryAnalyzer.class)
 *
 * ***************************************备注*************************************
 * 1：该监听器默认执行重跑次数为3尺
 * 2：暂时不支持动态配置，如果需要修改重跑次数 可以修改MAX_RETRY_COUNT参数
 * 3：对该class的处理器放置在AssertListener中
 * ***************************************结束*************************************
 * */
package com.Demo.Listeners.Failretry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MyRetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private final int MAX_RETRY_COUNT = 1;

    public boolean retry(ITestResult iTestResult) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }
}