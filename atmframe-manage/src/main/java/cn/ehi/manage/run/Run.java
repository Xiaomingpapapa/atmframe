package cn.ehi.manage.run;

import cn.ehi.manage.config.TaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 33053
 * @create 2018/12/13
 * 〈任务运行类〉
 */
public class Run {
    private static final Logger LOG = LoggerFactory.getLogger(Run.class);
    private static ExecutorService executorService;

    public static void runTask(List<TaskConfig> taskConfigs) {
        if (null == taskConfigs || taskConfigs.isEmpty()) {
            return;
        }
        if (null == executorService) {
            executorService = Executors.newFixedThreadPool(5);
        }

        taskConfigs.forEach(a -> {
            TestNGRunnable testNGRunnable = new TestNGRunnable(a);
            executorService.execute(testNGRunnable);
        });

        executorService.shutdown();
    }
}
