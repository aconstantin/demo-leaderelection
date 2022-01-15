package com.example.leaderelection;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

import javax.sql.DataSource;

@Configuration
public class IntegrationConfiguration {

    private final ApplicationEventPublisher applicationEventPublisher;

    public IntegrationConfiguration(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Bean
    public LockRepository lockRepository(final DataSource dataSource) {
        return new DefaultLockRepository(dataSource);
    }

    @Bean
    public ExpirableLockRegistry lockRegistry(final LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean
    public LockRegistryLeaderInitiator leaderInitiator(final LockRegistry lockRegistry) {
        final var lockRegistryLeaderInitiator = new LockRegistryLeaderInitiator(lockRegistry);
        lockRegistryLeaderInitiator.setHeartBeatMillis(5000L);
        lockRegistryLeaderInitiator.setBusyWaitMillis(1000L);
        lockRegistryLeaderInitiator.setApplicationEventPublisher(applicationEventPublisher);
        return lockRegistryLeaderInitiator;
    }
}