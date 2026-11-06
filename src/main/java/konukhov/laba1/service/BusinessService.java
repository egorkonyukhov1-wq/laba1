package konukhov.laba1.service;

import konukhov.laba1.model.*;
import konukhov.laba1.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessService {
    private final LossReportRepository lossReportRepo;
    private final ItemClaimRepository claimRepo;
    private final FoundItemRepository itemRepo;
    private final HandoverRecordRepository handoverRepo;

    public BusinessService(LossReportRepository lossReportRepo, ItemClaimRepository claimRepo, FoundItemRepository itemRepo, HandoverRecordRepository handoverRepo) {
        this.lossReportRepo = lossReportRepo;
        this.claimRepo = claimRepo;
        this.itemRepo = itemRepo;
        this.handoverRepo = handoverRepo;
    }

    @Transactional
    public void approveClaim(Long claimId) {
        ItemClaim claim = claimRepo.findById(claimId).orElseThrow();
        FoundItem item = claim.getFoundItem();
        if (!"AVAILABLE".equals(item.getStatus())) throw new RuntimeException("Item is not available");
        claim.setStatus("APPROVED");
        item.setStatus("RESERVED");
        claimRepo.save(claim);
        itemRepo.save(item);
    }

    @Transactional
    public void rejectClaim(Long claimId) {
        ItemClaim claim = claimRepo.findById(claimId).orElseThrow();
        claim.setStatus("REJECTED");
        claimRepo.save(claim);
    }

    @Transactional
    public void cancelLossReport(Long reportId) {
        LossReport report = lossReportRepo.findById(reportId).orElseThrow();
        report.setStatus("CANCELED");
        List<ItemClaim> claims = claimRepo.findByLossReportId(reportId);
        for (ItemClaim claim : claims) {
            claim.setStatus("REJECTED");
            claimRepo.save(claim);
        }
        lossReportRepo.save(report);
    }

    @Transactional
    public HandoverRecord completeHandover(Long claimId) {
        ItemClaim claim = claimRepo.findById(claimId).orElseThrow();
        if (!"APPROVED".equals(claim.getStatus())) throw new RuntimeException("Claim must be approved first");

        HandoverRecord record = new HandoverRecord();
        record.setClaim(claim);
        record.setDate(LocalDateTime.now().toString());
        record.setConfirmed(true);

        claim.setStatus("COMPLETED");
        claim.getFoundItem().setStatus("HANDED_OVER");

        claimRepo.save(claim);
        itemRepo.save(claim.getFoundItem());
        return handoverRepo.save(record);
    }

    public List<FoundItem> findMatchesForLossReport(Long reportId) {
        LossReport report = lossReportRepo.findById(reportId).orElseThrow();
        return itemRepo.findByCategory(report.getCategory());
    }
}