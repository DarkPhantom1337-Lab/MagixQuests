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

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigFile {

	private FileConfiguration filecfg;
	private Main plugin;
	private File file;
	private String filename;
	private String pluginname;

	public ConfigFile(Main plugin, String filename) {
		this.plugin = plugin;
		this.filename = filename;
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
		getCfgFile().set(pluginname, " File: " + filename + " || Author: DarkPhantom1337");
		getCfgFile().set("Quests", Arrays.asList("killer"));
		getCfgFile().set("CompletedTaskItem.Name", "§aЗадание выполнено!");
		getCfgFile().set("CompletedTaskItem.Lore", new ArrayList<String>());
		getCfgFile().set("CompletedTaskItem.Material", "STAINED_GLASS_PANE");
		getCfgFile().set("CompletedTaskItem.Data", 5);
		getCfgFile().set("LockTaskItem.Name", "§aЗадание закрыто!");
		getCfgFile().set("LockTaskItem.Lore", new ArrayList<String>());
		getCfgFile().set("LockTaskItem.Material", "STAINED_GLASS_PANE");
		getCfgFile().set("LockTaskItem.Data", 14);
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
	
	public ItemStack getCompletedTaskItem() {
		return getItem("CompletedTaskItem");
	}
	
	public ItemStack getLockTaskItem() {
		return getItem("LockTaskItem");
	}
	
	public List<String> getAllQuests(){
		return getCfgFile().getStringList("Quests");
	}
	
	public void setS(String path, String value) {
		getCfgFile().set(path, value);
		saveCfgFile();
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

}
