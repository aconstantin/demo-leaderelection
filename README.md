# Leadership Election with Spring Integration

This application demos the use of spring integration with a jdbc datasource for **leader election** and **lock acquisition**

In com.example.leaderelection.IntegrationConfiguration everything is configured.

In com.example.leaderelection.LeaderShipInfo the leadership changes are consumed and exposed as bean.

There are two ways of scheduling jobs in all application with the same schedule, but ensuring only one node will execute.

See com.example.leaderelection.ScheduledTaskOnLeader for execution only on the leader.

See com.example.leaderelection.ScheduledTaskWithLock for execution only if a lock could be acquired.

## Running the Example

## Startup Database

    docker compose up -d

## Startup Node(s) on specific port(s)

Just run the spring boot app and override the binding port by configuring the env var `SERVER_PORT` to the desired port.

    SERVER_PORT=8091 ./mvnw spring-boot:run

## Background

A `org.springframework.integration.jdbc.lock.DefaultLockRepository` is used which provides support to acquire locks, which will expire after 10 seconds if not renewed.

For selecting a leader the `org.springframework.integration.support.leader.LockRegistryLeaderInitiator` is used. This will try to re-acquire a lock on each node (in this example every 5 seconds).

If a node is not the leader it will try to (intially) acquire a lock every 1 second.

If a node dies unexpected and cannot return the lock, it will expire after 10 seconds and another node will acquire the lock 1 second later.

So the system is leaderless a maximum of 11 seconds, if the leader dies immediately after re-aquiring its lock. 