package konukhov.laba1.controller;

import konukhov.laba1.model.FoundItem;
import konukhov.laba1.service.FoundItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {
    private final FoundItemService service;

    public FoundItemController(FoundItemService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<FoundItem>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FoundItem> create(@RequestBody FoundItem item) {
        FoundItem created = service.create(item);
        return ResponseEntity.created(URI.create("/api/found-items/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FoundItem> update(@PathVariable Long id, @RequestBody FoundItem item) {
        return service.update(id, item).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
}