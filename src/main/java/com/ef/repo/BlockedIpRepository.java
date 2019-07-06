package com.ef.repo;

import com.ef.model.BlockedIp;
import org.springframework.data.repository.CrudRepository;

public interface BlockedIpRepository extends CrudRepository<BlockedIp, Integer> {
}
