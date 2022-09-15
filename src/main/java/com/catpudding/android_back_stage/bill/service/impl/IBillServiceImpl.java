package com.catpudding.android_back_stage.bill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catpudding.android_back_stage.bill.entity.BillItem;
import com.catpudding.android_back_stage.bill.entity.DayBills;
import com.catpudding.android_back_stage.bill.entity.MonthBillData;
import com.catpudding.android_back_stage.bill.mapper.BillMapper;
import com.catpudding.android_back_stage.bill.service.IBillService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class IBillServiceImpl extends ServiceImpl<BillMapper, BillItem> implements IBillService {

    @Autowired
    private BillMapper billMapper;

    @Override
    public MonthBillData getAllBillByUserId(String userId, Integer year, Integer month) {
        Date dateStart = getStartOfDayOnMonthFirstDay(year,month);
        Date dateEnd;
        if(month == 12){
            dateEnd = getStartOfDayOnMonthFirstDay(year + 1,1);
        }else{
            dateEnd = getStartOfDayOnMonthFirstDay(year,month + 1);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<BillItem> billItems = billMapper.getBillsByUserId(userId,simpleDateFormat.format(dateStart),simpleDateFormat.format(dateEnd));

        MonthBillData monthBillData = new MonthBillData();
        monthBillData.setData(new ArrayList<>());
        for (int i = 1;i<=getDaysOfMonth(dateStart);i++){
            DayBills dayBills = findCurrentDay(dateStart,i,billItems);
            if(dayBills.getBills().size() > 0){
                monthBillData.getData().add(dayBills);
            }
        }

        for(DayBills dayBills : monthBillData.getData()){
            monthBillData.setMonth_pay_money(monthBillData.getMonth_pay_money() + dayBills.getTotal_pay_price());
            monthBillData.setMonth_get_money(monthBillData.getMonth_get_money() + dayBills.getTotal_get_price());
        }
        return monthBillData;
    }

    @Override
    public int addBill(String userId, BillItem billItem) {
//        int row =
        billItem.setUserId(userId);
        billItem.getCreateDate().setSeconds(1);
        System.out.println(billItem.getUserId());
        return billMapper.insert(billItem);
    }

    @Override
    public int deleteBill(BillItem billItem) {
        return billMapper.deleteById(billItem.getId());
    }

    @Override
    public int editBill(BillItem billItem) {
        billItem.getCreateDate().setSeconds(1);
        return billMapper.updateById(billItem);
    }

    /**
     * 获取当前周日期
     * @param week
     * @return
     */
    private ArrayList<String> getWeekDate(int week){
        ArrayList<String> dates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //获得今天是一周的第几天
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.setWeekDate(calendar.get(Calendar.YEAR),week,2);
        Date dateStart = calendar.getTime();
        calendar.setWeekDate(calendar.get(Calendar.YEAR),week,1);
        Date dateEnd = calendar.getTime();
        List<Date> dateList = getDays(dateStart,dateEnd);

        SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
        System.out.println(sf.format(dateStart));
        System.out.println(sf.format(dateEnd));
        for(Date date : dateList){
            dates.add(sf.format(date));
        }
        return dates;
    }

    /**
     * 获取两个日期之间的所有日期
     * @param
     * @param
     * @return
     */
    private static List<Date> getDays(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd) || !tempStart.after(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }


    @Override
    public List<BillItem> getWeekData(String userId,int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //获得今天是一周的第几天
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.setWeekDate(calendar.get(Calendar.YEAR),week,2);
        Date dateStart = calendar.getTime();
        dateStart.setHours(0);
        dateStart.setMinutes(0);
        dateStart.setSeconds(0);
        calendar.setWeekDate(calendar.get(Calendar.YEAR),week,1);
        Date dateEnd = calendar.getTime();
        dateEnd.setHours(23);
        dateEnd.setMinutes(59);
        dateEnd.setSeconds(59);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(dateStart));
        System.out.println(simpleDateFormat.format(dateEnd));
        List<BillItem> billItems = billMapper.getBillsByUserId(userId,simpleDateFormat.format(dateStart),simpleDateFormat.format(dateEnd));
        return billItems;
    }

    @Override
    public List<BillItem> getMonthData(String userId, int month) {
        Date dateStart = new Date();
        dateStart.setMonth(month - 1);
        dateStart.setDate(1);
        dateStart.setHours(0);
        dateStart.setMinutes(0);
        dateStart.setSeconds(0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month - 1);
        Integer dayNum = calendar.getActualMaximum(Calendar.DATE);
        Date dateEnd = new Date();
        dateEnd.setMonth(month - 1);
        dateEnd.setDate(dayNum);
        dateEnd.setHours(23);
        dateEnd.setMinutes(59);
        dateEnd.setSeconds(59);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<BillItem> billItems = billMapper.getBillsByUserId(userId,simpleDateFormat.format(dateStart),simpleDateFormat.format(dateEnd));
        return billItems;
    }

    @Override
    public List<BillItem> getYearData(String userId, int year) {
        Date dateStart = new Date();
        dateStart.setYear(year - 1900);
        dateStart.setMonth(0);
        dateStart.setDate(1);
        dateStart.setHours(0);
        dateStart.setMinutes(0);
        dateStart.setSeconds(0);

        Calendar calendar = Calendar.getInstance();
        Integer dayNum = calendar.get(Calendar.DAY_OF_MONTH);
        Date dateEnd = new Date();
        dateEnd.setYear(year - 1900);
        dateEnd.setMonth(11);
        dateEnd.setDate(31);
        dateEnd.setHours(23);
        dateEnd.setMinutes(59);
        dateEnd.setSeconds(59);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<BillItem> billItems = billMapper.getBillsByUserId(userId,simpleDateFormat.format(dateStart),simpleDateFormat.format(dateEnd));
        return billItems;
    }

    @Override
    public List<BillItem> getMonthComputeData(String userId, int year, int month) {
        Date dateStart = new Date();
        dateStart.setYear(year - 1900);
        dateStart.setMonth(month - 1);
        dateStart.setDate(1);
        dateStart.setHours(0);
        dateStart.setMinutes(0);
        dateStart.setSeconds(0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month - 1);
        Integer dayNum = calendar.getActualMaximum(Calendar.DATE);
        Date dateEnd = new Date();
        dateEnd.setYear(year-1900);
        dateEnd.setMonth(month - 1);
        dateEnd.setDate(dayNum);
        dateEnd.setHours(23);
        dateEnd.setMinutes(59);
        dateEnd.setSeconds(59);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<BillItem> billItems = billMapper.getBillsByUserId(userId,simpleDateFormat.format(dateStart),simpleDateFormat.format(dateEnd));
        return billItems;
    }

    /**
     * 查找到当天的所有数据
     * @param start
     * @param date
     * @param data
     * @return
     */
    private DayBills findCurrentDay(Date start,Integer date,List<BillItem> data){
        DayBills dayBills = new DayBills();
        List<BillItem> billItems = new ArrayList<>();
        Date dateOne = (Date) start.clone();
        dateOne.setDate(date);
        Date dateTwo = (Date) start.clone();
        dateTwo.setDate(date);
        dateTwo.setHours(23);
        dateTwo.setMinutes(59);
        dateTwo.setSeconds(59);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 E");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(BillItem billItem :data){
            if(billItem.getCreateDate().compareTo(dateOne) >= 0 && billItem.getCreateDate().compareTo(dateTwo) <= 0){
                billItems.add(billItem);
                if(billItem.getPriceType() == 0){
                    dayBills.setTotal_pay_price(dayBills.getTotal_pay_price() + billItem.getPrice());
                }else{
                    dayBills.setTotal_get_price(dayBills.getTotal_get_price() + billItem.getPrice());
                }
            }
        }

        dayBills.setBills(billItems);
        dayBills.setDate(sf.format(dateOne));
        return dayBills;
    }

    /**
     * 获取这个月的第一天的Date对象
     * @param year
     * @param month
     * @return
     */
    public static Date getStartOfDayOnMonthFirstDay(Integer year,Integer month){
        Date date = new Date();
        date.setYear(year - 1900);
        date.setMonth(month - 1);
        date.setDate(1);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    /**
     * 获取当前日期月份一共又多少天
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
