package com.ef.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class BlockedIp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String ip;
    private String comment;

    public static BlockedIp blockIp(String ip, long threshold, String duration, String startDate) {
        BlockedIp blockedIp = new BlockedIp();
        blockedIp.ip = ip;
        blockedIp.comment = "more than " + threshold + "requests, " + duration
                + " from " + startDate;

        return blockedIp;
    }
}
