package konukhov.laba1.controller;

import konukhov.laba1.model.LossReport;
import konukhov.laba1.service.LossReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/loss-reports")
public class LossReportController {
    private final LossReportService service;

    public LossReportController(LossReportService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<LossReport>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LossReport> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LossReport> create(@RequestBody LossReport report) {
        LossReport created = service.create(report);
        return ResponseEntity.created(URI.create("/api/loss-reports/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LossReport> update(@PathVariable Long id, @RequestBody LossReport report) {
        return service.update(id, report).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
}