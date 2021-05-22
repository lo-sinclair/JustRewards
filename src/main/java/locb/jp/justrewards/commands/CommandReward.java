package locb.jp.justrewards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import locb.jp.justrewards.JustRewards;
import locb.jp.justrewards.tools.Actions;

public class CommandReward implements CommandExecutor {
	
    @SuppressWarnings("unused")
	private JustRewards pl;
	private final Actions actions = new Actions();
	public CommandReward(JustRewards justRewards) {
		pl = justRewards;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 2) {
			sender.sendMessage("Не хватает аргументов");
			return true;
		}
		
		String name = args[0];
		
		int pay = Integer.parseInt(args[1]);
		
		String comment = "";
		
		if(args.length > 2) {
			for(int i = 2; i < args.length; i++){
			    String arg = args[i] + " ";
			    comment = comment + arg;
			}
		}
		
		actions.sendReward(name, sender.getName(), pay, comment);

		
		return true;
	}

}
