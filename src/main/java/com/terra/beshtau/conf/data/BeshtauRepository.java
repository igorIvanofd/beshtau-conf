package com.terra.beshtau.conf.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BeshtauRepository extends JpaRepository<BeshtauEntity, Long>,
        JpaSpecificationExecutor<BeshtauEntity> {
}
