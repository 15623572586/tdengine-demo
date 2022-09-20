package com.yyh.demo.utils;

import com.google.errorprone.annotations.Var;
import com.yyh.demo.commons.Vars;
import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.mapper.mysql.MonitorPointInfomationMapper;
import com.yyh.demo.mapper.tdengine.TableManagementMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@EnableScheduling
@Component
public class DynamicScheduledTasks implements SchedulingConfigurer {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private MonitorPointInfomationMapper infomationMapper;
    @Autowired
    private TableManagementMapper tableManagementMapper;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //定时任务的执行内容 （创建新线程的方式）
        taskRegistrar.addTriggerTask(
            //1.添加任务内容(Runnable)
            () -> {
                System.out.println("===执行定时任务：" + dateFormat.format(new Date()));
                //定时任务的执行内容 （创建新线程的方式）
                List<MonitorPointInfo> monitorPointInfos = infomationMapper.getPointInfomation();
                saveIntoMemory(monitorPointInfos);
            },
            //2.设置执行周期(Trigger)
            triggerContext -> {
                System.out.println("===获取任务执行周期===");

                //从数据库获取执行周期
                 String cron = "";
//                         = qrtzCronMapper.getCron();
                //合法性校验.
                if (StringUtils.isEmpty(cron)) {
                    cron = "0 0/30 * * * ?";
                }
                //2.3 返回执行周期(Date)
                return new CronTrigger(cron).nextExecutionTime(triggerContext);
            }
        );
    }

    public void saveIntoMemory(List<MonitorPointInfo> monitorPointInfos) {
        Vars.pointInfoList = monitorPointInfos;
//        HashMap<String, MonitorPointInfo> newPointInfos = new HashMap<>();
//        for (MonitorPointInfo info: monitorPointInfos) {
//            // 创建超级表--如果存在自动忽略
//            String code4 = info.getRegion();
//            String stName = "st_" + code4;
//            tableManagementMapper.createSTableLz();
////            tableManagementMapper.createSTable(stName);
//
//            newPointInfos.put(info.getTagName(), info);
//        }
//        Vars.pointInfos = newPointInfos;
//        System.out.println("测点数："+Vars.pointInfos.size());
    }
}
