package ru.suleymanov.dao;

import ru.suleymanov.entity.Record;
import ru.suleymanov.entity.RecordStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecordDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Record> findAllRecords() {
        Query query = entityManager.createQuery("SELECT r FROM Record r ORDER BY r.id ASC");
        List<Record> records = query.getResultList();
        return records;
    }

    public void saveRecord(Record record) {
        entityManager.persist(record);
    }

    public void updateRecordStatus(int id, RecordStatus newStatus) {
        Query query = entityManager.createQuery("UPDATE Record SET status = :status WHERE id = :id");
        query.setParameter("id", id);
        query.setParameter("status", newStatus);
        query.executeUpdate();
    }

    public void deleteRecord(int id) {
        Query query = entityManager.createQuery("DELETE FROM Record WHERE id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}