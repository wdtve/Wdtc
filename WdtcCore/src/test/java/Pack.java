import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Pack {

    @Test
    public void getname() throws IOException {
        Files.createDirectories(Paths.get(System.getProperty("user.home") + "\\Wdtc"));
    }

}
