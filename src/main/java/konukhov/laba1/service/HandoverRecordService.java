package konukhov.laba1.service;

import konukhov.laba1.model.HandoverRecord;
import konukhov.laba1.repository.HandoverRecordRepository;
import konukhov.laba1.repository.ItemClaimRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HandoverRecordService {
    private final HandoverRecordRepository repository;
    private final ItemClaimRepository itemClaimRepository;

    public HandoverRecordService(HandoverRecordRepository repository, ItemClaimRepository itemClaimRepository) {
        this.repository = repository;
        this.itemClaimRepository = itemClaimRepository;
    }

    public List<HandoverRecord> getAll() { return repository.findAll(); }
    public Optional<HandoverRecord> getById(Long id) { return repository.findById(id); }

    public HandoverRecord create(Long claimId, boolean confirmed) {
        HandoverRecord record = new HandoverRecord();
        record.setClaim(itemClaimRepository.findById(claimId).orElseThrow());
        record.setDate(LocalDateTime.now().toString());
        record.setConfirmed(confirmed);
        return repository.save(record);
    }

    public Optional<HandoverRecord> update(Long id, HandoverRecord updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getClaim() != null) existing.setClaim(updated.getClaim());
            existing.setConfirmed(updated.isConfirmed());
            return repository.save(existing);
        });
    }
    public boolean delete(Long id) {
        if (repository.existsById(id)) { repository.deleteById(id); return true; }
        return false;
    }
}