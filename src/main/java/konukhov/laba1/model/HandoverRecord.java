package konukhov.laba1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "handover_records")
public class HandoverRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "claim_id", nullable = false)
    private ItemClaim claim;

    private String date;
    private boolean confirmed;

    public HandoverRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ItemClaim getClaim() { return claim; }
    public void setClaim(ItemClaim claim) { this.claim = claim; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
}