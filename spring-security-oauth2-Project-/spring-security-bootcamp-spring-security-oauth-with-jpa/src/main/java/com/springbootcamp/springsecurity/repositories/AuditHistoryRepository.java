package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.AuditHistory;
import org.springframework.data.repository.CrudRepository;

public interface AuditHistoryRepository extends CrudRepository<AuditHistory,Long> {
}
