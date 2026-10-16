package konukhov.laba1.model;

public class LossReport {
    private Long id;
    private Long userId;
    private String description;
    private String category;
    private String status;

    public LossReport() {}
    public LossReport(Long id, Long userId, String description, String category, String status) {
        this.id = id; this.userId = userId; this.description = description; this.category = category; this.status = status;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}