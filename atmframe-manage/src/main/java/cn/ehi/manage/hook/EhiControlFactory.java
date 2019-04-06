package cn.ehi.manage.hook;

import cn.ehi.manage.config.TaskConfig;
import cn.ehi.manage.config.TestNGType;
import cn.ehi.manage.luckyframe.LuckyframeProcess;
import cn.ehi.manage.run.Run;
import cn.ehi.testng.listener.TestListener;
import luckyclient.caserun.CustomControlFactory;
import luckyclient.planapi.entity.TestTaskexcute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EhiControlFactory extends CustomControlFactory{
	private final static Logger LOG = LoggerFactory.getLogger(EhiControlFactory.class);
	private static final int APP_AUTOMATION_SERVICS_EXTYPE = 3;
	@Override
	protected ControlRunnable getControlRunnable() {
		return new EhiCarService();
	}
	static class EhiCarService implements ControlRunnable{

		@Override
		public void run(TestTaskexcute task) {
			if(APP_AUTOMATION_SERVICS_EXTYPE == task.getTestJob().getExtype()) {
				runEhiCarService(task);
			}
		}

		private void runEhiCarService(TestTaskexcute taskexcute) {
		    LOG.debug("开始测试，并发数为：" + taskexcute.getTestJob().getThreadCount());
            List<TaskConfig> taskConfigs = new ArrayList<>();
            for (int i = 0; i < taskexcute.getTestJob().getThreadCount(); i++) {
                TaskConfig taskConfig = initTaskConfig(taskexcute);
                taskConfigs.add(taskConfig);
            }
            Run.runTask(taskConfigs);
		}

		private TaskConfig initTaskConfig(TestTaskexcute taskexcute) {
            TaskConfig taskConfig = new TaskConfig();
            String taskId = String.valueOf(taskexcute.getId());
            taskConfig.setTaskName(taskexcute.getTestJob().getTaskName()+taskId);
            taskConfig.setTestNGtype(TestNGType.XML);
            taskConfig.setTestngFileName(taskexcute.getTestJob().getTestngFile());
            Map<String, Object> taskData = new HashMap<>();
            taskData.put("taskexcute", taskexcute);
            taskData.put("taskId", taskId);
            taskConfig.setTaskData(taskData);
            LuckyframeProcess luckyframeProcess = new LuckyframeProcess(taskConfig);
            TestListener testListener = new TestListener(luckyframeProcess);
            taskConfig.setListener(testListener);
            return taskConfig;
        }
	}
}
