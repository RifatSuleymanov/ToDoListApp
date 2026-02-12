package ru.suleymanov.entity.dto;

import lombok.Data;
import ru.suleymanov.entity.Record;

import java.util.List;

@Data
public class RecordsContainerDto {

    private final String userName;
    private final List<Record> records;
    private final int numberOfDoneRecords;
    private final int numberOfActiveRecords;

    public RecordsContainerDto(String userName, List<Record> records, int numberOfDoneRecords, int numberOfActiveRecords) {
        this.userName = userName;
        this.records = records;
        this.numberOfDoneRecords = numberOfDoneRecords;
        this.numberOfActiveRecords = numberOfActiveRecords;
    }

}
