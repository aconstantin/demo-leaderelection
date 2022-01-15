package com.example.leaderelection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class LeaderShipInfo {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private boolean leader = false;

    public boolean isLeader() {
        return leader;
    }

    @EventListener(OnGrantedEvent.class)
    public void leadershipObtained() {
        logger.info("LEADERSHIP ...  obtained            at {}",  dateFormat.format(new Date()));
        leader = true;
    }

    @EventListener(OnRevokedEvent.class)
    public void leadershipLost() {
        logger.info("LEADERSHIP ...  lost                at {}",  dateFormat.format(new Date()));
        leader = false;
    }
}
