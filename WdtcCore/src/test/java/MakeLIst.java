import java.util.ArrayList;
import java.util.List;

public class MakeLIst {
    private static List<String> Jvm = new ArrayList<>();

    public List<String> getJvm() {
        return Jvm;
    }

    public void setJvm(String jvm) {
        Jvm.add(jvm);
    }
}
