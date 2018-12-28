import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.wy.api.cust.person.PersonCustApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dubbo-coreservice-wy-api-config.xml")
public class personCustTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	@Qualifier("personCustApi")
	private PersonCustApi personCustApi;
	
//	@Test
//	public void testDubbo(){
//		RemoteModelResult<BaseDto> result = personCustApi.getPersonCustNewByIdRestful("09841dc0-204a-41f2-a175-20a6dcee0187","客户名称");
////		String result = personCustService.getAllCust();
//		System.out.print("#########="+result);
//	}
}
