package locb.jp.justrewards;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import locb.jp.justrewards.commands.CommandReward;
import locb.jp.justrewards.events.PlayerListener;
import locb.jp.justrewards.server.JustServer;

public class JustRewards extends JavaPlugin {
	private static JustRewards instance;
	Logger log = Logger.getLogger("Minecraft");
	private SQLDatabase db;
	private JustServer server;




	@Override
	public FileConfiguration getConfig() {
		// TODO Auto-generated method stub
		return super.getConfig();
	}
	@Override
	public void onEnable() {
		instance = this;

		ConsoleCommandSender console = getServer().getConsoleSender();

		console.sendMessage(ChatColor.DARK_BLUE + "[JustRewards] " + ChatColor.GRAY + "Hi!");

		File config_file = new File(getDataFolder() + File.separator + "config.yml" );
		if(!config_file.exists()) {
			getLogger().info("Creating new config file...");
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}

		File salary_file = new File(getDataFolder() + File.separator + "salary.yml");
		if(!salary_file.exists()) {
			try {
				salary_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			db = new SQLDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		}

		startJustServer();
		getCommand("reward").setExecutor(new CommandReward(this));

		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	private void startJustServer() {
        Integer port = getConfig().getInt("jetty.port");

		server = new JustServer(port, this);

        try {
            server.start();
            getLogger().info("Started Server: " + port);
        } catch (Exception e) {
            getLogger().severe("Could not start embedded Jetty server");
        }
    }
	
	@Override
    public void onDisable() {
        try {
            server.stop();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Failed to stop metrics server gracefully: " + e.getMessage());
            getLogger().log(Level.FINE, "Failed to stop metrics server gracefully", e);
        }
    }

	public static JustRewards getInstance() {
		return instance;
	}

	public SQLDatabase getDb() {
		return db;
	}
}
