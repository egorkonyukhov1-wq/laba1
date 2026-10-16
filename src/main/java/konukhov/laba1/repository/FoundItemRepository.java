package konukhov.laba1.repository;

import konukhov.laba1.model.FoundItem;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FoundItemRepository {
    private final Map<Long, FoundItem> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<FoundItem> findAll() { return new ArrayList<>(storage.values()); }
    public Optional<FoundItem> findById(Long id) { return Optional.ofNullable(storage.get(id)); }
    public FoundItem save(FoundItem item) {
        if (item.getId() == null) item.setId(idGenerator.getAndIncrement());
        storage.put(item.getId(), item);
        return item;
    }
    public void deleteById(Long id) { storage.remove(id); }
    public boolean existsById(Long id) { return storage.containsKey(id); }
}