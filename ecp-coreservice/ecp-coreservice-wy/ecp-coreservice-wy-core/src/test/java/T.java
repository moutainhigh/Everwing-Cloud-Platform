import com.everwing.coreservice.common.utils.CommonUtils;
import org.junit.Test;

import java.util.Date;

/**
 * @author DELL shiny
 * @create 2018/1/10
 */
public class T {


    @Test
    public void name() throws Exception {
        Date d = CommonUtils.getDate("2018-01-04 15:20:10.0","yyyy-MM-dd HH:mm:ss");
        String dStr = CommonUtils.getDateStr(d);
        System.out.println(dStr);


    }
}
