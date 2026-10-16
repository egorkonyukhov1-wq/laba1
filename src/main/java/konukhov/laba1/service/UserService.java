package konukhov.laba1.service;

import konukhov.laba1.model.User;
import konukhov.laba1.repository.LossReportRepository;
import konukhov.laba1.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final LossReportRepository lossReportRepository;

    public UserService(UserRepository repository, LossReportRepository lossReportRepository) {
        this.repository = repository;
        this.lossReportRepository = lossReportRepository;
    }

    public List<User> getAll() { return repository.findAll(); }
    public Optional<User> getById(Long id) { return repository.findById(id); }
    public User create(User user) { return repository.save(user); }

    public Optional<User> update(Long id, User updated) {
        return repository.findById(id).map(existing -> {
            if (updated.getName() != null) {
                existing.setName(updated.getName());
            }
            if (updated.getEmail() != null) {
                existing.setEmail(updated.getEmail());
            }
            if (updated.getPhone() != null) {
                existing.setPhone(updated.getPhone());
            }
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        if (lossReportRepository.existsByUserId(id)) return false;
        repository.deleteById(id);
        return true;
    }
}