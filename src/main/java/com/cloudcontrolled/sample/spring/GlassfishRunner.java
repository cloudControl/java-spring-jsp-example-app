package com.cloudcontrolled.sample.spring;

import java.io.File;
import java.io.IOException;

import org.glassfish.api.deployment.DeployCommandParameters;
import org.glassfish.api.embedded.ContainerBuilder;
import org.glassfish.api.embedded.EmbeddedContainer;
import org.glassfish.api.embedded.EmbeddedDeployer;
import org.glassfish.api.embedded.EmbeddedFileSystem;
import org.glassfish.api.embedded.LifecycleException;
import org.glassfish.api.embedded.Server;

public class GlassfishRunner {

    private static final String CONTEXT_ROOT = "/";
    private static final String SERVER_ID = "embedded_glassfish";
    private static final String SERVER_INSTALL_DIR = "target/glassfish/installroot";
    private static final String SERVER_INSTANCE_DIR = "target/glassfish/instanceroot";

    /**
     * Embedded Glassfish runner
     *
     * @param args
     *            - args[0] path to *.war archive to be deployed
     *            - args[1] optional context root
     * @throws LifecycleException
     * @throws IOException
     */
    public static void main(String[] args) throws LifecycleException, IOException {
        Server.Builder builder = new Server.Builder(SERVER_ID);
        builder.embeddedFileSystem(createEmbeddedFileSystem());

        final Server server = builder.build();
        server.createPort(Integer.valueOf(System.getenv("PORT")));

        ContainerBuilder<EmbeddedContainer> container = server
                .createConfig(ContainerBuilder.Type.web);
        server.addContainer(container);
        server.start();

        EmbeddedDeployer deployer = server.getDeployer();
        DeployCommandParameters params = new DeployCommandParameters();
        params.contextroot = args.length == 2 ? args[1] : CONTEXT_ROOT;
        deployer.deploy(new File(args[0]), params);
    }

    private static EmbeddedFileSystem createEmbeddedFileSystem() {
        EmbeddedFileSystem.Builder efsb = new EmbeddedFileSystem.Builder();
        efsb.installRoot(new File(SERVER_INSTALL_DIR));
        efsb.instanceRoot(new File(SERVER_INSTANCE_DIR));
        efsb.autoDelete(true);
        return efsb.build();
    }
}