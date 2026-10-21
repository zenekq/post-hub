package com.posthub.util.repository;

import com.posthub.util.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Integer>, JpaSpecificationExecutor<ActionLog> {

    Optional<ActionLog> findByIdAndUserId(Integer id, Long userId);

    @Modifying
    @Query("UPDATE ActionLog al SET al.isRead = true WHERE al.id IN :ids")
    Integer setIsReadEqualsTrue(List<Integer> ids);

}
