package ua.darkphantom1337.magixquests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Quest {
	
	@SuppressWarnings("unused")
	private String questID;
	private QuestFile questcfg;
	private String menuname;
	private Integer menusize;
	
	public Quest(String questID) {
		this.questID = questID;
		this.questcfg = Main.getInstance().quests.get(questID);
		this.menuname = questcfg.getS("GUI.Name");
		this.menusize = questcfg.getInt("GUI.Size");
	}
	
	public void openQuestMenu(Player p) {
		Inventory inv = Bukkit.createInventory(p, menusize, menuname);
		for (int i = 0; i < menusize; i++) {
			if (questcfg.isNonFunctionalItem(i)) 
				inv.setItem(i, questcfg.getItem("GUI.NonFunctionalItems." + i));
			else if (questcfg.isTaskItem(i)) {
				QuestTask task =  new QuestTask(questcfg, i);
				ItemStack item = task.getTaskItem(p);
				inv.setItem(i, updateItemLore(item, getUpdateLore(item.getItemMeta().getLore() != null ? item.getItemMeta().getLore() : new ArrayList<String>(), p, task, Main.getInstance().getDataFile(p.getName()))));
			}
		}
		p.openInventory(inv);
	}
	
	public String getNeedStatus(Boolean b) {
		if (b)
			return "§a✔";
		else
			return "§c✘";
	}
	
	private String replaceValues(String toreplace, Player p, QuestTask taskdata, PlayerDataFile datafile) {
		return toreplace
				.replaceAll("%ckp%", "" + datafile.getKilledPlayers())
				.replaceAll("%ckm%", "" + datafile.getKilledMobs())
				.replaceAll("%nkp%", "" + taskdata.getNeedPlayerKills())
				.replaceAll("%nkm%", "" + taskdata.getNeedMobKills())
				.replaceAll("%sni%", getNeedStatus(taskdata.isYesItem(p)))
				.replaceAll("%sfnpc%", getNeedStatus(taskdata.isYesNPC(p)))
		.replaceAll("%skp%", getNeedStatus(datafile.getKilledPlayers() >= taskdata.getNeedPlayerKills()))
		.replaceAll("%skm%",  getNeedStatus(datafile.getKilledMobs() >= taskdata.getNeedMobKills()));
	}
	
	private List<String> getUpdateLore(List<String> lore, Player p, QuestTask taskdata, PlayerDataFile datafile) {
		List<String> new_lore = new ArrayList<String>();
		for (String s : lore)
			new_lore.add(replaceValues(s, p, taskdata, datafile));
		return new_lore;
	}

	private ItemStack updateItemLore(ItemStack item, List<String> new_lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(new_lore);
		item.setItemMeta(meta);
		return item;
	}
	
	

}
