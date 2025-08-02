package dev.eposs.elementsutils.feature.excaliburtimer;

import dev.eposs.elementsutils.api.timer.ExcaliburTimerApi;
import dev.eposs.elementsutils.util.TimerUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;


public class ExcaliburTimerData {

    private String next_user;
    private String time;
    
    private static final AtomicReference<ExcaliburTimerData> INSTANCE = new AtomicReference<>(new ExcaliburTimerData());

    private static Instant lastUpdate = Instant.MIN;
    
    public static void startUpdateTimers() {
        new Timer("ExcaliburTimerData Update Timer").scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateData();
            }
        }, 0, Duration.ofHours(1).toMillis());
    }
    
    public static void updateData() {
        // Only update data every 10 seconds to prevent spamming the API
        if (lastUpdate.isAfter(Instant.now().minusSeconds(10))) return;

        Thread.ofVirtual().name("ExcaliburTimerData Update Thread").start(() -> {
            lastUpdate = Instant.now();
            ExcaliburTimerData data = new ExcaliburTimerApi().getTimerData();
            if (data != null) {
                INSTANCE.set(data);
            }
        });
    }
    
    public static ExcaliburTimerData getInstance() {
        return INSTANCE.get();
    }
    
    public String getNextUser() {
        return next_user;
    }

    public ZonedDateTime getTime() {
        return TimerUtil.parseTime(time);
    }
}
