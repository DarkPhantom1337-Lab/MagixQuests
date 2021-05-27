/*
 * @Author DarkPhantom1337
 * @Version 1.0.0
 */
package ua.darkphantom1337.magixquests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerDataFile {

	private FileConfiguration filecfg;
	private Main plugin;
	private File file;
	private String filename;
	private String pluginname;

	public PlayerDataFile(Main plugin, String player_name) {
		this.plugin = plugin;
		this.filename = player_name + ".yml";
		this.pluginname = plugin.getName();
		setupWalletFile();
		if (getWalletFile().isSet(pluginname))
			saveWalletFile();
		else
			firstFill();
	}

	private void setupWalletFile() {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		String dirpath = plugin.getDataFolder() + File.separator + "DataFiles";
		if (!new File(dirpath).exists())
			new File(dirpath).mkdir();
		file = new File(dirpath, filename);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error in creating data file " + filename + "!");
				e.printStackTrace();
			}
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	private FileConfiguration getWalletFile() {
		return filecfg;
	}

	public void saveWalletFile() {
		try {
			filecfg.save(file);
		} catch (IOException localIOException) {
			System.out.println("Error in saving data file " + filename + "!");
		}
	}

	public void reloadWalletFile() {
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	private void firstFill() {
		getWalletFile().set(pluginname, " File: " + filename + " || Author: DarkPhantom1337");
		getWalletFile().set("KilledMobs", 0);
		getWalletFile().set("KilledPlayers", 0);
		getWalletFile().set("ClickedNPC", new ArrayList<String>());
		getWalletFile().set("CompletedQuests", new ArrayList<String>());
		getWalletFile().set("CompletedTask", new ArrayList<String>());
		getWalletFile().set("GivedTaskReward", new ArrayList<String>());
		getWalletFile().set("CompletedEveryDayTask", new ArrayList<String>());

		saveWalletFile();
	}

	public List<String> getClickedNPC() {
		return getWalletFile().getStringList("ClickedNPC");
	}

	public List<String> getCompletedQuests() {
		return getWalletFile().getStringList("CompletedQuests");
	}
	
	public List<String> getCompletedTask() {
		return getWalletFile().getStringList("CompletedTask");
	}
	
	public List<String> getCompletedEveryDayTask() {
		return getWalletFile().getStringList("CompletedEveryDayTask");
	}
	
	public List<String> getGivedTaskReward() {
		return getWalletFile().getStringList("GivedTaskReward");
	}
	
	public Integer getKilledMobs() {
		return getWalletFile().getInt("KilledMobs");
	}
	
	public Integer getKilledPlayers() {
		return getWalletFile().getInt("KilledPlayers");
	}
	
	
	public void setKilledMobs(Integer killedmobs) {
		getWalletFile().set("KilledMobs", killedmobs);
		saveWalletFile();
	}
	
	public void setKilledPlayers(Integer killedplayers) {
		getWalletFile().set("KilledPlayers", killedplayers);
		saveWalletFile();
	}

	public void giveKilledMobs(Integer killed) {
		setKilledMobs(getKilledMobs()+killed);
	}
	
	public void giveKilledPlayers(Integer killed) {
		setKilledPlayers(getKilledPlayers()+killed);
	}

	public void setClickedNPC(List<String> clnpc) {
		getWalletFile().set("ClickedNPC", clnpc);
		saveWalletFile();
	}

	public void setCompletedQuests(List<String> clnpc) {
		getWalletFile().set("CompletedQuests", clnpc);
		saveWalletFile();
	}
	
	public void setCompletedTask(List<String> clnpc) {
		getWalletFile().set("CompletedTask", clnpc);
		saveWalletFile();
	}
	
	public void setGivedTaskReward(List<String> clnpc) {
		getWalletFile().set("GivedTaskReward", clnpc);
		saveWalletFile();
	}
	
	public void setCompletedEveryDayTask(List<String> clnpc) {
		getWalletFile().set("CompletedEveryDayTask", clnpc);
		saveWalletFile();
	}

	public void addClickedNPC(String npcname) {
		List<String> cnpc = getClickedNPC();
		cnpc.add(npcname);
		setClickedNPC(cnpc);
	}
	
	public void giveCompletedQuests(String questname) {
		List<String> cnpc = getCompletedQuests();
		cnpc.add(questname);
		setCompletedQuests(cnpc);
	}
	
	public void giveCompletedTask(String questname, String taskname) {
		List<String> cnpc = getCompletedTask();
		cnpc.add(questname + ";" + taskname);
		setCompletedTask(cnpc);
	}
	
	public void giveGivedTaskReward(String questname, String taskname) {
		List<String> cnpc = getGivedTaskReward();
		cnpc.add(questname + ";" + taskname);
		setGivedTaskReward(cnpc);
	}
	
	
	public void giveCompletedEveryDayTask(String questname, String taskname, String date) {
		List<String> cnpc = getCompletedEveryDayTask();
		cnpc.add(questname + ";" + taskname + ";" + date);
		setCompletedEveryDayTask(cnpc);
	}
	
	public Boolean isCompleteQuest(String questname) {
		return getCompletedQuests().contains(questname);
	}
	
	public Boolean isCompletedTask(String questname, String taskname) {
		return getCompletedTask().contains(questname + ";" + taskname);
	}
	
	
	public Boolean isCompletedEveryDayTask(String questname, String taskname, String data) {
		return getCompletedEveryDayTask().contains(questname + ";" + taskname + ";" +data);
	}
	
	public Boolean isGivedReward(String questname, String taskname) {
		return getGivedTaskReward().contains(questname + ";" + taskname);
	}
	
	public Boolean getCompletedEveryDayTask(String questname, String taskname, String date) {
		return getCompletedEveryDayTask().contains(questname + ";" + taskname + ";" + date);
	}
}
