package com.yyh.demo.utils;

import com.yyh.demo.commons.Vars;
import com.yyh.demo.entity.MonitorPointData;
import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.mapper.mysql.MonitorPointInfomationMapper;
import com.yyh.demo.mapper.tdengine.TableManagementMapper;
import com.yyh.demo.service.IMonitorPointDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@EnableScheduling
@Component
public class WriteDataToTDengine implements SchedulingConfigurer {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private MonitorPointInfomationMapper infomationMapper;
    @Autowired
    private TableManagementMapper tableManagementMapper;
    @Autowired
    private IMonitorPointDataService monitorPointDataService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
            //1.添加任务内容(Runnable)
            () -> {
                System.out.println("===执行写入任务：" + dateFormat.format(new Date()));
                saveBatch();
                System.out.println("===完成一批写入任务：" + dateFormat.format(new Date()));
            },
            //2.设置执行周期(Trigger)
            triggerContext -> {
                System.out.println("===获取任务执行周期===");

                //从数据库获取执行周期
                 String cron = "";
//                         = qrtzCronMapper.getCron();
                //合法性校验.
                if (StringUtils.isEmpty(cron)) {
                    cron = "0 0/1 * * * ?";
                }
                //2.3 返回执行周期(Date)
                return new CronTrigger(cron).nextExecutionTime(triggerContext);
            }
        );
    }
    public void saveBatch() {
        List<MonitorPointData> monitorPointDataList = new ArrayList<>();
        if (Vars.pointInfos.size() == 0) return;
        for (String key : Vars.pointInfos.keySet()) {
            MonitorPointInfo info = Vars.pointInfos.get(key);
            MonitorPointData data = new MonitorPointData();
            data.setStName("st_" + info.getRegion());  // 四码--超级表
            data.setTName("t_" + key);  // 测点ID
            data.setCollectedTime(Vars.dateFormat.format(new Date()));
            data.setDataValue(Math.random());
            data.setTagName(info.getTagName());
            data.setDepartment(info.getDepartment());  // 厂部
            data.setProductionLine(info.getProdutionLine());
            data.setRegion(info.getRegion());
            data.setCode9(info.getEquipmentCode().substring(0,9));
            monitorPointDataList.add(data);
        }
        monitorPointDataService.saveBatch(monitorPointDataList);
    }
}
