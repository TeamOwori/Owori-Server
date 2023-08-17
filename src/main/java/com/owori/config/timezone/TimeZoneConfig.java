package com.owori.config.timezone;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {
    @PostConstruct
    public void timeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));
    }
}
