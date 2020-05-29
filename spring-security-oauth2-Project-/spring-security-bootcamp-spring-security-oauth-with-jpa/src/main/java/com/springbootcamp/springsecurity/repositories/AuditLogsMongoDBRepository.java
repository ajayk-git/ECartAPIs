package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.AuditLogsMongoDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogsMongoDBRepository extends MongoRepository<AuditLogsMongoDB,String> {

}
