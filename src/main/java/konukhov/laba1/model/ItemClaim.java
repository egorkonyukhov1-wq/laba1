package konukhov.laba1.model;

public class ItemClaim {
    private Long id;
    private Long lossReportId;
    private Long foundItemId;
    private String evidence;
    private String status;

    public ItemClaim() {}
    public ItemClaim(Long id, Long lossReportId, Long foundItemId, String evidence, String status) {
        this.id = id; this.lossReportId = lossReportId; this.foundItemId = foundItemId; this.evidence = evidence; this.status = status;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLossReportId() { return lossReportId; }
    public void setLossReportId(Long lossReportId) { this.lossReportId = lossReportId; }
    public Long getFoundItemId() { return foundItemId; }
    public void setFoundItemId(Long foundItemId) { this.foundItemId = foundItemId; }
    public String getEvidence() { return evidence; }
    public void setEvidence(String evidence) { this.evidence = evidence; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}