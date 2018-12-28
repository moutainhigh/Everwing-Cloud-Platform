package com.everwing.server.payment.wxminiprogram.vo;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description: 账单详情
 * @Date: Create in 2018-10-19 15:37
 **/

public class BillDetailVo implements Serializable {

    private BillDetailInfo_WY_BT wy = new BillDetailInfo_WY_BT();

    private BillDetailInfo_WY_BT bt = new BillDetailInfo_WY_BT ();

    private BillDetailInfo_W_E water = new BillDetailInfo_W_E ();

    private BillDetailInfo_W_E elect = new  BillDetailInfo_W_E();

    public BillDetailVo() {
    }

    public BillDetailVo(BillDetailInfo_WY_BT wy, BillDetailInfo_WY_BT bt, BillDetailInfo_W_E water, BillDetailInfo_W_E elect) {
        this.wy = wy;
        this.bt = bt;
        this.water = water;
        this.elect = elect;
    }

    public BillDetailInfo_WY_BT getWy() {
        return wy;
    }

    public void setWy(BillDetailInfo_WY_BT wy) {
        this.wy = wy;
    }

    public BillDetailInfo_WY_BT getBt() {
        return bt;
    }

    public void setBt(BillDetailInfo_WY_BT bt) {
        this.bt = bt;
    }

    public BillDetailInfo_W_E getWater() {
        return water;
    }

    public void setWater(BillDetailInfo_W_E water) {
        this.water = water;
    }

    public BillDetailInfo_W_E getElect() {
        return elect;
    }

    public void setElect(BillDetailInfo_W_E elect) {
        this.elect = elect;
    }
}
