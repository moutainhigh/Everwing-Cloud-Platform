/**
 * Created by wust on 2017/8/1.
 */

import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.UUID;

/**
 *
 * Function:生成权限工具
 * Reason:省去手工组装controller里面的url。注意：这种方式适合第一次配置，后面修改不适合。
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
public class GeneratorPermissionTool {
    public static void main(String[] args){
        Class cls = null;
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("请输入全限定类名:");
            String classFullName  = input.next();
            cls = Class.forName(classFullName);
            Annotation[] annotations4class = cls.getAnnotations();
            for (Annotation aClass : annotations4class) {
                if(aClass.toString().contains("RequestMapping")){
                    RequestMapping requestMapping = (RequestMapping) aClass;
                    String controllerPath = requestMapping.value()[0];
                    Method[] methods = cls.getMethods();
                    for (Method method : methods) {
                        StringBuffer operationElement = new StringBuffer("<operation").append(" id=\"").append(UUID.randomUUID()).append("\"");;
                        String name = "";
                        String desc = "";
                        String url = "";


                        Annotation[] annotations4method = method.getAnnotations();
                        for (Annotation annotation : annotations4method) {
                            if(annotation.toString().contains("RequestMapping")){
                                RequestMapping requestMapping4method = (RequestMapping) annotation;
                                String methodPath = requestMapping4method.value()[0];

                                String path = controllerPath + methodPath;

                                String reg = "(\\{\\w+\\})";
                                url = path.replaceAll(reg,"*");

                                operationElement.append(" url=\"").append(url).append("\"");
                            }

                            if(annotation.toString().contains("WyOperationLogAnnotation")){
                                WyOperationLogAnnotation wyOperationLogAnnotation = (WyOperationLogAnnotation) annotation;
                                name = wyOperationLogAnnotation.operationType().name();

                                desc = wyOperationLogAnnotation.businessName();

                                operationElement.append(" name=\"").append(name.toLowerCase().contains("insert") ? "Add" : name).append("\"");
                                operationElement.append(" desc=\"").append(desc).append("\"");
                            }
                        }
                        operationElement.append("/>");

                        if(StringUtils.isNotBlank(url)){
                            System.out.println(operationElement);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
