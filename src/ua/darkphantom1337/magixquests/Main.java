/*
 * @Author DarkPhantom1337
 * @Version 1.0.0
 */
package ua.darkphantom1337.magixquests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public ConfigFile cfg;
	public Map<String, Integer> top_ten_levels = new HashMap<String, Integer>();
	public LinkedList<String> all_top = new LinkedList<String>();
	private HashMap<String, PlayerDataFile> getdata = new HashMap<String, PlayerDataFile>();
	public HashMap<String, QuestFile> quests = new HashMap<String, QuestFile>();
	public HashMap<String, String> all_menus_names = new HashMap<String, String>();
	public static Main main;

	public void onEnable() {
		try {
			main = Main.this;
			cfg = new ConfigFile(this, "config.yml");
			updateQuests();
			getCommand("quest").setExecutor(new QuestsCMD(this));
			Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
			Bukkit.getConsoleSender()
					.sendMessage("§c[§eMagixQuests§c] §f-> §aPlugin succesfully enabled! // by Darkphantom1337, 2021");
		} catch (Exception e) {
			Bukkit.getConsoleSender()
					.sendMessage("§c[§eMagixQuests§c] §f-> Â§cError in enabling plugin! Plugin disabled!\nError:"
							+ e.getLocalizedMessage());
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public void onDisable() {
		Bukkit.getConsoleSender()
				.sendMessage("§c[§eMagixQuests§c] §f-> §cPlugin succesfully disabled! // by Darkphantom1337, 2021");
	}
	
	public void updateQuests() {
		HashMap<String, QuestFile> quests_new = new HashMap<String, QuestFile>();
		HashMap<String, String> all_menus_names_new = new HashMap<String, String>();
		for (String s :  cfg.getAllQuests()) {
			QuestFile questdata = new QuestFile(this, s);
			quests_new.put(s, questdata);
			all_menus_names_new.put(questdata.getS("GUI.Name"), s);
		}
		this.quests = quests_new;
		this.all_menus_names = all_menus_names_new;
	}

	public PlayerDataFile getDataFile(String playername) {
		if (getdata.containsKey(playername)) {
			return getdata.get(playername);
		} else {
			getdata.remove(playername);
			getdata.put(playername, new PlayerDataFile(this, playername));
			return getDataFile(playername);
		}
	}
	
	
	
	public static Main getInstance() {
		return main;
	}

}