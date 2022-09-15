package com.catpudding.android_back_stage.bill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.catpudding.android_back_stage.bill.entity.BillItem;
import com.catpudding.android_back_stage.bill.entity.MonthBillData;
import com.catpudding.android_back_stage.bill.model.ComputeDataMonth;


import java.util.List;

public interface IBillService extends IService<BillItem> {
    public MonthBillData getAllBillByUserId(String userId, Integer year, Integer month);

    public int addBill(String userId,BillItem billItem);

    public int deleteBill(BillItem billItem);
    public int editBill(BillItem billItem);

    public List<BillItem> getWeekData(String userId,int week);
    public List<BillItem> getMonthData(String userId,int month);
    public List<BillItem> getYearData(String userId,int year);

    public List<BillItem> getMonthComputeData(String userId,int year,int month);
}
