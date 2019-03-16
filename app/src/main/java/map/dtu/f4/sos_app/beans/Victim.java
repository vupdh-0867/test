package map.dtu.f4.sos_app.beans;

public class Victim extends User{
    private boolean isSeen;
    private String message;

    public Victim(){
        super();
        this.isSeen = false;
        this.message = "";
    }

    public Victim(String id, String name, Coordinate coordinate, String status, boolean isSeen, String message) {
        super(id, name, coordinate, status);
        this.isSeen = isSeen;
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
