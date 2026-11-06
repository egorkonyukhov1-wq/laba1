package konukhov.laba1.controller;

import konukhov.laba1.model.HandoverRecord;
import konukhov.laba1.service.HandoverRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/handover-records")
public class HandoverRecordController {
    private final HandoverRecordService service;

    public HandoverRecordController(HandoverRecordService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<HandoverRecord>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<HandoverRecord> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HandoverRecord> create(@RequestBody Map<String, Object> body) {
        Long claimId = Long.valueOf(body.get("claimId").toString());
        boolean confirmed = Boolean.parseBoolean(body.get("confirmed").toString());
        HandoverRecord created = service.create(claimId, confirmed);
        return ResponseEntity.created(URI.create("/api/handover-records/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HandoverRecord> update(@PathVariable Long id, @RequestBody HandoverRecord record) {
        return service.update(id, record).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}