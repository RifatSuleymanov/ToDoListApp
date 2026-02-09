package ru.suleymanov.service;

import ru.suleymanov.dao.RecordDao;
import ru.suleymanov.entity.Record;
import ru.suleymanov.entity.RecordStatus;
import ru.suleymanov.entity.dto.RecordsContainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RecordService {
    private final RecordDao recordDao;

    @Autowired
    public RecordService(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    @Transactional(readOnly = true)
    public RecordsContainerDto findAllRecords(String filterMode) {

        List<Record> records = recordDao.findAllRecords();

        int numberOfDoneRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.DONE).count();
        int numberOfActiveRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.ACTIVE).count();

        if (filterMode == null || filterMode.isBlank()) {
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }
        String filterModeInUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterModes = Arrays.stream(RecordStatus.values())
                .map(Enum::name)
                .toList();
        if (allowedFilterModes.contains(filterModeInUpperCase)) {
            List<Record> filterRecords =  records.stream()
                    .filter(record -> record.getStatus() == RecordStatus.valueOf(filterModeInUpperCase))
                    .toList();
            return new RecordsContainerDto(filterRecords, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    public void saveRecord(String title) {
        if(title != null && !title.isBlank()) {
            recordDao.saveRecord(new Record(title));
        }
    }

    public void updateRecordStatus(Integer id, RecordStatus newStatus) {
        recordDao.updateRecordStatus(id, newStatus);
    }

    public void deleteRecord(Integer id) {
        recordDao.deleteRecord(id);
    }
}
