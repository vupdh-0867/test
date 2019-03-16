package map.dtu.f4.sos_app.beans;

public class StringProcess {
    public static boolean notVaildNumber(String s){
        if(s.trim().isEmpty()) return true;
        String regex = "[0-9]+";
        if(s.matches(regex)) return false;
        return true;
    }
}
