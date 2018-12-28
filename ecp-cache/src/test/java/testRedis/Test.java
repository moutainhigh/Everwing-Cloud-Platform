package testRedis;

import com.everwing.cache.redis.SpringRedisTools;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-application-redis.xml")
public class Test {

	@Autowired
	private SpringRedisTools springRedisTools;
	
	@org.junit.Test
	public void testRedis(){
		springRedisTools.addData("zgrf", "123445");
		springRedisTools.addData("ddd", "444", 3, TimeUnit.SECONDS);
		System.out.println(springRedisTools.getByKey("zgrf"));
		System.out.println(springRedisTools.getByKey("ddd"));
	}
	
}
