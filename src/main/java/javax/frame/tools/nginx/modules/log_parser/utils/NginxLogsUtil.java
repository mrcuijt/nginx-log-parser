package javax.frame.tools.nginx.modules.log_parser.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class NginxLogsUtil {

    public static void logs(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                List<String> strings = processLogs(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> processLogs(String line) {
        List<String> parts = new ArrayList<String>();
        // 1、以空格作为匹配项，如果当前项包含“"”向后包含至下一个“"”并对没项拼接空格
        String[] datas = line.split(" ", -2);
        for (int i = 0; i < datas.length; i++) {
            String data = datas[i];
            if (data.contains("\"")) {
                String temp = data;
                for (; ; ) {
                    if (data.indexOf("\"") != data.lastIndexOf("\"")) {
                        break;
                    }
                    if (++i < datas.length) {
                        data = datas[i];
                        temp += " " + data;
                        if (data.contains("\"")) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                parts.add(temp);
            } else if (data.contains("[")) {
                String temp = data;
                for (; ; ) {
                    if (data.indexOf("]") != data.lastIndexOf("]")) {
                        break;
                    }
                    if (++i < datas.length) {
                        data = datas[i];
                        temp += " " + data;
                        if (data.contains("]")) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                parts.add(temp);
            } else {
                parts.add(data);
            }
        }
        return parts;
    }
}
