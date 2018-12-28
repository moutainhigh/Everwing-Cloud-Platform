package dubboTest;

import com.everwing.coreservice.common.dto.LoginRslt;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseData;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountAndHouse;
import com.everwing.coreservice.common.service.PlatformAccountService;
import com.everwing.coreservice.platform.api.AccountAndHouseApi;
import com.everwing.coreservice.platform.api.CommonQueryApi;
import com.everwing.coreservice.platform.api.TestApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dubbo-coreservice-platform-api-config.xml")
public class DubboTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	@Qualifier("platformAccountService")
	private PlatformAccountService platformAccountService;

	@Autowired
	@Qualifier("platformAccountApi")
	private TestApi testApi;

	@Resource
	private CommonQueryApi commonQueryApi;
	
	@Resource
	private AccountAndHouseApi accountAndHouseApi;
	
	@Test
	public void test4() throws Exception {
		ArrayList<AccountAndHouseData> list = new ArrayList<AccountAndHouseData>();
		list.add(new AccountAndHouseData("mkj10022", 3, "cqtyj_admincqtyj_1000qq0qqq"));
		list.add(new AccountAndHouseData("mkj10023", 3, "cqtyj_admincqtyj_1000qq0qqq"));
		accountAndHouseApi.batchDelete(list);
//		accountAndHouseApi.batchAdd(list);
		System.out.println("DubboTest.test4()");
	}
	
	@Test
	public void queryByMethods() throws Exception {
		RemoteModelResult<List<Account>> list = commonQueryApi.queryFrom(Account.class)//
				.andGreaterThanOrEqual("accountId", 340)//
				.andLessThan("createTime", new Date())//
				.setLimit(1, 5)//
				.setOrderStr("accountId desc")//
				.listResult();
		//WHERE account_id >= ? and create_time < ? 
		System.err.println(list);
	}
	@Test
	public void queryByObject() throws Exception {
		AccountAndHouse accountAndHouse = new AccountAndHouse();
		accountAndHouse.setAccountId("347");
		accountAndHouse.setHouseId("332");
		RemoteModelResult<AccountAndHouse> result = commonQueryApi
				.queryFrom(AccountAndHouse.class, accountAndHouse).singleResult();
		//WHERE account_id = ? and house_id = ? 
		System.err.println(result);
	}
	@Test
	public void queryByMethodsAndObject() throws Exception {
		Account account = new Account();
		account.setType(1);
		RemoteModelResult<List<Account>> list = commonQueryApi.queryFrom(Account.class)//
				.andGreaterThanOrEqual("accountId", 340)//
				.andLessThan("createTime", new Date())//
				.andIsNull("parentId")//
				.listResult();
		//WHERE account_id >= ? and create_time < ? 
		System.err.println(list);
	}

	// @Test
	public void testDubbo() {
		LoginRslt rslt = platformAccountService.platformAccountLogin("sdfsdf", "2222");
		System.out.println(rslt.getAccountID());
	}

	@Test
	public void testApi() {
		System.out.println(testApi.test2("sdfdsf"));
	}
}
