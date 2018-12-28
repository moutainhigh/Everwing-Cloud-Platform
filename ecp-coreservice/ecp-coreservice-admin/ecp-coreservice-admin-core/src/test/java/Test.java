/**
 * Created by wust on 2018/8/14.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * Function:
 * Reason:
 * Date:2018/8/14
 * @author wusongti@lii.com.cn
 */

public class Test {


    public static void main(String[] args) throws Exception {
        String ddlSql = readSqlFileToString("classpath:sql/wy_ddl_function.sql");
        System.out.println(ddlSql);
        //String functionSql = readSqlFileToString("classpath:sql/wy_ddl_function.sql");
        //String dmlSql = readSqlFileToString("classpath:sql/wy_dml.sql");
    }

    private static String readSqlFileToString(String path) {
        FileInputStream is = null;
        StringBuilder stringBuilder = null;
        try {
            File file = ResourceUtils.getFile(path);
            if (file != null && file.length() != 0) {
                is = new FileInputStream(file);
                InputStreamReader streamReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if(line.startsWith("--")
                            || line.startsWith("/*")
                            || "".equals(line)){
                        continue;
                    }

                    if(path.contains("function") && line.contains("--")){
                        line = line.substring(0,line.indexOf("--"));
                    }
                    stringBuilder.append(line).append(" ");
                }
                reader.close();
                is.close();
            }
        } catch (Exception e) {
            throw new ECPBusinessException("读取sql文件【"+path+"】失败。");
        }
        return String.valueOf(stringBuilder);
    }
}
