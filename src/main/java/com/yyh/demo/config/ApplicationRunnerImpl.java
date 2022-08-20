package com.yyh.demo.config;

import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.mapper.mysql.MonitorPointInfomationMapper;
import com.yyh.demo.utils.DynamicScheduledTasks;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private MonitorPointInfomationMapper infomationMapper;
    @Autowired
    private DynamicScheduledTasks dynamicScheduledTasks;

    @Override
    public void run(ApplicationArguments args) {
        List<MonitorPointInfo> pointInfos = infomationMapper.getPointInfomation();
        dynamicScheduledTasks.saveIntoMemory(pointInfos);
    }
}
