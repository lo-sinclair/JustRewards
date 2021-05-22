package locb.jp.justrewards.events;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import locb.jp.justrewards.JustRewards;
import locb.jp.justrewards.model.Reward;
import locb.jp.justrewards.tools.Actions;



public class PlayerListener implements Listener {
	
	private final JustRewards pl;
	private final Actions actions;
	FileConfiguration config;
	FileConfiguration salary_conf;
	
	public PlayerListener(JustRewards justRewards) {
		this.actions = new Actions();
		pl = justRewards;
		config = pl.getConfig();
		
		File salary_file = new File(JustRewards.getInstance().getDataFolder() + File.separator + "salary.yml");
		salary_conf = YamlConfiguration.loadConfiguration(salary_file);
	}

	
	@EventHandler
	public void join (PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String nick = p.getName();
		
		File salary_file = new File(pl.getDataFolder() + File.separator + "salary.yml");
		FileConfiguration salary = YamlConfiguration.loadConfiguration(salary_file);
		ConfigurationSection section = salary.getConfigurationSection("salary");
		if (section != null) {
			 for (String k : section.getKeys(false)) {
				 String group = salary.getString("salary." + k + ".group");
				 Integer pay = salary.getInt("salary." + k + ".pay");
				 
				 if(p.hasPermission("group." + group)) {
					 actions.sendPay(p, pay, group);
					 break;
				 }
			 }
		 }
		
		List<Reward> unsentRewards = pl.getDb().findUnsentRewards(nick);

		if(!unsentRewards.isEmpty()) { 
			int pay = salary_conf.getInt("vote.pay");
			
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					for(Reward r : unsentRewards) {
						actions.sendReward(nick, r.getSender(), pay, r.getComment(), true, r.getTime());
					}	
				}
			};
			Bukkit.getScheduler().runTaskLater(pl, runnable, 20 * config.getInt("time_after_join") + 3*20);
		}
		
		
	}
	
	
}
