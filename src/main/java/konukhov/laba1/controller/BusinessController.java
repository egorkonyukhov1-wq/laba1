package konukhov.laba1.controller;

import konukhov.laba1.model.FoundItem;
import konukhov.laba1.model.HandoverRecord;
import konukhov.laba1.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/business")
public class BusinessController {
    private final BusinessService service;

    public BusinessController(BusinessService service) { this.service = service; }

    @PostMapping("/claims/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveClaim(@PathVariable Long id) {
        service.approveClaim(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/claims/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectClaim(@PathVariable Long id) {
        service.rejectClaim(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reports/{id}/cancel")
    public ResponseEntity<Void> cancelReport(@PathVariable Long id) {
        service.cancelLossReport(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/claims/{id}/handover")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HandoverRecord> handover(@PathVariable Long id) {
        return ResponseEntity.ok(service.completeHandover(id));
    }

    @GetMapping("/reports/{id}/matches")
    public ResponseEntity<List<FoundItem>> getMatches(@PathVariable Long id) {
        return ResponseEntity.ok(service.findMatchesForLossReport(id));
    }
}