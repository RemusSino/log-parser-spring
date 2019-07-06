package com.ef;

import com.ef.model.AccessLog;
import com.ef.model.BlockedIp;
import com.ef.repo.AccessLogRepository;
import com.ef.repo.BlockedIpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class Parser implements ApplicationRunner {

    private AccessLogRepository accessLogRepository;
    private BlockedIpRepository blockedIpRepository;
    private AccessLogFileReader fileReader;
    private BatchSplitter batchSplitter;
    private ConfigurableApplicationContext context;
    private AccessFrequencyCalculator frequencyCalculator;
    private String startDate;
    private String duration;
    private long threshold;
    private String accessLogFilePath;

    @Autowired
    public Parser(AccessLogRepository accessLogRepository,
                  BlockedIpRepository blockedIpRepository,
                  AccessLogFileReader fileReader,
                  BatchSplitter batchSplitter,
                  ConfigurableApplicationContext context,
                  AccessFrequencyCalculator frequencyCalculator) {
        this.accessLogRepository = accessLogRepository;
        this.blockedIpRepository = blockedIpRepository;
        this.fileReader = fileReader;
        this.batchSplitter = batchSplitter;
        this.context = context;
        this.frequencyCalculator = frequencyCalculator;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkArgs(args);
        List<AccessLog> accessLogs = readAccessLogFile();
        saveToDbInBatches(accessLogs);
        Set<String> ips = findIpsWithFrequency(accessLogs);
        if (!CollectionUtils.isEmpty(ips))
            markIpsBlockedInDB(ips);
        closeApplication();
    }

    private void markIpsBlockedInDB(Set<String> ips) {
        if (!CollectionUtils.isEmpty(ips)) {
            List<BlockedIp> collect = ips.stream()
                    .map(ip -> BlockedIp.blockIp(ip, threshold, duration, startDate))
                    .collect(Collectors.toList());
            blockedIpRepository.saveAll(collect);
            log.info("{} marked blocked in the DB", ips.stream().collect(Collectors.joining(",")));
        }
    }

    private List<AccessLog> readAccessLogFile() throws IOException {
        log.info("Reading access logs from file");
        List<AccessLog> accessLogs = fileReader.read(accessLogFilePath);
        log.info("Finished reading {} access logs from file", accessLogs.size());
        return accessLogs;
    }

    private void checkArgs(ApplicationArguments args) {
        if (!args.containsOption("startDate")
                || !args.containsOption("duration")
                || !args.containsOption("threshold")
                || !args.containsOption("accesslog")) {
            log.error("Must provide accesslog, startDate, duration, threshold arguments");
            closeApplication();
        }

        duration = args.getOptionValues("duration").get(0);
        if (!duration.equalsIgnoreCase("hourly")
                && !duration.equalsIgnoreCase("daily")) {
            log.error("Duration argument must be: 'hourly' or 'daily'");
            closeApplication();
        }
        accessLogFilePath = args.getOptionValues("accesslog").get(0);
        if (!Paths.get(accessLogFilePath).toFile().exists()
                || !Paths.get(accessLogFilePath).toFile().isFile()) {
            log.error("Access log file path doesn't exist or is not a file");
            closeApplication();
        }
        startDate = args.getOptionValues("startDate").get(0);
        threshold = Long.valueOf(args.getOptionValues("threshold").get(0));
    }

    private Set<String> findIpsWithFrequency(List<AccessLog> accessLogs) {
        log.info("Searching for ips with more than {} accesses, {} starting with {}",
                threshold,
                duration,
                startDate);
        Set<String> ips;
        if (duration.equalsIgnoreCase("hourly"))
            ips = frequencyCalculator.countIpFrequencyHourly(startDate, threshold, accessLogs);
        else
            ips = frequencyCalculator.countIpFrequencyDaily(startDate, threshold, accessLogs);

        log.info("Ips with more than {} accesses, {} starting with {} : {} ",
                threshold,
                duration,
                startDate,
                ips.stream().collect(Collectors.joining(",")));

        return ips;
    }

    @SuppressWarnings("unchecked")
    private void saveToDbInBatches(final List<AccessLog> accessLogs) {
        log.info("Preparing to save to DB, in batches");
        long startTime = System.currentTimeMillis();
        List<List<AccessLog>> batches = batchSplitter.splitToBatches(accessLogs, 1_0000);
        batches.parallelStream().forEach(batch -> {
            accessLogRepository.saveAll(batch);
            log.info("Batch of {} saved to DB", batch.size());
        });
        long stopTime = System.currentTimeMillis();
        log.info("Finished saving all access logs to DB in {} seconds ",
                TimeUnit.MILLISECONDS.toSeconds(stopTime - startTime));
    }

    public void closeApplication() {
        context.close();
    }

    public static void main(String[] args) {
        SpringApplication.run(Parser.class, args);
    }
}
