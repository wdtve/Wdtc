import com.alibaba.fastjson2.JSON;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class list {
    public static void main(String[] args) throws IOException {
        MakeLIst makeLIst = new MakeLIst();
        makeLIst.setJvm("1");
        makeLIst.setJvm("2");
        String jsonArray = JSON.toJSONString(makeLIst);
        FileUtils.writeStringToFile(new File("WdtcCore/src/test/java/list.json"), jsonArray);
    }
}
