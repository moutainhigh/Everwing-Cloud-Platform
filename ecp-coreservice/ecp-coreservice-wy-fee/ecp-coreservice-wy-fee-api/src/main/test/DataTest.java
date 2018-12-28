import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcCommonAccountDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcLateFeeDto;
import com.everwing.coreservice.common.wy.fee.dto.AcSpecialDetailDto;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据测试
 *
 * @author DELL shiny
 * @create 2018/6/13
 */
public class DataTest {

    @Test
    public void acCommonPrestore() {
        AcCommonAccountDetailDto commonAccountPrestore=new AcCommonAccountDetailDto();
        commonAccountPrestore.setProjectId("1013");
        commonAccountPrestore.setProjectName("testData");
        commonAccountPrestore.setBusinessTypeEnum(AcBusinessTypeEnum.PRESTORE);
        commonAccountPrestore.setDesc("通用预存500");
        commonAccountPrestore.setHouseCodeNew("222");
        commonAccountPrestore.setMoney(new BigDecimal(500));
        commonAccountPrestore.setOperator("shiny");
        commonAccountPrestore.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        List<AcCommonAccountDetailDto> list=new ArrayList<>();
        list.add(commonAccountPrestore);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void acCommonDeduction() {
        AcCommonAccountDetailDto commonAccountDeduction=new AcCommonAccountDetailDto();
        commonAccountDeduction.setProjectId("1013");
        commonAccountDeduction.setProjectName("testData");
        commonAccountDeduction.setBusinessTypeEnum(AcBusinessTypeEnum.DEDUCTION);
        commonAccountDeduction.setDeductionEnum(AcDeductionEnum.WY);
        commonAccountDeduction.setDesc("通用抵扣300");
        commonAccountDeduction.setHouseCodeNew("222");
        commonAccountDeduction.setMoney(new BigDecimal(300));
        commonAccountDeduction.setOperator("shiny");
        commonAccountDeduction.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        List<AcCommonAccountDetailDto> list=new ArrayList<>();
        list.add(commonAccountDeduction);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void acCommonRefund() {
        AcCommonAccountDetailDto commonAccountRefund=new AcCommonAccountDetailDto();
        commonAccountRefund.setProjectId("1013");
        commonAccountRefund.setProjectName("testData");
        commonAccountRefund.setBusinessTypeEnum(AcBusinessTypeEnum.REFUND);
        commonAccountRefund.setDesc("通用退费200");
        commonAccountRefund.setHouseCodeNew("222");
        commonAccountRefund.setMoney(new BigDecimal(200));
        commonAccountRefund.setOperator("shiny");
        commonAccountRefund.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        List<AcCommonAccountDetailDto> list=new ArrayList<>();
        list.add(commonAccountRefund);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void acLateFee() {
        AcLateFeeDto lateFeeDto=new AcLateFeeDto();
        lateFeeDto.setProjectId("1013");
        lateFeeDto.setProjectName("testData");
        lateFeeDto.setDesc("滞纳金200");
        lateFeeDto.setHouseCodeNew("222");
        lateFeeDto.setMoney(new BigDecimal(200));
        lateFeeDto.setOperator("shiny");
        lateFeeDto.setDelayAccountTypeEnum(AcDelayAccountTypeEnum.WY);
        lateFeeDto.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        lateFeeDto.setIsPay(0);
        List<AcLateFeeDto> list=new ArrayList<>();
        list.add(lateFeeDto);
        System.out.println(JSON.toJSONString(list));
    }
    @Test
    public void acSpecialPrestore() {
        AcSpecialDetailDto specialDetailDto=new AcSpecialDetailDto();
        specialDetailDto.setProjectId("1013");
        specialDetailDto.setProjectName("testData");
        specialDetailDto.setDesc("预存1000");
        specialDetailDto.setHouseCodeNew("222");
        specialDetailDto.setMoneyPrincipal(new BigDecimal(1000));
        specialDetailDto.setOperator("shiny");
        specialDetailDto.setAccountTypeEnum(AcSpecialAccountTypeEnum.WY);
        specialDetailDto.setBusinessTypeEnum(AcSpecialBusinessTypeEnum.SPECIAL_BUSINESS_TYPE_PRESTORE);
        specialDetailDto.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        List<AcSpecialDetailDto> list=new ArrayList<>();
        list.add(specialDetailDto);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void acSpecialDeduction() {
        AcSpecialDetailDto specialDetailDto=new AcSpecialDetailDto();
        specialDetailDto.setProjectId("1013");
        specialDetailDto.setProjectName("testData");
        specialDetailDto.setDesc("抵扣600");
        specialDetailDto.setHouseCodeNew("222");
        specialDetailDto.setMoneyPrincipal(new BigDecimal(600));
        specialDetailDto.setOperator("shiny");
        specialDetailDto.setAccountTypeEnum(AcSpecialAccountTypeEnum.WY);
        specialDetailDto.setBusinessTypeEnum(AcSpecialBusinessTypeEnum.SPECIAL_BUSINESS_TYPE_DEDUCTION);
        specialDetailDto.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        List<AcSpecialDetailDto> list=new ArrayList<>();
        list.add(specialDetailDto);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void acSpecialRefund() {
        AcSpecialDetailDto specialDetailDto=new AcSpecialDetailDto();
        specialDetailDto.setProjectId("1013");
        specialDetailDto.setProjectName("testData");
        specialDetailDto.setDesc("退费400");
        specialDetailDto.setHouseCodeNew("222");
        specialDetailDto.setMoneyPrincipal(new BigDecimal(400));
        specialDetailDto.setOperator("shiny");
        specialDetailDto.setAccountTypeEnum(AcSpecialAccountTypeEnum.WY);
        specialDetailDto.setBusinessTypeEnum(AcSpecialBusinessTypeEnum.SPECIAL_BUSINESS_TYPE_REFUND);
        specialDetailDto.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        List<AcSpecialDetailDto> list=new ArrayList<>();
        list.add(specialDetailDto);
        System.out.println(JSON.toJSONString(list));
    }
}
