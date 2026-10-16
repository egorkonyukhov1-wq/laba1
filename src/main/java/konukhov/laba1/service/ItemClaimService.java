package konukhov.laba1.service;

import konukhov.laba1.model.ItemClaim;
import konukhov.laba1.repository.HandoverRecordRepository;
import konukhov.laba1.repository.ItemClaimRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemClaimService {
    private final ItemClaimRepository repository;
    private final HandoverRecordRepository handoverRecordRepository;

    public ItemClaimService(ItemClaimRepository repository, HandoverRecordRepository handoverRecordRepository) {
        this.repository = repository;
        this.handoverRecordRepository = handoverRecordRepository;
    }

    public List<ItemClaim> getAll() {
        return repository.findAll();
    }

    public Optional<ItemClaim> getById(Long id) {
        return repository.findById(id);
    }

    public ItemClaim create(ItemClaim claim) {
        return repository.save(claim);
    }

    public Optional<ItemClaim> update(Long id, ItemClaim updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getLossReportId() != null) {
                existing.setLossReportId(updated.getLossReportId());
            }
            if (updated.getFoundItemId() != null) {
                existing.setFoundItemId(updated.getFoundItemId());
            }
            if (updated.getEvidence() != null) {
                existing.setEvidence(updated.getEvidence());
            }
            if (updated.getStatus() != null) {
                existing.setStatus(updated.getStatus());
            }
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        if (handoverRecordRepository.existsByClaimId(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}