package locb.jp.justrewards.server;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import locb.jp.justrewards.JustRewards;

public class JustServer {
	
	private final int port;
	private final JustRewards plugin;

	private Server server;

	public JustServer(int port, JustRewards plugin) {
		this.port = port;
		this.plugin = plugin;
	}

	public void start() throws Exception {
		GzipHandler gzipHandler = new GzipHandler();
		gzipHandler.setHandler( new HotmcController(plugin));
		
		RewardsServlet allRequestsServlet = new RewardsServlet();
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(allRequestsServlet), "/hotmc");

		InetSocketAddress address = new InetSocketAddress(port);
		server = new Server(address);
		server.setHandler(context);
		//server.setHandler(gzipHandler);

		server.start();
	}

	public void stop() throws Exception {
		if (server == null) {
			return;
		}

		server.stop();
	}

}
