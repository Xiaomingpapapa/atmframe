package cn.ehi.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * java文件操作工具类
 * 功能 压缩文件 文件复制等
 * @author wanghao
 * @version 2017-3-27
 */
public class FileUtil {
	// protected static Logger log = LoggerFactory.getLogger(FileUtil.class);
	public static String in = // 获取默认配置文件夹路径
			System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "html"
					+ File.separator;


	private static void createDirectory(String directory, String subDirectory) {
		String dir[];
		File fl = new File(directory);
		try {
			if (subDirectory == "" && fl.exists() != true) {
				fl.mkdir();
			} else if (subDirectory != "") {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (subFile.exists() == false)
						subFile.mkdir();
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 拷贝文件夹中的所有文件到另外一个文件夹
	 * 
	 * @param srcDirector
	 *            源文件夹
	 * @param desDirector
	 *            目标文件夹
	 */
	public static void copyFileWithDirector(String srcDirector, String desDirector) throws IOException {
		(new File(desDirector)).mkdirs();
		File[] file = (new File(srcDirector)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// log.debug("拷贝：" + file[i].getAbsolutePath() + "-->" +
				// desDirector + "/" + file[i].getName());
				FileInputStream input = new FileInputStream(file[i]);
				FileOutputStream output = new FileOutputStream(desDirector + "/" + file[i].getName());
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
			if (file[i].isDirectory()) {
				// log.debug("拷贝：" + file[i].getAbsolutePath() + "-->" +
				// desDirector + "/" + file[i].getName());
				copyFileWithDirector(srcDirector + "/" + file[i].getName(), desDirector + "/" + file[i].getName());
			}
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            folderPath 文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) throws Exception {
		// 删除完里面所有内容
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		// 删除空文件夹
		myFilePath.delete();
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 */
	public static boolean delAllFile(String path) throws Exception {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				// 先删除文件夹里面的文件
				delAllFile(path + "/" + tempList[i]);
				// 再删除空文件夹
				delFolder(path + "/" + tempList[i]);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 关闭一个或多个流对象
	 * 
	 * @param closeables
	 *            可关闭的流对象列表
	 * @throws IOException
	 */
	public static void close(Closeable... closeables) throws IOException {
		if (closeables != null) {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					closeable.close();
				}
			}
		}
	}

	/**
	 * 关闭一个或多个流对象
	 * 
	 * @param closeables
	 *            可关闭的流对象列表
	 */
	public static void closeQuietly(Closeable... closeables) {
		try {
			close(closeables);
		} catch (IOException e) {
			// do nothing
		}
	}
	
	/**
	 * 
	 * @param strPath 指定文件名
	 * @param endWith 指定文件后缀
	 * @return 返回指定文件下的所有指定 后缀名的所有文件
	 */
	public static List<File> getFileList(String strPath,String endWith) {
        File dir = new File(strPath);
        List<File> filelist = new ArrayList<File>();
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath(),endWith); // 获取文件绝对路径
                } else if (fileName.endsWith(endWith)) { // 判断文件名是否以endwith结尾
                    String strFileName = files[i].getAbsolutePath();
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }

        }
        return filelist;
    }
//	public static void main(String[] args) {
//		List<File> list = getFileList(XmlUtil.DEFALUT_DATA_DIR_PATH, "xml");
//		for (File file : list) {
//			System.out.println(file.getAbsolutePath());
//		}
//		
//	}
}