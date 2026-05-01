package org.example.spring_boot_2.Task;

import org.springframework.web.bind.annotation.RequestParam;

public record TaskSearchFilter(
        Long creatorId,
        Long assignedUserId,
        Status status,
        Priority priority,
        Integer pageSize,
        Integer pageNumber
) {

}
