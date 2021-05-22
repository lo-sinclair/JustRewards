package locb.jp.justrewards.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import locb.jp.justrewards.JustRewards;
import locb.jp.justrewards.model.Reward;

public class Actions {
	
	private final JustRewards pl = JustRewards.getInstance();
	FileConfiguration config = JustRewards.getInstance().getConfig();
	
	public Actions() {
		config = JustRewards.getInstance().getConfig();
	}
	
	public boolean sendPay(Player p, Integer pay, String group) {
		
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		long ts_naw = System.currentTimeMillis();
		
		boolean allow_pay = true;
		Reward lpay = pl.getDb().findLastPay(p.getName());
		if(lpay != null) {
			Long ts_lpay = lpay.getTime();
		   
		   //long diffInMillies = Math.abs(ts_lpay - ts_naw );
			
			
		    Date date1 = new Date(ts_lpay - 9*60*60*1000);
		    Date date2 = new Date(ts_naw - 9*60*60*1000);
		   
		    Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			cal1.add(Calendar.HOUR, -9);
			cal1.set(Calendar.MILLISECOND, 0);
			cal1.set(Calendar.SECOND, 0);
			cal1.set(Calendar.MINUTE, 0);
			cal1.set(Calendar.HOUR_OF_DAY, 0);
			
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			cal2.add(Calendar.HOUR, -9);
			cal2.set(Calendar.MILLISECOND, 0);
			cal2.set(Calendar.SECOND, 0);
			cal2.set(Calendar.MINUTE, 0);
			cal2.set(Calendar.HOUR_OF_DAY, 0);
		   
		   long diffInMillies = Math.abs( cal1.getTimeInMillis() - cal2.getTimeInMillis() );
		   
		   long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		   if(diff < 1) {
			   allow_pay = false;
		   }
		}
		
		if(allow_pay) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					ItemStack item = Craft.craftBit();
					item.setAmount(pay);
					if ( InventoryIsFree(p.getInventory(), item) ) {
						p.getInventory().addItem(item);
					}
					else {
						p.getWorld().dropItem(p.getLocation().add(0,1,0), item);
					}
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1, 1);
					String m = config.getString("messages.pay_arrived");
					p.sendMessage(m.replace("&", "\u00a7") );
					
					Reward reward = new Reward(
							p.getName(), 
							"PLUGIN",
							"pay", 
							ts_naw, 
							pay,
							group, 
							"Dayly pay",
							1);
					pl.getDb().createReward(reward);
				}
			};
			Bukkit.getScheduler().runTaskLater(pl, r, 20 * config.getInt("time_after_join"));

		}
		
		return true;
	}
	
	
	
	public boolean sendReward(String nick, String sender, Integer pay, String comment, boolean update, long time) {
		
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		long ts_naw = System.currentTimeMillis();
		
		int status = 0;
		Player p = Bukkit.getPlayer(nick);
		
		if( p != null ) {
			ItemStack item = Craft.craftBit();
			item.setAmount(pay);
			
			status = 1;
			
			if ( InventoryIsFree(p.getInventory(), item) ) {
				p.getInventory().addItem(item);
			}
			else {
				p.getWorld().dropItem(p.getLocation().add(0,1,0), item);
			}

			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1, 1);
			String m = config.getString("messages.reward_send");
			if(comment.length() != 0) {
				m = m + ": " + comment;
			}
			p.sendMessage(m.replace("&", "\u00a7") );
		}
		
		if(!update) {
			Reward reward = new Reward(
					nick,
					sender,
					"reward", 
					ts_naw, 
					pay,
					"", 
					comment,
					status);
			pl.getDb().createReward(reward); 
		}
		else {
			pl.getDb().updateRewardStatus(nick, time, 1);		
		}

		return true;
	}
	
	
	public boolean sendReward(String nick, String sender, Integer pay, String comment) {
		this.sendReward(nick, sender, pay, comment, false, 0);
		return true;
	}
	
	
	
	private boolean InventoryIsFree(PlayerInventory inventory, ItemStack item_bit){
		if(inventory.firstEmpty() != -1) {
			return true;
		}
		else {
			int i = inventory.first(Material.IRON_NUGGET);
			ItemStack item = inventory.getItem(i);
			if ( item.getType().equals(item_bit.getType()) && item.getItemMeta().equals(item_bit.getItemMeta()) ) {
				if (item.getAmount() <= (64 - item_bit.getAmount()) ) {
					return true;
				}
			}
		}
		return false;
	}

	
	
	

}
