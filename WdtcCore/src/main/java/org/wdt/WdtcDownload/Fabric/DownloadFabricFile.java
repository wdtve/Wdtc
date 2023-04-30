package org.wdt.WdtcDownload.Fabric;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.wdt.Launcher;
import org.wdt.WdtcDownload.FileUrl;

import java.io.IOException;
import java.util.List;

public class DownloadFabricFile {
    private static String FabricVersionNumber;
    private static String GameVersionNumber;
    private static Launcher launcher;

    public DownloadFabricFile(String FabricVersionNumber, String GameVersionNumber) {
        DownloadFabricFile.FabricVersionNumber = FabricVersionNumber;
        DownloadFabricFile.GameVersionNumber = GameVersionNumber;
    }

    public DownloadFabricFile(String FabricVersionNumber, Launcher launcher) {
        DownloadFabricFile.FabricVersionNumber = FabricVersionNumber;
        DownloadFabricFile.GameVersionNumber = launcher.getVersion();
        DownloadFabricFile.launcher = launcher;
    }

    public void StartTask() throws IOException, DependencyCollectionException, DependencyResolutionException {
        GetFabricFile getFile = new GetFabricFile(FabricVersionNumber, GameVersionNumber);
        List<String> FileList = getFile.getFabricFileName();
        for (String s : FileList) {
            DownloadTask(s);
        }
    }

    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }

    private static RepositorySystemSession newSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(launcher.GetGameLibPath());
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        return session;
    }

    private static void DownloadTask(String coords) throws DependencyResolutionException, DependencyCollectionException {
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem);
        Dependency dependency =
                new Dependency(new DefaultArtifact(coords), "compile");
        RemoteRepository central =
                new RemoteRepository.Builder("central", "default", FileUrl.getBmclapiLibraries()).build();
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.addRepository(central);
        DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();
        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot(node);
        repoSystem.resolveDependencies(session, dependencyRequest);
        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        node.accept(nlg);
        System.out.println(nlg.getClassPath());
    }
}
