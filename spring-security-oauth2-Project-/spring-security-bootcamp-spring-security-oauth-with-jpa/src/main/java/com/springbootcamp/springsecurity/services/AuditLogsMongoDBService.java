package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.entities.AuditLogsMongoDB;
import com.springbootcamp.springsecurity.repositories.AuditLogsMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditLogsMongoDBService {

    @Autowired
    AuditLogsMongoDBRepository auditLogsMongoRepository;

    public void saveNewObject (String tableName, Long id, String name){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setPerformedBy(name);
        auditLogs.setObjectId(id);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity(" Data is added in "+tableName+" table.");
        auditLogsMongoRepository.save(auditLogs);
    }

    public void updateObject (String tableName, Long id,String name){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setObjectId(id);
        auditLogs.setPerformedBy(name);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity("Data is updated in Table "+tableName+" at Id "+id);
        auditLogsMongoRepository.save(auditLogs);
    }

    public void deleteObject (String tableName, Long id,String name){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setObjectId(id);
        auditLogs.setPerformedBy(name);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity("Data is deleted from Table "+tableName);
        auditLogsMongoRepository.save(auditLogs);
    }

    public void readObject (String tableName, Long id,String name){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setObjectId(id);
        auditLogs.setPerformedBy(name);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity("Data is read from Table "+tableName );
        auditLogsMongoRepository.save(auditLogs);
    }

    public void readAllObjects (String tableName,String name){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setObjectId(null);
        auditLogs.setPerformedBy(name);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity("All Data is read from Table "+tableName);
        auditLogsMongoRepository.save(auditLogs);
    }


    public void activateObject (String tableName, Long id,String name){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setObjectId(id);
        auditLogs.setPerformedBy(name);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity("A object is activated from Table "+tableName+" at Id"+id);
        auditLogsMongoRepository.save(auditLogs);
    }


    public void deactivateObject (String tableName, Long id,String name){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setObjectId(id);
        auditLogs.setPerformedBy(name);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity("A object is deactivated from Table "+tableName+" at Id"+id);
        auditLogsMongoRepository.save(auditLogs);
    }
    public void registerUser (String tableName,Long id,String doneBy,Object metaData){
        AuditLogsMongoDB  auditLogs =new AuditLogsMongoDB();
        Date date=new Date();
        auditLogs.setCreatedDate(date);
        auditLogs.setPerformedBy(doneBy);
        auditLogs.setObjectId(id);
        auditLogs.setMetaData(metaData);
        auditLogs.setTableName(tableName);
        auditLogs.setActivity("A new entry is added in "+tableName+" table.");
        auditLogsMongoRepository.save(auditLogs);
    }


}
