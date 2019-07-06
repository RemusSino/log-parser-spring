package com.ef;

import com.ef.model.AccessLog;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessLogFileReaderTest {

    private AccessLogFileReader fileReader = new AccessLogFileReader();

    @Test
    public void test_file_read() throws IOException {
        ClassPathResource resource = new ClassPathResource("access.log");
        List<AccessLog> readAccessLogs = fileReader.read(Paths.get(resource.getURI()).toAbsolutePath().toString());
        assertThat(readAccessLogs).hasSize(2);
        assertThat(readAccessLogs.get(0).getTimestamp()).isEqualTo(DateUtils.parseAccessLogDate("2017-01-01 00:00:11.763"));
        assertThat(readAccessLogs.get(0).getIp()).isEqualTo("192.168.234.82");
        assertThat(readAccessLogs.get(1).getTimestamp()).isEqualTo(DateUtils.parseAccessLogDate("2017-01-01 00:01:09.639"));
        assertThat(readAccessLogs.get(1).getIp()).isEqualTo("192.168.234.239");
    }
}
