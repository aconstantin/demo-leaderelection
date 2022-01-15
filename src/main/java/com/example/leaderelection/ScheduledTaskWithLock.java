package com.example.leaderelection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

@Component
public class ScheduledTaskWithLock {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final LockRegistry lockRegistry;

    public ScheduledTaskWithLock(LockRegistry lockRegistry) {
        this.lockRegistry = lockRegistry;
    }

    @Scheduled(cron = "0/15 * * * * *")
    public void everyMinute() {
        final Lock lock = lockRegistry.obtain(getClass().getSimpleName());
        if (lock.tryLock()) {
            try {
                logger.info("Lock was acquired, executing task at {}", dateFormat.format(new Date()));
            } finally {
                lock.unlock();
            }
        } else {
            logger.info("Skipping task, because lock could not be acquired");
        }
    }

}
