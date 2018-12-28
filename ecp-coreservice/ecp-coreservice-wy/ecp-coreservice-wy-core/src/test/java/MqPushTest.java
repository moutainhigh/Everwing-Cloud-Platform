import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-application-context.xml"})
public class MqPushTest {
	
	private static final String QUEUE_KEY = "key.junit2Wycore";
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Test
	public void sendToWyCoreListener(){
		JSONObject obj = new JSONObject();
		obj.put("companyId", "957929c6-28cb-4a21-b425-63780e740d2c");
		JSONObject cert = new JSONObject();
		cert.put("custId", "32477808-ce1b-4943-b151-c62f7ba36f56");
		Page page = new Page();
		page.setCurrentPage(1);
		page.setShowCount(3);
		cert.put("page", page);
		obj.put("cert", cert);
		this.amqpTemplate.convertAndSend(QUEUE_KEY,obj);
	}
	
}
