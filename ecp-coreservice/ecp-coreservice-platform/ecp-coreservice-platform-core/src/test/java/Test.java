import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.service.AccountService;
import com.everwing.coreservice.common.platform.service.CommonService;
import com.everwing.coreservice.common.platform.service.IdGenService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhugeruifei on 17/3/27.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring/spring-application-context.xml")
public class Test {

//    @Autowired
//    @Qualifier("platformAccountService")
//    private PlatformAccountService platformAccountService;
    
    @Autowired
    private CommonService commonService;
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private IdGenService idGenService;
    
    @org.junit.Test
    public void test4(){
    	RemoteModelResult<Account> account = accountService.queryMaxAccountName(3);
    	System.out.println(account.getModel());
    }
    
    @org.junit.Test
    public void test3(){
    	Account account = new Account();
    	account.setAccountId("13");
    	account.setAccountName("monkong");
    }

    @org.junit.Test
    public void testGetMaxId(){
        System.out.println(idGenService.queryMaxIdByType(3));
    }
    
//    @org.junit.Test
//    public void test(){
//        platformAccountService.platformAccountLogin("fsfsdf","113123");
//    }
    public static void main(String[] args) {
        Account account=new Account();
        account.setMobile("1111");
        account.setPassword("12324");
        Account account1=new Account();
        account1.setAccountId("dddd");
        account1.setAccountName("ccc");
        System.out.println(JSONObject.toJSONString(account, SerializerFeature.MapSortField,SerializerFeature.WriteMapNullValue));
        System.out.println(JSONObject.toJSONString(account1, SerializerFeature.MapSortField,SerializerFeature.WriteMapNullValue));
    }
}
