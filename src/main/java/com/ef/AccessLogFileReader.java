package com.ef;

import com.ef.model.AccessLog;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessLogFileReader {

    public List<AccessLog> read(String accessLogFilePath) throws IOException {
        return Files.lines(Paths.get(accessLogFilePath))
                .parallel()
                .map(AccessLog::fromAccessLogFileLine)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
