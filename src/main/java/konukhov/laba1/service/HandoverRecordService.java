package konukhov.laba1.service;

import konukhov.laba1.model.HandoverRecord;
import konukhov.laba1.repository.HandoverRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HandoverRecordService {
    private final HandoverRecordRepository repository;

    public HandoverRecordService(HandoverRecordRepository repository) {
        this.repository = repository;
    }

    public List<HandoverRecord> getAll() {
        return repository.findAll();
    }

    public Optional<HandoverRecord> getById(Long id) {
        return repository.findById(id);
    }

    public HandoverRecord create(HandoverRecord record) {
        record.setDate(LocalDateTime.now().toString());
        return repository.save(record);
    }

    public Optional<HandoverRecord> update(Long id, HandoverRecord updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getClaimId() != null) {
                existing.setClaimId(updated.getClaimId());
            }
            if (updated.getConfirmed() != null) {
                existing.setConfirmed(updated.getConfirmed());
            }
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}