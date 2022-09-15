package com.catpudding.android_back_stage.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catpudding.android_back_stage.bill.entity.BillItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillMapper extends BaseMapper<BillItem> {

    List<BillItem> getBillsByUserId(
            @Param("userId") String userId,
            @Param("dateStart") String dateStart,
            @Param("dateEnd") String dateEnd
            );
}
