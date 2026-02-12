package ru.suleymanov.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "records")
@Data
public class Record {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "status", nullable = false)
    private RecordStatus status;

    @ManyToOne
    @JoinColumn(name = "user_name")
    private User user;

    public Record() { }

    public Record(String title, User user) {
        this.title = title;
        this.status = RecordStatus.ACTIVE;
        this.user = user;
    }

}
