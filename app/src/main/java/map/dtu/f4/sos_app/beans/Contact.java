package map.dtu.f4.sos_app.beans;

public class Contact {
    private String ten, sdt;

    public Contact() {
        this.ten = "";
        this.sdt = "";
    }

    public Contact(String ten, String sdt) {
        this.ten = ten;
        this.sdt = sdt;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "ten='" + ten + '\'' +
                ", sdt='" + sdt + '\'' +
                '}';
    }
}
