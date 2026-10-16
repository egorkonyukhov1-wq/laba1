package konukhov.laba1.model;

public class HandoverRecord {
    private Long id;
    private Long claimId;
    private String date;
    private Boolean confirmed;

    public HandoverRecord() {}
    public HandoverRecord(Long id, Long claimId, String date, boolean confirmed) {
        this.id = id; this.claimId = claimId; this.date = date; this.confirmed = confirmed;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClaimId() { return claimId; }
    public void setClaimId(Long claimId) { this.claimId = claimId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}