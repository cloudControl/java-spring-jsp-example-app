package com.cloudcontrolled.sample.spring.visitcounter;

import java.io.IOException;

import com.cloudcontrolled.sample.spring.memcachier.MemcachierConnection;

public class VisitCounter {

    private static final String KEY = "count";

    private MemcachierConnection mc;

    public VisitCounter() throws IOException {
        String user = System.getenv("MEMCACHIER_USERNAME");
        String pass = System.getenv("MEMCACHIER_PASSWORD");
        String addr = System.getenv("MEMCACHIER_SERVERS");
        mc = new MemcachierConnection(user, pass, addr);
    }

    public int getVisitCount() {
        if (mc.get(KEY) == null) {
            return 0;
        } else {
            return (Integer) mc.get(KEY);
        }
    }

    public void updateVisitCount() {
        int count = getVisitCount();
        mc.set(KEY, 0, count + 1);
    }
}