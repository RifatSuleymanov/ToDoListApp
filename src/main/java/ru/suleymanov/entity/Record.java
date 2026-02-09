package ru.suleymanov.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "records")
public class Record {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "status", nullable = false)
    private RecordStatus status;

    public Record(String title) {
        this.title = title;
        status = RecordStatus.ACTIVE;
    }

    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }
}
