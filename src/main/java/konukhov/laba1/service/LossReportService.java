package konukhov.laba1.service;

import konukhov.laba1.model.LossReport;
import konukhov.laba1.model.User;
import konukhov.laba1.repository.LossReportRepository;
import konukhov.laba1.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LossReportService {
    private final LossReportRepository repository;
    private final UserRepository userRepository;

    public LossReportService(LossReportRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<LossReport> getAll() { return repository.findAll(); }
    public Optional<LossReport> getById(Long id) { return repository.findById(id); }

    public LossReport create(LossReport report, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        report.setUser(user);
        return repository.save(report);
    }

    public Optional<LossReport> update(Long id, LossReport updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
            if (updated.getCategory() != null) existing.setCategory(updated.getCategory());
            if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
            return repository.save(existing);
        });
    }
    public boolean delete(Long id) {
        if (repository.existsById(id)) { repository.deleteById(id); return true; }
        return false;
    }
}