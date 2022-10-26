package com.yyh.demo.mapper.tdengine;

import com.yyh.demo.dto.SearchConditionDto;
import com.yyh.demo.entity.MonitorPointData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 喻云虎
 * @since 2022-08-09
 */
public interface MonitorPointDataMapper extends BaseMapper<MonitorPointData> {



    /**
     * 插入数据：以超级表为模板，如果子表不存在，则自动创建
     * @param monitorPointData 数据
     * @return 返回插入数据条数
     */
    int insertBatch(@Param("list") List<MonitorPointData> monitorPointData);

    /**
     * 单表查询 和 聚合查询
     * @param conditionDto 查询条件
     * @return 返回查询结果，不包含标签
     */
    List<MonitorPointData> selectByConditions(SearchConditionDto conditionDto);

    int insertBatch2(@Param("list") List<MonitorPointData> monitorPointData);

    List<MonitorPointData> selectFirstByConditions(SearchConditionDto searchConditionDto);
    List<MonitorPointData> selectLastByConditions(SearchConditionDto searchConditionDto);

//    MonitorPointData selectFirst();
}
