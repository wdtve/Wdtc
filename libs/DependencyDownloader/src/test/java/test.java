import org.junit.jupiter.api.Test;
import org.wdt.platform.DefaultDependency;

public class test {
    @Test
    public void test() {
        DefaultDependency dependency = new DefaultDependency("log4j:log4j:1.2.17");
        System.out.println(dependency.getSpaec());
    }
}
