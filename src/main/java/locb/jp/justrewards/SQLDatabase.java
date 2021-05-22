
package locb.jp.justrewards;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import locb.jp.justrewards.JustRewards;
import locb.jp.justrewards.model.Reward;

public class SQLDatabase {
	private String url;
	private String user;
	private String password;
	
	
	public SQLDatabase() throws Exception {
		FileConfiguration config = JustRewards.getInstance().getConfig();
		
		Connection conn;
		
		if(config.getBoolean("mySQL.enable")) {
			String host = config.getString("mySQL.host");
			int port = config.getInt("mySQL.port");
			String dbname = config.getString("mySQL.dbname");
			user = config.getString("mySQL.user");
			password = config.getString("mySQL.password");
			url = "jdbc:mysql://" + host + ":" + port + "/" +dbname;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = getConnection();		
			} catch (Exception e) {
				url = "jdbc:sqlite:" + JustRewards.getInstance().getDataFolder() + File.separator + "database.db";
				Class.forName("org.sqlite.JDBC");
				user = null;
				conn = getConnection();
				JustRewards.getInstance().getLogger().warning("Failed to connect to MySQL, using SQLite");
			} 
			
			
		} else {
			url = "jdbc:sqlite:" + JustRewards.getInstance().getDataFolder() + File.separator + "database.db";
			Class.forName("org.sqlite.JDBC");
			conn = getConnection();
			JustRewards.getInstance().getLogger().info("Using SQLite");
		}
		
		Statement s = (Statement) conn.createStatement(); 
		
		String q = "CREATE TABLE IF NOT EXISTS justrewards_rewards ("
					+ "`nick` VARCHAR(30),"
					+ "`sender` VARCHAR(30)," 
				    + "`type` VARCHAR(30),"
				    + "`time` TIMESTAMP,"
				    + "`pay` INT,"
				    + "`group` VARCHAR,"
				    + "`comment` TEXT,"
				    + "`status` TINYINT,"
				    + "PRIMARY KEY(`nick`, `time`))";
		
		s.executeUpdate(q);
		
		s.close();
		conn.close();
	}
	
	public void createReward(Reward reward) {
		try {
			Connection c = getConnection();
			Statement s = c.createStatement();

			String q = "INSERT INTO justrewards_rewards (`nick`, `sender`, `type`, `time`, `pay`, `group`, `comment`, `status`) "
					+ "VALUES ('%s', '%s', '%s', %d, %d, '%s', '%s', '%d');";
			s.executeUpdate(String.format(q, 
					reward.getNick(),
					reward.getSender(),
					reward.getType(),
					reward.getTime(),
					reward.getPay(),
					reward.getGroup(),
					reward.getComment(),
					reward.getStatus()
			));

			s.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public int updateRewardStatus(String nick, Long time, int status) {
		int count = 0;
		
		try {
			Connection c = getConnection();
			Statement s = c.createStatement();
			
			String q = "UPDATE justrewards_rewards SET `status` = %d "
							+ "WHERE `nick` = '%s' AND `time` = %d;";
			
			count =  s.executeUpdate(String.format(q, status, nick, time));
			s.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	
	public Reward findLastPay( String nick ) {
		Reward reward = null;
		
		try {
			Connection c = getConnection();
			Statement s = c.createStatement();
			
			String q = "SELECT * FROM justrewards_rewards WHERE `nick` = '%s' AND `type` = 'pay' ORDER BY `time` DESC LIMIT 1;";
			
			ResultSet result = s.executeQuery(String.format(q, nick));
			
			if(result.next()) {
				reward = new Reward(
					result.getString("nick"), 
					result.getString("sender"),
					result.getString("type"), 
					result.getLong("time"),
					result.getInt("pay"),
					result.getString("group"), 
					result.getString("comment"),
					result.getInt("status")
				);
			}
			
			s.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reward;
	}
	
	public List<Reward> findUnsentRewards ( String nick ) {
		List<Reward> rewards = new ArrayList<>();
		
		try {
			Connection c = getConnection();
			Statement s = c.createStatement();
			
			String q = "SELECT * FROM justrewards_rewards WHERE `nick` = '%s' AND `status` = 0;";
			
			ResultSet result = s.executeQuery(String.format(q, nick));
			
			if(result.next()) {
				rewards.add (new Reward(
					result.getString("nick"), 
					result.getString("sender"),
					result.getString("type"), 
					result.getLong("time"),
					result.getInt("pay"),
					result.getString("group"), 
					result.getString("comment"),
					result.getInt("status")
				));
			}
			
			s.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rewards;
	}
	
	
	
	public Connection getConnection() throws SQLException {
		if (user != null)
			return DriverManager.getConnection(url, user, password);
		else return DriverManager.getConnection(url);
	}
}
