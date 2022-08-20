package com.yyh.demo.mapper.mysql;

import com.yyh.demo.entity.MonitorPointInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MonitorPointInfomationMapper {
    @Select("SELECT equipment_code AS `equipmentCode`,\n" +
            "info.id AS point_id,\n" +
            "info.tag_name, \n" +
            "obj1.`code` AS department, \n" +
            "obj2.`code` AS  production_line, \n" +
            "obj3.`code` AS region\n" +
            "FROM monitoring_point_information AS info\n" +
            "INNER JOIN equipment ON info.equipment_id = equipment.id\n" +
            "INNER JOIN monitor_equipment_object AS obj1 ON obj1.id = equipment.department\n" +
            "INNER JOIN monitor_equipment_object AS obj2 ON obj2.id = equipment.production_line\n" +
            "INNER JOIN monitor_equipment_object AS obj3 ON obj3.id = equipment.region\n")
    List<MonitorPointInfo> getPointInfomation();
}
