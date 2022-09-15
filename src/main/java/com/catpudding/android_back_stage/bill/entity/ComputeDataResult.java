package com.catpudding.android_back_stage.bill.entity;

import java.util.ArrayList;
import java.util.List;

public class ComputeDataResult {
    private List<Double> data  = new ArrayList<>();
    private List<BillItem> ranking = new ArrayList<>();

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public List<BillItem> getRanking() {
        return ranking;
    }

    public void setRanking(List<BillItem> ranking) {
        this.ranking = ranking;
    }
}
