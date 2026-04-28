package Lab03SRC;

public class Location {
    private int id;
    private String name;
    private String region;
    private double latitude;
    private double longitude;
    private String imageUrl;

    public Location(int id, String name, String region, double latitude, double longitude, String imageUrl) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRegion() { return region; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getImageUrl() { return imageUrl; }
}
