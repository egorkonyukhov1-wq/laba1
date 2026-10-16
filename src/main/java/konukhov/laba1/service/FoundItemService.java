package konukhov.laba1.service;

import konukhov.laba1.model.FoundItem;
import konukhov.laba1.repository.FoundItemRepository;
import konukhov.laba1.repository.ItemClaimRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoundItemService {
    private final FoundItemRepository repository;
    private final ItemClaimRepository itemClaimRepository;

    public FoundItemService(FoundItemRepository repository, ItemClaimRepository itemClaimRepository) {
        this.repository = repository;
        this.itemClaimRepository = itemClaimRepository;
    }

    public List<FoundItem> getAll() {
        return repository.findAll();
    }

    public Optional<FoundItem> getById(Long id) {
        return repository.findById(id);
    }

    public FoundItem create(FoundItem item) {
        return repository.save(item);
    }

    public Optional<FoundItem> update(Long id, FoundItem updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getDescription() != null) {
                existing.setDescription(updated.getDescription());
            }
            if (updated.getCategory() != null) {
                existing.setCategory(updated.getCategory());
            }
            if (updated.getLocation() != null) {
                existing.setLocation(updated.getLocation());
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
        if (itemClaimRepository.existsByFoundItemId(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}