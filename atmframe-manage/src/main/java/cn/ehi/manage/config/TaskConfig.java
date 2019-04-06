package cn.ehi.manage.config;

import cn.ehi.manage.ITestProcess;
import org.testng.ITestNGListener;

import java.util.Map;

/**
 * @author 33053
 * @create 2018/11/21
 * 〈TestNG测试任务配置类〉
 */
public class TaskConfig {
	private String taskName;
	private TestNGType testNGtype;
	//测试用例配置文件名称
	private String testngFileName;
	//外部测试过程
    private ITestNGListener listener;
	//任务调度数据
	private Map<String,Object> taskData;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public TestNGType getTestNGtype() {
		return testNGtype;
	}

	public void setTestNGtype(TestNGType testNGtype) {
		this.testNGtype = testNGtype;
	}

	public String getTestngFileName() {
		return testngFileName;
	}

	public void setTestngFileName(String testngFileName) {
		this.testngFileName = testngFileName;
	}

    public ITestNGListener getListener() {
        return listener;
    }

    public void setListener(ITestNGListener listener) {
        this.listener = listener;
    }

    public Map<String, Object> getTaskData() {
		return taskData;
	}

	public void setTaskData(Map<String, Object> taskData) {
		this.taskData = taskData;
	}
}
