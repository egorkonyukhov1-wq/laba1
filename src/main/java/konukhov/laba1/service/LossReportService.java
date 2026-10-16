package konukhov.laba1.service;

import konukhov.laba1.model.LossReport;
import konukhov.laba1.repository.ItemClaimRepository;
import konukhov.laba1.repository.LossReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LossReportService {
    private final LossReportRepository repository;
    private final ItemClaimRepository itemClaimRepository;

    public LossReportService(LossReportRepository repository, ItemClaimRepository itemClaimRepository) {
        this.repository = repository;
        this.itemClaimRepository = itemClaimRepository;
    }

    public List<LossReport> getAll() {
        return repository.findAll();
    }

    public Optional<LossReport> getById(Long id) {
        return repository.findById(id);
    }

    public LossReport create(LossReport report) {
        return repository.save(report);
    }

    public Optional<LossReport> update(Long id, LossReport updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getUserId() != null) {
                existing.setUserId(updated.getUserId());
            }
            if (updated.getDescription() != null) {
                existing.setDescription(updated.getDescription());
            }
            if (updated.getCategory() != null) {
                existing.setCategory(updated.getCategory());
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
        if (itemClaimRepository.existsByLossReportId(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}