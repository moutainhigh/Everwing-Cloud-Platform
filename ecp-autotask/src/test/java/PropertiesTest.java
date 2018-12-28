import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 读取配置
 *
 * @author DELL shiny
 * @create 2018/6/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/spring-application-context.xml")
public class PropertiesTest {

    @Value("${09841dc0-204a-41f2-a175-20a6dcee0187}")
    private String company;

    @Test
    public void readConfig(){
        System.out.println(company);
    }
}
