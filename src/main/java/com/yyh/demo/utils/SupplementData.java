package com.yyh.demo.utils;

import com.yyh.demo.commons.Vars;
import com.yyh.demo.entity.MonitorPointData;
import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.mapper.mysql.MonitorPointInfomationMapper;
import com.yyh.demo.mapper.tdengine.TableManagementMapper;
import com.yyh.demo.service.IMonitorPointDataService;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
@EnableScheduling
@Component
public class SupplementData implements SchedulingConfigurer {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private MonitorPointInfomationMapper infomationMapper;
    @Autowired
    private TableManagementMapper tableManagementMapper;
    @Autowired
    private IMonitorPointDataService monitorPointDataService;
    Date currTime;
    String startTimeStr;
    String endTimeStr;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        currTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTime);
        calendar.add(Calendar.DATE, -179);
        Date startTime = null;
        try {
            startTime = dateFormat.parse(dateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(startTime);
        calendar1.add(Calendar.HOUR, 6);
        Date endTime = null;
        try {
            endTime = dateFormat.parse(dateFormat.format(calendar1.getTime()));
        } catch (ParseException e) {
            System.out.println("parse failed");
        }
        startTimeStr = dateFormat.format(startTime);
        endTimeStr = dateFormat.format(endTime);
        taskRegistrar.addTriggerTask(
            //1.添加任务内容(Runnable)
            () -> {
                System.out.println("===================="+ dateFormat.format(currTime) +"====================");
                System.out.println("===执行补充数据任务：" + startTimeStr + "--" + endTimeStr);
                try {
                    if (dateFormat.parse(endTimeStr).getTime() <= currTime.getTime()) {
                        monitorPointDataService.supplementDataByDate(startTimeStr, endTimeStr);
                    }
                    startTimeStr = endTimeStr;
                    Calendar calendarEnd = Calendar.getInstance();
                    calendarEnd.setTime(dateFormat.parse(startTimeStr));
                    calendarEnd.add(Calendar.HOUR, 6);
                    if (calendarEnd.getTime().getTime() > currTime.getTime()){
                        endTimeStr = dateFormat.format(currTime.getTime());
                    }else {
                        endTimeStr = dateFormat.format(calendarEnd.getTime());
                    }
                } catch (ParseException e) {
                    System.out.println("supplement failed!");
                }
                System.out.println("===完成补充任务：" + startTimeStr + "--" + endTimeStr);
            },
            //2.设置执行周期(Trigger)
            triggerContext -> {
                System.out.println("===获取任务执行周期===");
                currTime = new Date();
                //从数据库获取执行周期
                 String cron = "";
//                         = qrtzCronMapper.getCron();
                //合法性校验.
                if (StringUtils.isEmpty(cron)) {
                    cron = "0 0/5 * * * ?";
                }
                //2.3 返回执行周期(Date)
                return new CronTrigger(cron).nextExecutionTime(triggerContext);
            }
        );
    }
}
