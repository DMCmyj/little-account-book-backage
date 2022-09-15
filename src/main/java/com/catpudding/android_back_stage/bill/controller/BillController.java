package com.catpudding.android_back_stage.bill.controller;

import com.catpudding.android_back_stage.bill.entity.BillItem;
import com.catpudding.android_back_stage.bill.entity.DayBills;
import com.catpudding.android_back_stage.bill.entity.MonthBillData;
import com.catpudding.android_back_stage.bill.entity.StatisticsParams;
import com.catpudding.android_back_stage.bill.model.ComputeDataMonth;
import com.catpudding.android_back_stage.bill.service.IBillService;
import com.catpudding.android_back_stage.common.JwtUtil;
import com.catpudding.android_back_stage.common.Result;
import com.catpudding.android_back_stage.system.entity.SysUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private IBillService iBillService;

    /**
     * 按照月份获取账单数据
     * @return
     */
    @RequestMapping(value = "/getByMonth",method = RequestMethod.GET)
    public Result<?> getBillByMonth(@RequestHeader("X-Access-Token") String token,
                                    @RequestParam(name = "year",defaultValue = "2022") Integer year,
                                    @RequestParam(name = "month",defaultValue = "5") Integer month){
        Result<MonthBillData> result = new Result<>();
        //获取用户
        SysUser user = JwtUtil.parse(token).getSysUser();
        MonthBillData monthBillData = iBillService.getAllBillByUserId(user.getId(),year,month);
        monthBillData.getData().sort(new Comparator<DayBills>() {
            @Override
            public int compare(DayBills o1, DayBills o2) {
                SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 E");
                try {
                    Date d1 = sf.parse(o1.getDate());
                    Date d2 = sf.parse(o2.getDate());
                    return d2.compareTo(d1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        result.setResult(monthBillData);
        result.setCode(200);
        return result;
    }

    @RequestMapping(value = "/addBill",method = RequestMethod.POST)
    public Result<?> addBill(@RequestHeader("X-Access-Token") String token,@RequestBody BillItem billItem){
        Result<String> result = new Result<>();
        //获取用户
        SysUser user = JwtUtil.parse(token).getSysUser();
        int row = iBillService.addBill(user.getId(),billItem);
        if(row > 0){
            result.setCode(200);
            result.setResult("添加成功");
        }else{
            result.setCode(10001);
            result.setResult("添加失败");
        }
        return result;
    }

    @RequestMapping(value = "/deleteBill",method = RequestMethod.POST)
    public Result<?> deleteBill(@RequestHeader("X-Access-Token") String token,@RequestBody BillItem billItem){
        Result<String> result = new Result<>();
        int row = iBillService.deleteBill(billItem);
        if(row > 0){
            result.setCode(200);
            result.setResult("删除成功");
        }else{
            result.setCode(10001);
            result.setResult("删除失败");
        }
        return result;
    }

    @RequestMapping(value = "/editBill",method = RequestMethod.POST)
    public Result<?> editBill(@RequestHeader("X-Access-Token") String token,@RequestBody BillItem billItem){
        Result<String> result = new Result<>();
        int row = iBillService.editBill(billItem);
        if(row > 0){
            result.setCode(200);
            result.setResult("修改成功");
        }else{
            result.setCode(10001);
            result.setResult("修改失败");
        }
        return result;
    }

    @RequestMapping(value = "/getComputeData",method = RequestMethod.POST)
    public Result<?> getComputeData(@RequestHeader("X-Access-Token") String token,@RequestBody StatisticsParams statisticsParams){
        Result<List<BillItem>> result = new Result<>();
        SysUser user = JwtUtil.parse(token).getSysUser();
        switch (statisticsParams.getType()){
            case 1:
                result.setResult(iBillService.getWeekData(user.getId(),statisticsParams.getNum()));
                result.setCode(200);
                break;
            case 2:
                result.setResult(iBillService.getMonthData(user.getId(),statisticsParams.getNum()));
                result.setCode(200);
                break;
            case 3:
                result.setResult(iBillService.getYearData(user.getId(),statisticsParams.getNum()));
                result.setCode(200);
                break;
        }
        return result;
    }

    @RequestMapping(value = "/get_month_compute_data",method = RequestMethod.POST)
    public Result<?> getBillComputeByYear(
            @RequestHeader("X-Access-Token") String token,
            @RequestBody ComputeDataMonth computeDataMonth
    ){
        Result<List<BillItem>> result = new Result<>();
        SysUser user = JwtUtil.parse(token).getSysUser();
        result.setResult(iBillService.getMonthComputeData(user.getId(),computeDataMonth.getYear(),computeDataMonth.getMonth()));
        return result;
    }
}
