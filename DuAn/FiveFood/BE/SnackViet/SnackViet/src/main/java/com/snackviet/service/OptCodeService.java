package com.snackviet.service;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

@Service
public class OptCodeService {
    private final AtomicReference<Integer> code = new AtomicReference<>(null);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public int generateCode() {
        Random random = new Random();
        int newCode = 100000 + random.nextInt(900000);
        code.set(newCode);
        // Đặt lại mã OTP sau 10 phút
        scheduleCodeReset();

        return newCode;
    }

    public Integer getCode() {
        return code.get();
    }

    // Lên lịch reset mã OTP sau 10 phút
    private void scheduleCodeReset(){
        scheduler.schedule(() -> code.set(null), 10, TimeUnit.MINUTES);
    }
}
