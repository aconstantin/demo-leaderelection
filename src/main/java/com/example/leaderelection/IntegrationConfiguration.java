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
        DefaultLockRepository defaultLockRepository = new DefaultLockRepository(dataSource);

        /*
         the time (in milliseconds) to expire dead locks

         DefaultLockRepository.DEFAULT_TTL is 10 seconds
         */
        // defaultLockRepository.setTimeToLive(DefaultLockRepository.DEFAULT_TTL);
        return defaultLockRepository;
    }

    @Bean
    public ExpirableLockRegistry lockRegistry(final LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean
    public LockRegistryLeaderInitiator leaderInitiator(final LockRegistry lockRegistry) {
        final var lockRegistryLeaderInitiator = new LockRegistryLeaderInitiator(lockRegistry);

        /*
        Time in milliseconds to wait in between attempts to re-acquire the lock, once it is held.
        The heartbeat time has to be less than the remote lock expiry period, if there is one,
        otherwise other nodes can steal the lock while we are sleeping here.
         */
        // is less than DefaultLockRepository.DEFAULT_TTL which is 10 seconds
        lockRegistryLeaderInitiator.setHeartBeatMillis(5000L); // default would be 500ms

        /*
         Time in milliseconds to wait in between attempts to acquire the lock, if it is not held.

         The longer this is, the longer the system can be leaderless, if the leader dies.

         If a leader dies without releasing its lock, the system might still have to wait for the old lock to expire,
         but after that it should not have to wait longer than the busy wait time to get a new leader.
         */
        lockRegistryLeaderInitiator.setBusyWaitMillis(1000L);// default would be 50ms

        lockRegistryLeaderInitiator.setApplicationEventPublisher(applicationEventPublisher);
        return lockRegistryLeaderInitiator;
    }
}