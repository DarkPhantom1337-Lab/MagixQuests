/*
 * @Author DarkPhantom1337
 * @Version 1.0.0
 */
package ua.darkphantom1337.magixquests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QuestFile {

	private FileConfiguration filecfg;
	private Main plugin;
	private File file;
	private String filename;
	private String questname;
	private String pluginname;

	public QuestFile(Main plugin, String questname) {
		this.plugin = plugin;
		this.questname = questname;
		this.filename = "quest_" + questname + ".yml";
		this.pluginname = plugin.getName();
		setupCfgFile();
		if (getCfgFile().isSet(pluginname))
			saveCfgFile();
		else
			firstFill();
	}

	private void setupCfgFile() {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		file = new File(plugin.getDataFolder(), filename);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException localIOException) {
				System.out.println("Error in creating file " + filename + "!");
			}
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	private FileConfiguration getCfgFile() {
		return filecfg;
	}

	public void saveCfgFile() {
		try {
			filecfg.save(file);
		} catch (IOException localIOException) {
			System.out.println("Error in saving file " + filename + "!");
		}
	}

	public void reloadCfgFile() {
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	private void firstFill() {
		getCfgFile().set(pluginname, "Quest: " + questname + " || Author: DarkPhantom1337");
		getCfgFile().set("QuestID", questname);
		getCfgFile().set("GUI.Name", "§aКвест 'Killer'");
		getCfgFile().set("GUI.Size", 54);
		for (int slot : new Integer[] {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53}) {
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Name", "§dФиолетовая панель");
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Lore", new ArrayList<String>());
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Material", "STAINED_GLASS_PANE");
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Data", 10);
		}
		for (int slot : new Integer[] {10,11,12,13,14,15,16,19,20,21,23,24,25,28,34,37,38,39,40,41,42,43}) {
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Name", "§fБелая панель"); 
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Lore", new ArrayList<String>());
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Material", "STAINED_GLASS_PANE");
			getCfgFile().set("GUI.NonFunctionalItems." + slot + ".Data", 0);
		}
		getCfgFile().set("GUI.NonFunctionalItems.22.Name", "§aБумага"); 
		getCfgFile().set("GUI.NonFunctionalItems.22.Lore", Arrays.asList("§7Текст для квеста " + questname,"§7Ты на верном пути!", "§7Продолжай в том же духе!","§7Ну с фантазией туговато крч..."));
		getCfgFile().set("GUI.NonFunctionalItems.22.Material", "PAPER");
		for (int slot : new Integer[] {29,30,31,32,33}) {
			getCfgFile().set("GUI.TaskItems." + slot + ".Name", "§fЗадание №" + (slot-28)); 
			getCfgFile().set("GUI.TaskItems." + slot + ".TaskID", "task"+(slot-28)); 
			getCfgFile().set("GUI.TaskItems." + slot + ".TaskType", slot == 29 || slot == 30  ? "USUAL" : "EVERYDAY"); 
			getCfgFile().set("GUI.TaskItems." + slot + ".Lore", Arrays.asList("","§7Требования:", "§7Убить мобов: %ckm%/%nkm% %skm%",
					"§7Убить игроков: %ckp%/%nkp% %skp%",
					"§7Принести блок камня: %sni%",
					"§7Найти торговца золотом Валеру: %sfnpc%"));
						getCfgFile().set("GUI.TaskItems." + slot + ".Material", "SKULL_ITEM");
			getCfgFile().set("GUI.TaskItems." + slot + ".Data", 3);
			getCfgFile().set("GUI.TaskItems." + slot + ".NeedPlayerKills", 1);
			getCfgFile().set("GUI.TaskItems." + slot + ".NeedMobKills", 1);
			getCfgFile().set("GUI.TaskItems." + slot + ".NeedItem", true);
			getCfgFile().set("GUI.TaskItems." + slot + ".NeededItem.Material", "STONE");
			getCfgFile().set("GUI.TaskItems." + slot + ".NeededItem.Amount", 6);
			getCfgFile().set("GUI.TaskItems." + slot + ".NeedNPC", true);
			getCfgFile().set("GUI.TaskItems." + slot + ".NeededNPC.Name", "Валера");
			getCfgFile().set("GUI.TaskItems." + slot + ".NeedTask", slot == 29 ? false : true);
			if (slot != 29)
				getCfgFile().set("GUI.TaskItems." + slot + ".NeededTask", Arrays.asList("task" + (slot-29)));
			getCfgFile().set("GUI.TaskItems." + slot + ".Commands", Arrays.asList("give %playername% stone 50"));
		}
		saveCfgFile();
	}

	public ItemStack getItem(String path) {
		ItemStack item = new ItemStack(Material.valueOf(getCfgFile().getString(path + ".Material")));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getCfgFile().getString(path + ".Name"));
		meta.setLore(getCfgFile().getStringList(path + ".Lore"));
		if (getCfgFile().isSet(path + ".Data"))
			item.setDurability((short) getCfgFile().getInt(path + ".Data"));
		item.setItemMeta(meta);
		return item;
	}
	
	public Boolean isNonFunctionalItem(Integer slot) {
		return getCfgFile().isSet("GUI.NonFunctionalItems." + slot + ".Name");
	}
	public Boolean isTaskItem(Integer slot) {
		return getCfgFile().isSet("GUI.TaskItems." + slot + ".Name");
	}

	public void setS(String path, String value) {
		getCfgFile().set(path, value);
		saveCfgFile();
	}
	

	public Location getHoloLocation() {
		String[] data = getCfgFile().getString("HoloLocation").split(";");
		return new Location(Bukkit.getWorld(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]),
				Integer.parseInt(data[3]));
	}

	public List<String> getHoloText() {
		return getCfgFile().getStringList("HoloText");
	}

	public ItemStack getPanelItem(String parameter) {
		ItemStack item = new ItemStack(
				Material.valueOf(getCfgFile().getString("Items." + parameter + "Panel.Material")));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getCfgFile().getString("Items." + parameter + "Panel.Name"));
		meta.setLore(getCfgFile().getStringList("Items." + parameter + "Panel.Lore"));
		if (parameter.equals("True"))
			item.setDurability((short) 5);
		else
			item.setDurability((short) 14);
		item.setItemMeta(meta);
		return item;
	}

	public Integer getInt(String path) {
		return getCfgFile().getInt(path);
	}

	public String getS(String path) {
		return getCfgFile().getString(path);
	}

	public Boolean getB(String path) {
		return getCfgFile().getBoolean(path);
	}

	public List<String> getSL(String path) {
		return getCfgFile().getStringList(path);
	}

	public String getGiveMsg() {
		return getCfgFile().getString("GiveMsg").replaceAll("&", "§");
	}

}
