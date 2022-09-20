package com.yyh.demo.mapper.tdengine;

import com.yyh.demo.entity.TableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface TableManagementMapper {
    /**
     * 创建数据表：主要针对超级表
     * @param tableName 超级表名
     */
//    void createSTable(@Param("tableName") String tableName);
    void createSTableData();
    int createTableData(@Param("list") List<TableInfo> tableInfos);

    /**
     * 查询超级表信息
     * @param stName null ot not null
     * @return List
     *
     */
    List<HashMap<String, String>> showStables(@Param("stName") String stName);
}
