package ua.darkphantom1337.magixquests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.event.NPCRightClickEvent;

public class Listeners implements Listener {

	private Main plugin;

	public Listeners(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getInventory() != null && e.getInventory().getType().equals(InventoryType.CHEST)) {
			if (plugin.all_menus_names.containsKey(e.getInventory().getName())) {
				e.setCancelled(true);
				if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
						|| e.getAction() == InventoryAction.COLLECT_TO_CURSOR)
					e.setCancelled(true);
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
					try {
						QuestFile questdata = plugin.quests.get(plugin.all_menus_names.get(e.getInventory().getName()));
						if (questdata != null) {
							if (questdata.isTaskItem(e.getSlot())) {
								QuestTask task = new QuestTask(questdata, e.getSlot());
								PlayerDataFile pldata = plugin.getDataFile(e.getWhoClicked().getName());
								if (task.getTaskType().equals("USUAL")) {
									if (!pldata.isCompletedTask(questdata.getS("QuestID"), task.getTaskID())) {
										if (task.is—ompletedRequirements((Player) e.getWhoClicked())) {
											if (task.getTaskType().equals("USUAL"))
												pldata.giveCompletedTask(questdata.getS("QuestID"), task.getTaskID());
											else
												pldata.giveCompletedEveryDayTask(questdata.getS("QuestID"),
														task.getTaskID(),
														new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
											pldata.setKilledMobs(0);
											pldata.setKilledPlayers(0);
											if (task.isNeedItem())
												e.getWhoClicked().getInventory()
														.remove(new ItemStack(task.getNeededItemMaterial(), 1));
											e.getWhoClicked().closeInventory();
											for (String cmd : task.getCommands())
												Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
														cmd.replaceAll("%playername%", e.getWhoClicked().getName()));
											pldata.giveGivedTaskReward(questdata.getS("QuestID"), task.getTaskID());
										}
									}
								} else {
									if (!pldata.isCompletedEveryDayTask(questdata.getS("QuestID"), task.getTaskID(),
											new SimpleDateFormat("dd/MM/yyyy").format(new Date()))) {
										if (task.is—ompletedRequirements((Player) e.getWhoClicked())) {
											if (task.getTaskType().equals("USUAL"))
												pldata.giveCompletedTask(questdata.getS("QuestID"), task.getTaskID());
											else
												pldata.giveCompletedEveryDayTask(questdata.getS("QuestID"),
														task.getTaskID(),
														new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
											pldata.setKilledMobs(0);
											pldata.setKilledPlayers(0);
											if (task.isNeedItem())
												e.getWhoClicked().getInventory()
														.remove(new ItemStack(task.getNeededItemMaterial(), 1));
											e.getWhoClicked().closeInventory();
											for (String cmd : task.getCommands())
												Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
														cmd.replaceAll("%playername%", e.getWhoClicked().getName()));
											pldata.giveGivedTaskReward(questdata.getS("QuestID"), task.getTaskID());
										}
									}
								}
							}
						}
					} catch (Exception ignore) {
						// TODO: handle exception
					}
				}
			}

		}
	}

	@EventHandler
	public void onNpcRightClick(NPCRightClickEvent e) {
		if (plugin.getDataFile(e.getClicker().getName()).getClickedNPC().contains(e.getNPC().getName()) == false)
			plugin.getDataFile(e.getClicker().getName()).addClickedNPC(e.getNPC().getName());
	}

}
