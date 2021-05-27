package ua.darkphantom1337.magixquests;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MagixLevelsCMD implements CommandExecutor {

	private Main plugin;

	public MagixLevelsCMD(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0 && sender instanceof Player) {
			return true;
		}	
		if (args.length == 2) {
			if (args[0].equals("open")) {
				new Quest(args[1]).openQuestMenu((Player) sender);
				return true;
			}
		}		
		if (!sender.hasPermission("magixquests.admin")) {
			sender.sendMessage("§c[§eMagixQuests§c] §f-> §cКоманда не существует.");
			return false;
		}
		if (args.length == 1) {
			if (args[0].equals("reload")) {
				plugin.updateQuests();
				sender.sendMessage("§c[§eMagixQuests§c] §f-> §aКвесты перезагружены.");
				return true;
			}
		}
		sender.sendMessage("§c[§eMagixQuests§c] §f-> §cТакого аргумента не существует либо Вы не указали дополнительные аргументы.");
		return false;
	}

}
