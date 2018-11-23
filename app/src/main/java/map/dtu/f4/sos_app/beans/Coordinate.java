package map.dtu.f4.sos_app.beans;

public class Coordinate {
    private double latitude;
    private double longitude;
    private long time;

    public Coordinate() {
    }

    public Coordinate(double latitude, double longitude, long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTime() {
        return time;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

