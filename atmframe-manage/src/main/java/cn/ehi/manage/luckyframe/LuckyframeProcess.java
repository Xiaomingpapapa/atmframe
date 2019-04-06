package cn.ehi.manage.luckyframe;

import cn.ehi.core.config.AppiumConfig;
import cn.ehi.core.config.AppiumConfigUtil;
import cn.ehi.core.config.Configuration;
import cn.ehi.core.testbase.TestCaseBase;
import cn.ehi.manage.ITestProcess;
import cn.ehi.manage.config.TaskConfig;
import cn.ehi.manage.status.CaseStatus;
import cn.ehi.manage.status.TaskStatus;
import cn.ehi.testng.entity.TestMethodResult;
import cn.ehi.testng.utils.TestNGUtil;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.entity.TestTaskexcute;
import luckyclient.publicclass.remoterinterface.HttpClientHelper;
import luckyclient.publicclass.remoterinterface.HttpRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.internal.TestResult;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 33053
 * @create 2018/11/20
 * 〈Luckyframe测试过程管理类〉
 */
public class LuckyframeProcess implements ITestProcess {
    private static final Logger LOG = LoggerFactory.getLogger(LuckyframeProcess.class);
    private TaskConfig config;
    private String taskId;
    private TestTaskexcute taskexcute;
    //测试方法和对应caseID的映射集合
    private Map<String,String> methodMap = new HashMap<>();
    //测试方法错误截图映射集合
    private Map<String,String> shotFileMap = new HashMap<>();
    //开子线程更新测试结果
    private ExecutorService exec;

    public LuckyframeProcess(TaskConfig taskConfig) {
        this.config = taskConfig;
        this.taskId = taskConfig.getTaskData().get("taskId").toString();
        this.taskexcute = (TestTaskexcute)taskConfig.getTaskData().get("taskexcute");
        this.exec = Executors.newFixedThreadPool(1);
    }

    @Override
    public void beforeInvocation(TestCaseBase caseBase, TestMethodResult result) {

    }

