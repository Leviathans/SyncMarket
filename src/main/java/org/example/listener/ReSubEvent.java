package org.example.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 重新订阅事件
 */
public class ReSubEvent extends ApplicationEvent {

    private Future future;

    public ReSubEvent(Object source, Future future) {
        super(source);
        this.future = future;
    }

    public Future getFuture() {
        return future;
    }

    public void setFuture(Future future) {
        this.future = future;
    }
}
