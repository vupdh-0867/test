package map.dtu.f4.sos_app.beans;

public class User {
    private String id;
    private String name;
    private String status;
    Coordinate coordinate;

    public User() {
        this.id = "";
        this.name = "";
        this.status = "";
        this.coordinate = new Coordinate();
    }

    public User(String id, String name, Coordinate coordinate, String status) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}