package org.example.spring_boot_2.Task;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query("SELECT t FROM TaskEntity t WHERE t.assignedUserId = :userId AND t.status = :status")
    List<TaskEntity> findByAssignedUserIdAndStatus(@Param("userId") Long userId,
                                                   @Param("status") Status status);

    @Query("""
            SELECT t FROM TaskEntity t 
            WHERE t.creatorId = :creatorId 
            AND t.assignedUserId = :assignedUserId
            AND t.status = :status
            AND t.priority = :priority
                        """)
    List<TaskEntity> searchAllTaskByFilter(
            @Param("creatorId") Long creatorId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("status") Status status,
            @Param("priority") Priority priority,
            Pageable pageable
    );
}
