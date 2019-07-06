package com.ef;

import com.ef.model.AccessLog;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class AccessLogTest {

    @Test
    public void test_access_log_creation_from_file_line() {
        AccessLog expected = expectedAccessLog();
        String fileLine = "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";
        AccessLog actual = AccessLog.fromAccessLogFileLine(fileLine);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private AccessLog expectedAccessLog() {
        AccessLog expected = new AccessLog();
        expected.setUserAgent("swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0");
        expected.setStatus(200);
        expected.setRequest("GET / HTTP/1.1");
        expected.setIp("192.168.234.82");
        expected.setTimestamp(DateUtils.parseAccessLogDate("2017-01-01 00:00:11.763"));
        return expected;
    }
}

