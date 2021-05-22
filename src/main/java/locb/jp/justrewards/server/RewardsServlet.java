package locb.jp.justrewards.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.server.Request;

import locb.jp.justrewards.JustRewards;
import locb.jp.justrewards.tools.Actions;


@MultipartConfig
public class RewardsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final Actions actions = new Actions();
	FileConfiguration salary_conf;
	
	public RewardsServlet() {
		File salary_file = new File(JustRewards.getInstance().getDataFolder() + File.separator + "salary.yml");
		salary_conf = YamlConfiguration.loadConfiguration(salary_file);
	}
	
	
	// чудо-свойство
	private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement(JustRewards.getInstance().getDataFolder() + "/temp");
	 
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String contentType = request.getContentType();
		
		if (contentType != null && contentType.startsWith("multipart/")) {
			request.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
		}
		
		String nick = request.getParameter("nick");
		String time = request.getParameter("time");
		String sign = request.getParameter("sign");
		
		
		System.out.println(ChatColor.DARK_BLUE + "[JustRewards] " + ChatColor.GRAY + "New vote: {nick: " + nick + "; time: " + time + "}");

		if (! nick.isEmpty() && !time.isEmpty() && !sign.isEmpty()) {
			String sk = "fas73g1j1zf4YTy0SGmsy9iRyqPcCP";

			String sha1 = DigestUtils.sha1Hex(nick + time + sk);
			
			if ( !sign.equals(sha1) ) {
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				response.setContentType("text/html;charset=utf-8");
				
				response.getWriter().println("Переданные данные не прошли проверку.");
				return;
			}
			
			int pay = salary_conf.getInt("vote.pay");
			
			actions.sendReward(nick, "PLUGIN", pay, "Vote pay - HotMC");
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().print("ok");
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println("Не переданы необходимые данные.");
		
	}
	
	
	//http://146.59.162.126:8129/hotmc?nick=gena&time=1620300239&sign=359d2889fa37eabcab25c3cae00aa133add4f339
	//http://localhost:8129/hotmc?nick=gena&time=1620300239&sign=359d2889fa37eabcab25c3cae00aa133add4f339
	//http://212.24.97.196:8129/hotmc?nick=gena&time=1620300239&sign=359d2889fa37eabcab25c3cae00aa133add4f339
	//http://212.24.97.196/vote.php?nick=gena&time=1620300239&sign=359d2889fa37eabcab25c3cae00aa133add4f339
	
	//curl -X POST -F 'nick=gena' -F 'time=1620300239' -F 'sign=359d2889fa37eabcab25c3cae00aa133add4f339' http://212.24.97.196:8129/hotmc
	//curl -X POST -F 'nick=gena' -F 'time=1620300239' -F 'sign=359d2889fa37eabcab25c3cae00aa133add4f339' http://212.24.97.196/vote.php
	

}
