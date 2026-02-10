package ru.suleymanov.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "records")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Record {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "status", nullable = false)
    private RecordStatus status;

}
