/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.dynamicreports.api.system.CommonApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/31
 * @author wusongti@lii.com.cn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/spring-application-context.xml","classpath*:/spring/spring-mvc.xml"})
public class CommonControllerTest {
    @Autowired
    private CommonApi commonApi;

    @Test
    public void test(){
        RemoteModelResult<MessageMap> result = commonApi.initResources();
        System.out.println(result);
    }
}
