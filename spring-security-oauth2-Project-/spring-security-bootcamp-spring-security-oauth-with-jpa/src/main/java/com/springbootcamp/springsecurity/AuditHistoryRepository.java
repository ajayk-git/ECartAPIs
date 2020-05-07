package com.springbootcamp.springsecurity;

import org.springframework.data.repository.CrudRepository;

public interface AuditHistoryRepository extends CrudRepository<AuditHistory,Long> {
}
