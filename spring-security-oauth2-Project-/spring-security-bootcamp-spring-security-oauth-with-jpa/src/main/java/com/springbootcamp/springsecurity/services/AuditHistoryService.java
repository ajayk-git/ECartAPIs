package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.AuditHistory;
import com.springbootcamp.springsecurity.repositories.AuditHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditHistoryService {

    @Autowired
    AuditHistoryRepository auditRepository;


    public void saveNewObject (String tableName,Long id,String name){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setDoneBy(name);
        auditHistory.setObjectId(id);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("New Data is added in "+tableName+" table.");
        auditRepository.save(auditHistory);
    }

    public void updateObject (String tableName, Long id,String name){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setObjectId(id);
        auditHistory.setDoneBy(name);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("Data is updated in Table "+tableName+" at Id "+id);
        auditRepository.save(auditHistory);
    }

    public void deleteObject (String tableName, Long id,String name){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setObjectId(id);
        auditHistory.setDoneBy(name);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("Data is deleted from Table "+tableName);
        auditRepository.save(auditHistory);
    }

    public void readObject (String tableName, Long id,String name){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setObjectId(id);
        auditHistory.setDoneBy(name);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("Data is read from Table "+tableName );
        auditRepository.save(auditHistory);
    }

    public void readAllObjects (String tableName,String name){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setObjectId(null);
        auditHistory.setDoneBy(name);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("All Data is read from Table "+tableName);
        auditRepository.save(auditHistory);
    }


    public void activateObject (String tableName, Long id,String name){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setObjectId(id);
        auditHistory.setDoneBy(name);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("A object is activated from Table "+tableName+" at Id"+id);
        auditRepository.save(auditHistory);
    }


    public void deactivateObject (String tableName, Long id,String name){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setObjectId(id);
        auditHistory.setDoneBy(name);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("A object is deactivated from Table "+tableName+" at Id"+id);
        auditRepository.save(auditHistory);
    }
    public void registerUser (String tableName,Long id,String doneBy){
        AuditHistory auditHistory =new AuditHistory();
        Date date=new Date();
        auditHistory.setCreatedDate(date);
        auditHistory.setDoneBy(doneBy);
        auditHistory.setObjectId(id);
        auditHistory.setTableName(tableName);
        auditHistory.setEvent("A new entry is added in "+tableName+" table.");
        auditRepository.save(auditHistory);
    }



}
