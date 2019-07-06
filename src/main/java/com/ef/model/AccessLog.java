package com.ef.model;

import com.ef.DateUtils;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = {
        @Index(name = "INDEX_TIMESTAMP", columnList = "timestamp")
})
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private LocalDateTime timestamp;
    private String ip;
    private String request;
    private int status;
    private String userAgent;

    public static AccessLog fromAccessLogFileLine(final String line) {
        if (StringUtils.isEmpty(line)) {
            throw new IllegalArgumentException("Error: access log line is empty");
        }
        String[] details = line.split("\\|");

        if (details.length != 5) {
            throw new IllegalArgumentException("Error: access log line doesn't respect the expected format");
        }

        AccessLog accessLog = new AccessLog();
        accessLog.setTimestamp(DateUtils.parseAccessLogDate(details[0]));
        accessLog.setIp(details[1]);
        accessLog.setRequest(details[2].replaceAll("\"", ""));
        accessLog.setStatus(Integer.parseInt(details[3]));
        accessLog.setUserAgent(details[4].replaceAll("\"", ""));

        return accessLog;
    }
}


