package konukhov.laba1.service;

import konukhov.laba1.model.ItemClaim;
import konukhov.laba1.repository.FoundItemRepository;
import konukhov.laba1.repository.ItemClaimRepository;
import konukhov.laba1.repository.LossReportRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItemClaimService {
    private final ItemClaimRepository repository;
    private final LossReportRepository lossReportRepository;
    private final FoundItemRepository foundItemRepository;

    public ItemClaimService(ItemClaimRepository repository, LossReportRepository lossReportRepository, FoundItemRepository foundItemRepository) {
        this.repository = repository;
        this.lossReportRepository = lossReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public List<ItemClaim> getAll() { return repository.findAll(); }
    public Optional<ItemClaim> getById(Long id) { return repository.findById(id); }

    public ItemClaim create(Long lossReportId, Long foundItemId, String evidence) {
        ItemClaim claim = new ItemClaim();
        claim.setLossReport(lossReportRepository.findById(lossReportId).orElseThrow());
        claim.setFoundItem(foundItemRepository.findById(foundItemId).orElseThrow());
        claim.setEvidence(evidence);
        claim.setStatus("PENDING");
        return repository.save(claim);
    }

    public Optional<ItemClaim> update(Long id, ItemClaim updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getEvidence() != null) existing.setEvidence(updated.getEvidence());
            if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
            return repository.save(existing);
        });
    }
    public boolean delete(Long id) {
        if (repository.existsById(id)) { repository.deleteById(id); return true; }
        return false;
    }
}