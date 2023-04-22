import org.eclipse.aether.artifact.DefaultArtifact;
import org.junit.jupiter.api.Test;

public class FabricDownload {

    @Test
    public void getfabriclist() {
        DefaultArtifact defaultArtifact = new DefaultArtifact("org.ow2.asm:asm-util:9.4");
        String a = defaultArtifact.getGroupId().replaceAll("\\.", "/") + "/" + defaultArtifact.getArtifactId() + "/" + defaultArtifact.getVersion() + "/" + defaultArtifact.getArtifactId() + "-" + defaultArtifact.getVersion() + ".jar";
        System.out.println("https://maven.fabricmc.net/" + a);
    }
}
