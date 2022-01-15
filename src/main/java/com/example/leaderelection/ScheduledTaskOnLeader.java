package com.example.leaderelection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTaskOnLeader {

    private final LeaderShipInfo leaderShipInfo;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    public ScheduledTaskOnLeader(LeaderShipInfo leaderShipInfo) {
        this.leaderShipInfo = leaderShipInfo;
    }

    @Scheduled(cron = "7/15 * * * * *")
    public void everyMinute() {
        if (leaderShipInfo.isLeader()) {
            logger.info("Current Node is leader --> executing task at {}", dateFormat.format(new Date()));
        } else {
            logger.info("Do not execute --> because current node is no leader");
        }
    }

}
