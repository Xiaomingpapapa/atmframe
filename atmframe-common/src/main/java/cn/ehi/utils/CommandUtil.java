package cn.ehi.utils;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 33053
 * @create 2018/12/8
 * 〈〉
 */
public class CommandUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CommandUtil.class);

    /**
     * 获取当前链接的手机的udid列表
     *
     * @return
     */
    public static List<String> getUdidList(String host) {
        //设备udid list
        List<String> udidList = null;
        if (udidList == null || udidList.isEmpty()) {
            udidList = new ArrayList<>();
            String cmdResult = exeCmd("adb -H "+ host + " devices");
            List<String> list = null;
            if (!TextUtils.isEmpty(cmdResult)){
                list = Arrays.asList(cmdResult.split("\n"));
            }
            if (1 == list.size()) {
                LOG.error("当前没有手机链接...");
                return null;
            }
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        String[] devicesInfo = list.get(i).split("\t");
                        //状态为device才是正确链接了手机，如果是offline、unauthorized
                        try {
                            if (devicesInfo[1].equals("device")) {
                                udidList.add(devicesInfo[0].trim());
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //跳过两行
                            // * daemon not running. starting it now on port 5037 *
                            // * daemon started successfully *
                            i = i + 2;
                        }

                    }
                }
            }
        }
        return udidList;
    }

    //杀死 node.exe 进程
    public static void killNodeApplication() {
        String cmd = "taskkill /f /t /im node.exe";
        exeCmd(cmd);
    }


    public static String exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            LOG.debug(sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
