import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class Tests {
    @Test
    public void testgetErrorWin() {
        try {
            File file = new File("asd.txt");
            System.out.println(FileUtils.readFileToString(file, "UTF-8"));
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
