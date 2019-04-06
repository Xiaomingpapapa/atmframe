package cn.ehi.manage.run;
import cn.ehi.manage.config.TaskConfig;
import cn.ehi.manage.config.TestNGType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;


/**
 * testng测试运行类，根据testng启动配置，启动一个testng测试
 */
public class TestNGRunnable implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(TestNGRunnable.class);
	private TaskConfig taskConfig;
	public TestNGRunnable(TaskConfig taskConfig) {
		this.taskConfig = taskConfig;
	}

	@Override
	public void run() {
		String name  = taskConfig.getTaskName();
        LOG.debug(name+":开始测试");
		TestNG testNG  = new TestNG();
		
		if(taskConfig.getTestNGtype() == TestNGType.XML){
			//设置xml文件名
			List<String> suites = new ArrayList<String>();
			suites.add(taskConfig.getTestngFileName());
			testNG.setTestSuites(suites);
		}
		//禁止testNG添加默认的listener,防止输出默认的测试报告
		testNG.setUseDefaultListeners(false);
		testNG.addListener(taskConfig.getListener());
		testNG.run();//同步阻塞

		LOG.debug(name + " : 测试结束 ，测试结果 ：" + fmtStatus(testNG.getStatus()));
	}

	private String fmtStatus(int status){
		switch (status) {
		case 0:
			return "成功";
		default:
			return "失败";
		}
	}
}
