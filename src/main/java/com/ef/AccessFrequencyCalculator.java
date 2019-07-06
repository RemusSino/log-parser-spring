package com.ef;

import com.ef.model.AccessLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ef.DateUtils.parseDateArgument;

@Service
public class AccessFrequencyCalculator {

    public Set<String> countIpFrequencyHourly(final String startDateTime,
                                              long threshold,
                                              List<AccessLog> accessLogs) {
        return countIpFrequency(parseDateArgument(startDateTime),
                DateUtils.add(startDateTime, 1, ChronoUnit.HOURS),
                threshold,
                accessLogs);
    }

    public Set<String> countIpFrequencyDaily(final String startDateTime,
                                             long threshold,
                                             List<AccessLog> accessLogs) {
        return countIpFrequency(parseDateArgument(startDateTime),
                DateUtils.add(startDateTime, 1, ChronoUnit.DAYS),
                threshold,
                accessLogs);
    }

    private Set<String> countIpFrequency(LocalDateTime start,
                                         LocalDateTime end,
                                         long threshold,
                                         List<AccessLog> accessLogs) {
        //don't need the cache
        Map<String, Long> ipFrequencyMap = new HashMap<>();
        Set<String> ips = new HashSet<>();
        for (AccessLog accessLog : accessLogs) {
            if (accessLog.getTimestamp().isAfter(start)
                    && accessLog.getTimestamp().isBefore(end)) {
                Long count = ipFrequencyMap.get(accessLog.getIp());
                if (count != null) {
                    ipFrequencyMap.put(accessLog.getIp(), ++count);
                    if (count > threshold) {
                        ips.add(accessLog.getIp());
                    }
                } else {
                    ipFrequencyMap.put(accessLog.getIp(), 1L);
                }
            }
        }

        return ips;
    }
}
