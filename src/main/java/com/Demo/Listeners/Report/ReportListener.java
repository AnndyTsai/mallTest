/**
 * create by anndy 2019
 *
 * 实现IReporter,ITestListener两个TestNG的接口
 *
 * @IReporter 报告
 * @ITestListener 重要是方便用户添加测试用例名称 testcase中需要添加private成员变量name来赋值
 *
 * template为html模板
 *
 * testcase引入listener方式为
 *  todo 方法一：@Listeners({ReportListener.class})
 *  todo 方法二：也可以使用testng.xml的<listeners><listeners/>引入
 *
 * ***************************************备注*************************************
 * 1：该监听器只支持静态html数据展示，不支持动态数据
 * 2：报告名称参数传入 支持Jenkins带入参数，但是需要对测试类方法做一点小小的改动
 * 3：如果需要支持数据记录在服务端，需要使用ReportListenerWithserver
 * 		ReportListenerWithserver支持静态html+服务端数据存储
 * ***************************************结束*************************************
 * */
package com.Demo.Listeners.Report;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

public class ReportListener implements IReporter, ITestListener {

	private String path = System.getProperty("user.dir") + File.separator + "report.html";

	private String templatePath = System.getProperty("user.dir") + File.separator + "template";

	private int testsPass = 0;

	private int testsFail = 0;

	private int testsSkip = 0;

	private String beginTime;

	private long totalTime;

	private String name;

	private String reportName;

	private String tester;

	private int failRetry;

	/**
	 * ITestListener的override方法
	 *
	 * 如果没有添加private string 成员变量 则系统默认按照时间赋值测试用例名称
	 */

	public void onTestStart(ITestResult result) {

		try {
			Field field1 = result.getInstance().getClass().getDeclaredField("testCaseName");
			Field field2 = result.getInstance().getClass().getDeclaredField("testReportName");
			Field field3 = result.getInstance().getClass().getDeclaredField("tester");
			// 测试用例名称
			field1.setAccessible(true);
			name = (String) field1.get(result.getInstance());
			// 测试报告名称
			field2.setAccessible(true);
			reportName = (String) field2.get(result.getInstance());
			// 测试报告执行人
			field3.setAccessible(true);
			tester = (String) field3.get(result.getInstance());
		} catch (Exception e) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			name = formatter.format(System.currentTimeMillis());
		}
	}

	public ReportListener() {

	}

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		List<ITestResult> list = new ArrayList<ITestResult>();
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (ISuiteResult suiteResult : suiteResults.values()) {
				ITestContext testContext = suiteResult.getTestContext();
				IResultMap passedTests = testContext.getPassedTests();
				testsPass = testsPass + passedTests.size();
				IResultMap failedTests = testContext.getFailedTests();
				testsFail = testsFail + failedTests.size();
				IResultMap skippedTests = testContext.getSkippedTests();
				testsSkip = testsSkip + skippedTests.size();
				IResultMap failedConfig = testContext.getFailedConfigurations();
				list.addAll(this.listTestResult(passedTests));
				list.addAll(this.listTestResult(failedTests));
				list.addAll(this.listTestResult(skippedTests));
				list.addAll(this.listTestResult(failedConfig));
			}
		}
		this.sort(list);
		try {
			this.outputResult(list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<ITestResult> listTestResult(IResultMap resultMap) {
		Set<ITestResult> results = resultMap.getAllResults();
		return new ArrayList<ITestResult>(results);
	}

	private void sort(List<ITestResult> list) {
		Collections.sort(list, new Comparator<ITestResult>() {
			public int compare(ITestResult r1, ITestResult r2) {
				if (r1.getStartMillis() > r2.getStartMillis()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}

	private void outputResult(List<ITestResult> list) throws FileNotFoundException {
		try {
			List<ReportInfo> listInfo = new ArrayList<ReportInfo>();
			int index = 0;
			for (ITestResult result : list) {
				String tn = result.getTestContext().getCurrentXmlTest().getParameter("testCase");
				if (index == 0) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					beginTime = formatter.format(new Date(result.getStartMillis()));
					index++;
				}
				long spendTime = result.getEndMillis() - result.getStartMillis();
				totalTime += spendTime;
				String status = this.getStatus(result.getStatus());
				List<String> log = Reporter.getOutput(result);
				for (int i = 0; i < log.size(); i++) {
					log.set(i, this.toHtml(log.get(i)));
				}
				Throwable throwable = result.getThrowable();
				if (throwable != null) {
					log.add(this.toHtml(throwable.toString()));
					StackTraceElement[] st = throwable.getStackTrace();
					for (StackTraceElement stackTraceElement : st) {
						log.add(this.toHtml("    " + stackTraceElement));
					}
				}
				ReportInfo info = new ReportInfo();
				info.setName(tn);
				info.setSpendTime(spendTime + "ms");
				info.setStatus(status);
				info.setClassName(result.getInstanceName());
				info.setMethodName(result.getName());
				info.setDescription(result.getMethod().getDescription());
				info.setLog(log);
				listInfo.add(info);
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("reportName", reportName);
			result.put("tester", tester);
			result.put("testName", name);
			result.put("testPass", testsPass);
			result.put("testFail", testsFail);
			result.put("testSkip", testsSkip);
			result.put("testAll", testsPass + testsFail + testsSkip);
			result.put("beginTime", beginTime);
			result.put("totalTime", totalTime + "ms");
			result.put("testResult", listInfo);
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

			// 判断获取到的reportName是否为null 如果不为null就修改path
			if (reportName != null && !reportName.equals("")) {

				path = System.getProperty("user.dir") + File.separator + reportName + ".html";
			}
			String template = this.read(templatePath);
			BufferedWriter output = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8"));
			template = template.replaceFirst("\\$\\{resultData\\}", Matcher.quoteReplacement(gson.toJson(result)));
			output.write(template);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getStatus(int status) {
		String statusString = null;
		switch (status) {
		case 1:
			statusString = "成功";
			break;
		case 2:
			statusString = "失败";
			break;
		case 3:
			statusString = "跳过";
			break;
		default:
			break;
		}
		return statusString;
	}

	private String toHtml(String str) {
		if (str == null) {
			return "";
		} else {
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
			str = str.replaceAll(" ", "&nbsp;");
			str = str.replaceAll("\n", "<br>");
			str = str.replaceAll("\"", "\\\\\"");
		}
		return str;
	}

	public static class ReportInfo {

		private String name;

		private String className;

		private String methodName;

		private String description;

		private String spendTime;

		private String status;

		private List<String> log;

		private String reportName;

		private int failRetry;

		private String tester;

		public String getTester() {
			return tester;
		}

		public void setTester(String tester) {
			this.tester = tester;
		}

		public int getFailRetry() {
			return failRetry;
		}

		public void setFailRetry(int failRetry) {
			this.failRetry = failRetry;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getSpendTime() {
			return spendTime;
		}

		public void setSpendTime(String spendTime) {
			this.spendTime = spendTime;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public List<String> getLog() {
			return log;
		}

		public void setLog(List<String> log) {
			this.log = log;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	private String read(String path) {
		File file = new File(path);
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		try {
			is = new FileInputStream(file);
			int index = 0;
			byte[] b = new byte[1024];
			while ((index = is.read(b)) != -1) {
				sb.append(new String(b, 0, index));
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
