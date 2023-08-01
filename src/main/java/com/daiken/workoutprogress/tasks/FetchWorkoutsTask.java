package com.daiken.workoutprogress.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FetchWorkoutsTask {

    @Autowired
    public FetchWorkoutsTask() {
    }

    @PostConstruct
    public void init() {
    }

    @Async
    public void runAsync() {
        run();
    }

    //    @Scheduled(cron = "${tasks.FetchWorkoutsTask.cron}")
    public void runScheduled() {
        run();
    }

    private void run() {
    }
}
