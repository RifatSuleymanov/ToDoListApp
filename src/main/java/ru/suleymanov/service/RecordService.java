package ru.suleymanov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.suleymanov.entity.Record;
import ru.suleymanov.entity.RecordStatus;
import ru.suleymanov.entity.User;
import ru.suleymanov.entity.dto.RecordsContainerDto;
import ru.suleymanov.repository.RecordRepository;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserService userService;

    @Autowired
    public RecordService(RecordRepository recordRepository, UserService userService) {
        this.recordRepository = recordRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public RecordsContainerDto findAllRecords(String filterMode) {
        User user = userService.getCurrentUser();
        Long userId = user.getId();

        List<Record> records = recordRepository.findByUserIdOrderByIdAsc(userId);

        int numberOfDoneRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.DONE).count();
        int numberOfActiveRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.ACTIVE).count();

        if (filterMode == null || filterMode.isBlank()) {
            return new RecordsContainerDto(user.getName(),records, numberOfDoneRecords, numberOfActiveRecords);
        }
        String filterModeInUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterModes = Arrays.stream(RecordStatus.values())
                .map(Enum::name)
                .toList();
        if (allowedFilterModes.contains(filterModeInUpperCase)) {
            List<Record> filterRecords = records.stream()
                    .filter(record -> record.getStatus() == RecordStatus.valueOf(filterModeInUpperCase))
                    .toList();
            return new RecordsContainerDto(user.getName(), filterRecords, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            return new RecordsContainerDto(user.getName(), records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    public void saveRecord(String title) {
        if (title != null && !title.isBlank()) {
            User user = userService.getCurrentUser();
            recordRepository.save(new Record(title, user));
        }
    }

    public void updateRecordStatus(Integer id, RecordStatus newStatus) {
        recordRepository.update(id, newStatus);
    }

    public void deleteRecord(Integer id) {
        recordRepository.deleteById(id);
    }
}
