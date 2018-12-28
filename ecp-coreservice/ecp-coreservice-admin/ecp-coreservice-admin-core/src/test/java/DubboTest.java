import com.everwing.coreservice.admin.core.service.impl.AdminTestImpl;
import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.platform.dao.mapper.extra.TestMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-application-context-admin.xml")
public class DubboTest {
	@Autowired
	AdminTestImpl adminTest;
	
	@Autowired
	TestMapper testMapper ;
	
	@Test
	public void test2() {
		PageBean pageBean = new PageBean();
		pageBean.addParam("test", 1);
		pageBean.setItemList(testMapper.testByPage(pageBean));
		System.out.println(pageBean);
	}

	@Test
	public void test1() {
		System.out.println("DubboTest.test1()" + adminTest.test("testtest"));
	}

}
