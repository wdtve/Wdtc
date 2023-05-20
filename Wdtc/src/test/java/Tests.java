import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Tests {
    @Test
    public void testgetErrorWin() throws IOException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        System.out.println(formatter.format(calendar.getTime()));
    }
}
