package konukhov.laba1.model;

public class FoundItem {
    private Long id;
    private String description;
    private String category;
    private String location;
    private String status;

    public FoundItem() {}
    public FoundItem(Long id, String description, String category, String location, String status) {
        this.id = id; this.description = description; this.category = category; this.location = location; this.status = status;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}