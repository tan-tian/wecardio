package com.borsam;

import com.hiteam.common.util.io.FileUtil;
import com.hiteam.common.util.lang.StringUtil;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

/**
 * <pre>
 * 根据客户提供的英文国际化资源文件覆盖到项目中
 * @author : Zhang zhongtao
 * @version : Ver 1.0
 * </pre>
 */
public class PropertiesTest {

    public static void main(String[] args) throws Exception {
        String root = "E:\\wk\\ht\\code\\yl\\wecardio\\src\\main\\resources\\language\\";
        //客户提供的国际化资源文件目录，与上root同结构
        String customerRoot = "C:\\Users\\86000\\Desktop\\language";

        File rootDic = new File(root);
        String[] list = rootDic.list();

        for (String proFileName : list) {
            String proRootDic = root + File.separator + proFileName + File.separator;
            String customerRootDic = customerRoot + File.separator + proFileName + File.separator;

            String filePathZhCn = proRootDic + "message_zh_CN.properties";
            String filePathEnUs = proRootDic + "message_en_US.properties";
            //客户修改过的英文国际化文件
            String filePathGl = customerRootDic + "message_en_US.properties";

            //读取中文国际化文件
            FileInputStream fileZhCnStream = FileUtil.openInputStream(new File(filePathZhCn));

            //删除同目录的英文国际化，并重新根据中文国际化文件创建新的
            File fileEnUs = new File(filePathEnUs);
            if (fileEnUs.exists()) {
                fileEnUs.delete();
            }

            File tmpFile = FileUtil.createTmpFile(fileZhCnStream, "message_en_US", "properties");
            fileEnUs = new File(filePathEnUs);
            tmpFile.renameTo(fileEnUs);

            //加载客户给的英文国际化文件
            Properties configGl = new Properties();
            configGl.load(new FileInputStream(filePathGl));

            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileEnUs));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                String key = line.replaceAll("=.*", "");
                String val = Objects.toString(configGl.getProperty(key), "");
                line = line.replaceAll("=.*", "=");

                if (StringUtil.isBlank(val) && !key.contains("#") && StringUtil.isNotBlank(line)) {
                    System.out.println(filePathEnUs.toString().replace(root, "") + "[" + key + "], 没有英文翻译,文件路径: ");
                }

                buffer.append(line).append(val).append("\n");
            }

            reader.close();
            inputStreamReader.close();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileEnUs));
            bufferedWriter.write(buffer.toString());
            bufferedWriter.close();

        }

    }

}
