package com.catpudding.android_back_stage.bill.entity;

public class StatisticsParams {
    private Integer type;//1是周，2是月，3是年
    private Integer num;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
