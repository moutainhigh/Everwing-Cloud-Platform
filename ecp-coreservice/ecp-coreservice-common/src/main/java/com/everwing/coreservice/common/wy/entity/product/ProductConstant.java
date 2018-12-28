package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2018/12/14.
 */

/**
 *
 * Function:产品模块常量
 * Reason:
 * Date:2018/12/14
 * @author wusongti@lii.com.cn
 */
public class ProductConstant {
    /**
     * 产品类型
     */
    public static final String PRODUCTTYPE_BUILDINGLEASE = "buildingLease";
    public static final String PRODUCTTYPE_CARPARKSCARD = "carParksCard";
    public static final String PRODUCTTYPE_FIXEDCARPARK = "fixedCarPark";
    public static final String PRODUCTTYPE_ENTRANCEGUARDCARD  = "entranceGuardCard";
    public static final String PRODUCTTYPE_DECORATIONSERVICE = "decorationService";
    public static final String PRODUCTTYPE_COMMONSERVICE = "commonService";


    /**
     * 表标识
     */
    public static final String TABLE_ID_PRODUCT_DETAIL_BUILDINGLEASE = "productdetail_" + PRODUCTTYPE_BUILDINGLEASE;
    public static final String TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD = "productdetail_" + PRODUCTTYPE_CARPARKSCARD;
    public static final String TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK = "productdetail_" + PRODUCTTYPE_FIXEDCARPARK;
    public static final String TABLE_ID_PRODUCT_DETAIL_ENTRANCEGUARDCARD = "productdetail_" + PRODUCTTYPE_ENTRANCEGUARDCARD;
    public static final String TABLE_ID_PRODUCT_DETAIL_DECORATIONSERVICE = "productdetail_" + PRODUCTTYPE_DECORATIONSERVICE;
    public static final String TABLE_ID_PRODUCT_DETAIL_COMMONSERVICE = "productdetail_" + PRODUCTTYPE_COMMONSERVICE;


    /**
     * 所有产品公共字段：注释请参考t_fields表
     */
    public static final String PRODUCTDETAIL_COLUMN_PROJECT_ID = "project_id";
    public static final String PRODUCTDETAIL_COLUMN_BATCH_NO = "batch_no";
    public static final String PRODUCTDETAIL_COLUMN_CODE = "code";
    public static final String PRODUCTDETAIL_COLUMN_NAME = "name";
    public static final String PRODUCTDETAIL_COLUMN_DESCRIPTION = "description";
    public static final String PRODUCTDETAIL_COLUMN_START_TIME = "start_time";
    public static final String PRODUCTDETAIL_COLUMN_END_TIME = "end_time";
    public static final String PRODUCTDETAIL_COLUMN_MASTER_PICTURE = "master_picture";
    public static final String PRODUCTDETAIL_COLUMN_CREATER_ID = "creater_id";
    public static final String PRODUCTDETAIL_COLUMN_CREATER_NAME = "creater_name";
    public static final String PRODUCTDETAIL_COLUMN_CREATE_TIME= "create_time";
    public static final String PRODUCTDETAIL_COLUMN_MODIFY_ID = "modify_id";
    public static final String PRODUCTDETAIL_COLUMN_MODIFY_NAME = "modify_name";
    public static final String PRODUCTDETAIL_COLUMN_MODIFY_TIME = "modify_time";
    public static final String PRODUCTDETAIL_COLUMN_AVERAGE_PRICE= "average_price";
    public static final String PRODUCTDETAIL_COLUMN_TAXRATE = "taxrate";
    public static final String PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER = "is_associated_buyer";

    /**
     * 某种产品特有字段：注释请参考t_fields表
     */
    public static final String PRODUCTDETAIL_COLUMN_BUILDING_CODE = "building_code";
    public static final String PRODUCTDETAIL_COLUMN_VALUATION_TYPE = "valuation_type";
    public static final String PRODUCTDETAIL_COLUMN_VALUATION_UNIT = "valuation_unit";
    public static final String PRODUCTDETAIL_COLUMN_DEPOSIT = "deposit";
    public static final String PRODUCTDETAIL_COLUMN_STOCK = "stock";
    public static final String PRODUCTDETAIL_COLUMN_CARD_FEE = "card_fee";
    public static final String PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE = "is_associated_buyer_vehicle";
    public static final String PRODUCTDETAIL_COLUMN_ASSOCIATE_VEHICLE_NUMBER = "associate_vehicle_number";
    public static final String PRODUCTDETAIL_COLUMN_HOUSE_CODE = "house_code";
    public static final String PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW = "house_code_new";
    public static final String PRODUCTDETAIL_COLUMN_MARKET_STATE = "market_state";
    public static final String PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE = "is_calendar_month_sale";
    public static final String PRODUCTDETAIL_COLUMN_PREFERENTIAL_TYPE = "preferential_type";
    public static final String PRODUCTDETAIL_COLUMN_PRODUCT_UNIT = "product_unit";
    public static final String PRODUCTDETAIL_COLUMN_SUB_PRODUCT = "sub_product";
    public static final String PRODUCTDETAIL_COLUMN_AREA_TYPE = "area_type";


    public static final String MARKET_STATE_001 = "001";  // 未售
    public static final String MARKET_STATE_002 = "002";  // 已售
    public static final String MARKET_STATE_003 = "003";  // 空置
}
