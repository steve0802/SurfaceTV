package com.steve.surfacetv.datasource.util;

import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue<T> {
    private Queue<Runnable> runnableQueue = new LinkedList<>();
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void addTask(Runnable runnable) {
        this.runnableQueue.offer(runnable);
    }

    public void start() {
        Runnable runnable = this.runnableQueue.poll();
        if (runnable != null)
            runnable.run();
    }

    public void runNextTask() {
        Runnable runnable = this.runnableQueue.poll();
        if (runnable != null)
            runnable.run();
    }
}
