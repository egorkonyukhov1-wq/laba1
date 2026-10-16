package konukhov.laba1.controller;

import konukhov.laba1.model.ItemClaim;
import konukhov.laba1.service.ItemClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/item-claims")
public class ItemClaimController {
    private final ItemClaimService service;

    public ItemClaimController(ItemClaimService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<ItemClaim>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemClaim> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ItemClaim> create(@RequestBody ItemClaim claim) {
        ItemClaim created = service.create(claim);
        return ResponseEntity.created(URI.create("/api/item-claims/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemClaim> update(@PathVariable Long id, @RequestBody ItemClaim claim) {
        return service.update(id, claim).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
}