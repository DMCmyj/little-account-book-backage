package com.catpudding.android_back_stage.bill.entity;

import java.util.List;

public class MonthBillData {
    private Double month_pay_money = 0d;
    private Double month_get_money = 0d;
    private List<DayBills> data;

    public List<DayBills> getData() {
        return data;
    }

    public void setData(List<DayBills> data) {
        this.data = data;
    }

    public Double getMonth_pay_money() {
        return month_pay_money;
    }

    public void setMonth_pay_money(Double month_pay_money) {
        this.month_pay_money = month_pay_money;
    }

    public Double getMonth_get_money() {
        return month_get_money;
    }

    public void setMonth_get_money(Double month_get_money) {
        this.month_get_money = month_get_money;
    }
}
