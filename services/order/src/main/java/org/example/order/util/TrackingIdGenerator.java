package org.example.order.util;

public class TrackingIdGenerator {
    public static String generateTrackingId() {
        return "TRC"+System.currentTimeMillis();
    }
}
