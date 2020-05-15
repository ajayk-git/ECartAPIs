package com.springbootcamp.springsecurity.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "AuditLogs")
public class AuditLogsMongoDB {

    @Id
    String id;

    Long objectId;

    String tableName;

    String  activity;

    @CreatedDate
    Date createdDate;

    @CreatedBy
    String performedBy;


}
