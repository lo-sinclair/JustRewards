package locb.jp.justrewards.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


import locb.jp.justrewards.JustRewards;

public class HotmcController extends AbstractHandler
 {
	
	@SuppressWarnings("unused")
	private final JustRewards plugin;

	public HotmcController(JustRewards plugin) {
		this.plugin = plugin;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		if (!target.equals("/hotmc")) {
		  response.sendError(HttpStatus.INTERNAL_SERVER_ERROR_500);
	      //response.sendError(HttpServletResponse.SC_NOT_FOUND);
	      return;
	   }
		System.out.println(target);
		String name = request.getParameter("name");
		System.out.println(name);
		//Future<Object> = plugin.getServer().getScheduler().callSyncMethod(plugin, () -> HotmcRegistry)

		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("Ok");
		
		//response.sendError(HttpStatus.INTERNAL_SERVER_ERROR_500);
		
	}






	


}
