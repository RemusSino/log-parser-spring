package com.ef;

import com.ef.model.AccessLog;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AccessFrequencyCalculatorTest {

    private AccessFrequencyCalculator frequencyCalculator = new AccessFrequencyCalculator();

    @Test
    public void testCalculator() {
        Set<String> ips = frequencyCalculator.countIpFrequencyHourly("2017-01-01.00:00:11",
                1L,
                createAccessLogs());
        Assertions.assertThat(ips).containsOnly("192.168.0.2");
    }

    private static List<AccessLog> createAccessLogs() {
        AccessLog a1 = new AccessLog();
        a1.setTimestamp(DateUtils.parseAccessLogDate("2017-01-01 00:00:11.763"));
        a1.setIp("192.168.0.1");

        AccessLog a2 = new AccessLog();
        a2.setTimestamp(DateUtils.parseAccessLogDate("2017-01-01 00:00:12.763"));
        a2.setIp("192.168.0.2");

        AccessLog a3 = new AccessLog();
        a3.setTimestamp(DateUtils.parseAccessLogDate("2017-01-01 00:00:13.763"));
        a3.setIp("192.168.0.2");

        AccessLog a4 = new AccessLog();
        a4.setTimestamp(DateUtils.parseAccessLogDate("2017-01-01 12:00:13.763"));
        a4.setIp("192.168.0.1");

        return Arrays.asList(a1, a2, a3);
    }
}

