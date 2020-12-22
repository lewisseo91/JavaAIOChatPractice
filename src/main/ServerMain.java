package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import servlet.AIOServer;

import java.io.IOException;

public class ServerMain {

	static Server server;

	public static void main(String[] args) throws Exception {
		startWebServer();
		AIOServerStart();
	}

	private static void startWebServer() throws Exception {

		server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(10800);
		server.addConnector(connector);

		WebAppContext ctx = new WebAppContext();
		ctx.setContextPath("/");
		ctx.setWar("html");
		ctx.setParentLoaderPriority(true);
		ctx.setWelcomeFiles(new String[]{"index.html"});
		ctx.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
		ctx.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".|.*.jar$|.*/classes/.*");

		// 4. Enabling the Annotation based configuration
		org.eclipse.jetty.webapp.Configuration.ClassList classList = org.eclipse.jetty.webapp.Configuration.ClassList
				.setServerDefault(server);
		classList.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
		classList.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");

		server.setHandler(ctx);
		server.start();
	}

	public static void AIOServerStart() throws Exception {
//		new AIOServer().start(8888);
//        new AioServer().startWithCompletionHandler();
	}
}