    @Override
    public void afterInvocation(TestCaseBase caseBase, TestMethodResult result) {
        ITestResult testResult = result.getTestResult();
        if(testResult == null)
            return;

        String methodPath = TestNGUtil.getTestMethodPath(testResult.getMethod());
        if(null != result.getImagePath() && 0 != result.getImagePath().length()){
            shotFileMap.put(methodPath, result.getImagePath());
        }

        //上传测试用例结果
        try{
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    String caseId = methodMap.get(methodPath);
                    LogOperation.UpdateCaseDetail(caseId, getStatus(testResult.getStatus()), null);
                    logExceptionDetail(testResult, caseId);

                    LogOperation.updateTastdetail(taskId, methodMap.size(),TaskStatus.EXECUTING.value());
                }
            });
        }catch (Exception e) {
            LOG.error("上传测试用例结果失败");
        }
    }

    @Override
    public void onStart(ISuite suite) {
        LOG.info("Luckyframe调度任务--" + config.getTaskName() + "--开始测试");
        LogOperation.AddTaskExcute(config.getTaskName(), taskexcute.getTestJob().getId(), taskId);

        //获取suite下的所有测试方法
        List<ITestNGMethod> allMethods = suite.getAllMethods();
        //获取设备信息
        Configuration conf = new Configuration();
        AppiumConfig appiumConfig = AppiumConfigUtil.getAvailableServerConfig(conf);
        for(ITestNGMethod method :allMethods){
            int caseId = LogOperation.AddCaseDetailReturnId(taskId,
                    TestNGUtil.getTestClassDescription(method) + "(" + appiumConfig.getDeviceName() + ")", "1",
                    TestNGUtil.getTestMethodDescription(method)
                    , CaseStatus.UNEXCUTE.value());
            methodMap.put(TestNGUtil.getTestMethodPath(method), caseId+"");
            LOG.info("添加用例--"+TestNGUtil.getTestMethodPath(method)+"--用例id--"+caseId);
        }
        LogOperation.updateTastdetail(taskId, allMethods.size(), TaskStatus.EXECUTING.value());

    }

    @Override
    public void onFinish(ISuite suite) {
        LOG.info("上传错误截图");
        uploadScreemShot(HttpRequest.getWebUrl() +"/logDetail/upload.do");
        LogOperation.updateTastdetail(taskId, methodMap.size());
        //释放线程池资源
        if(exec != null){
            exec.shutdownNow();
        }
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        List<ITestResult> list = new ArrayList<>();
        for (ISuite suite : suites) {

            Map<String, ISuiteResult> suiteResults = suite.getResults();
            for (ISuiteResult suiteResult : suiteResults.values()) {

                ITestContext testContext = suiteResult.getTestContext();

                IResultMap passedTests = testContext.getPassedTests();

                IResultMap failedTests = testContext.getFailedTests();
                IResultMap skippedTests = testContext.getSkippedTests();
                list.addAll(listTestResult(passedTests));
                list.addAll(listTestResult(failedTests));
                list.addAll(listTestResult(skippedTests));
            }
        }

        sort(list);
        try {
            uploadResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<ITestResult> listTestResult(IResultMap resultMap) {
        Set<ITestResult> results = resultMap.getAllResults();
        return new ArrayList<ITestResult>(results);
    }

    /**
     * 对测试结果按执行时间进行排序
     * @param list
     */
    private void sort(List<ITestResult> list) {
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                if (r1.getStartMillis() > r2.getStartMillis()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * 上传测试结果
     * @param list
     */
    private void uploadResult(List<ITestResult> list) {
        LOG.info("正在上传执行结果----------");
        for (ITestResult result : list) {
            String methodPath = TestNGUtil.getTestMethodPath(result.getMethod());

            String caseId = methodMap.get(methodPath);
            LogOperation.UpdateCaseDetail(caseId, getStatus(result.getStatus()), null);
            logExceptionDetail(result, caseId);
        }
        LogOperation.updateTastdetail(taskId, list.size());
        LOG.info("已上传执行结果------------");
    }

    /**
     * 将TestNG用例执行状态对应转换成自定义状态类型
     * @param status
     * @return
     */
    private Integer getStatus(int status) {
        switch (status) {
            case TestResult.SUCCESS:
                return CaseStatus.PASS.value();
            case TestResult.FAILURE:
                return CaseStatus.FAIL.value();
            case TestResult.SKIP:
                return CaseStatus.UNEXCUTE.value();
            default:
                return CaseStatus.UNEXCUTE.value();
        }
    }


    /**
     * 记录异常方法的堆栈信息
     * @param result
     * @param caseId
     */
    private void logExceptionDetail(ITestResult result, String caseId) {
        PrintWriter pw = null;
        StringWriter sw = new StringWriter();
        try {
            if (result.getThrowable() != null) {
                pw = new PrintWriter(sw);
                result.getThrowable().printStackTrace(pw);
                LogOperation.CaseLogDetail2(caseId, taskId, sw.toString(), "error", "ending", "");
            }
        } catch (Exception e) {
        } finally {
            if (pw != null)
                pw.close();
        }
    }

    private void uploadScreemShot(String url) {
        Map<String,Object> map = new HashMap<>();
        LOG.info("截图存放集合数量:" + shotFileMap.size());
        for(Map.Entry<String, String> entry : shotFileMap.entrySet()){
            LOG.info("准备上传图片");
            String filePath = entry.getValue();
            File file = FileUtils.getFile(filePath);
            String time = format(new Date(), "yyyy_MM_dd_HH_mm_ss_SSS");
            String imgname = Thread.currentThread().getName()+"-" +time+ "."+ FilenameUtils.getExtension(file.getName());
            String result = HttpClientHelper.httpClientPostFile(url, file, imgname, map, "utf-8");
            if(result.equals("failure")){
                LOG.info(file.getName()+"---------上传失败");
            }else{
                LOG.info(file.getName()+"---------上传成功");

                //更新报错方法的错误截图
                if(methodMap.get(entry.getKey()) == null)
                    continue;
                LogOperation.UpdateCaseDetail(methodMap.get(entry.getKey()), null, result);

            }
        }
    }

    public String format(Date date,String pattern){
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(date);
    }
}
