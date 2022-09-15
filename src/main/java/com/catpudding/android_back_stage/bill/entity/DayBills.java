package com.catpudding.android_back_stage.bill.entity;

import java.util.List;

public class DayBills {
    private String date;
    private List<BillItem> bills;
    private Double total_pay_price = 0d;
    private Double total_get_price = 0d;

    public Double getTotal_pay_price() {
        return total_pay_price;
    }

    public void setTotal_pay_price(Double total_pay_price) {
        this.total_pay_price = total_pay_price;
    }

    public Double getTotal_get_price() {
        return total_get_price;
    }

    public void setTotal_get_price(Double total_get_price) {
        this.total_get_price = total_get_price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BillItem> getBills() {
        return bills;
    }

    public void setBills(List<BillItem> bills) {
        this.bills = bills;
    }
}
