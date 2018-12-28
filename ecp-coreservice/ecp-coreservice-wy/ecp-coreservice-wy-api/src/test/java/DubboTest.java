import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dubbo-coreservice-wy-api-config.xml")
public class DubboTest extends AbstractJUnit4SpringContextTests{
	
//	@Autowired
//	@Qualifier("lookupService")
//	private LookupService lookupService;
	
//	@Test
//	public void testDubbo(){
//		String result = lookupService.test("abcdef");
//		System.out.print("#########="+result);
//	}
}
