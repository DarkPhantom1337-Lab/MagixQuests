package ua.darkphantom1337.magixquests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuestTask {

	private QuestFile quest;
	private Main plugin;
	private Integer taskslot;

	public QuestTask(QuestFile quest, Integer taskslot) {
		this.quest = quest;
		this.plugin = Main.getInstance();
		this.taskslot = taskslot;
	}

	public int getNeedPlayerKills() {
		return quest.getInt("GUI.TaskItems." + taskslot + ".NeedPlayerKills");
	}

	public int getNeedMobKills() {
		return quest.getInt("GUI.TaskItems." + taskslot + ".NeedMobKills");
	}

	public boolean isNeedItem() {
		return quest.getB("GUI.TaskItems." + taskslot + ".NeedItem");
	}

	public Material getNeededItemMaterial() {
		return Material.valueOf(quest.getS("GUI.TaskItems." + taskslot + ".NeededItem.Material"));
	}

	public int getNeededItemAmount() {
		return quest.getInt("GUI.TaskItems." + taskslot + ".NeededItem.Amount");
	}

	public boolean isNeedNPC() {
		return quest.getB("GUI.TaskItems." + taskslot + ".NeedNPC");
	}

	public String getNeededNPCName() {
		return quest.getS("GUI.TaskItems." + taskslot + ".NeededNPC.Name");
	}

	public boolean isNeedTask() {
		return quest.getB("GUI.TaskItems." + taskslot + ".NeedTask");
	}

	public List<String> getNeededTasksID() {
		return quest.getSL("GUI.TaskItems." + taskslot + ".NeededTask");
	}

	public List<String> getCommands() {
		return quest.getSL("GUI.TaskItems." + taskslot + ".Commands");
	}

	public String getTaskID() {
		return quest.getS("GUI.TaskItems." + taskslot + ".TaskID");
	}

	public String getTaskType() {
		return quest.getS("GUI.TaskItems." + taskslot + ".TaskType");
	}

	public boolean isYesItem(Player p) {
		if (isNeedItem()) {
			return p.getInventory().contains(getNeededItemMaterial(), getNeededItemAmount());
		} else
			return true;
	}

	public boolean isYesNPC(Player p) {
		if (isNeedNPC()) {
			return plugin.getDataFile(p.getName()).getClickedNPC().contains(getNeededNPCName());
		} else
			return true;
	}

	public boolean isÑompletedRequirements(Player p) {
		PlayerDataFile playerdata = plugin.getDataFile(p.getName());
		Boolean needplkill = false, needmobkill = false, needitem = false, neednpc = false, needtask = false;
		if (playerdata.getKilledPlayers() >= getNeedPlayerKills())
			needplkill = true;
		if (playerdata.getKilledMobs() >= getNeedMobKills())
			needmobkill = true;
		if (isNeedItem()) {
			if (p.getInventory().contains(getNeededItemMaterial(), getNeededItemAmount()))
				needitem = true;
		} else
			needitem = true;
		if (isNeedNPC()) {
			if (playerdata.getClickedNPC().contains(getNeededNPCName()))
				neednpc = true;
		} else
			neednpc = true;
		if (isNeedTask()) {
			if (getNeededTasksID().isEmpty())
				needtask = true;
			else {
				needtask = true;
				for (String taskid : getNeededTasksID()) {
					if (getTaskTypeForTaskID(taskid).equals("USUAL")) {
						if (playerdata.isCompletedTask(quest.getS("QuestID"), taskid) == false) {
							needtask = false;
							break;
						}
					} else {
						if (playerdata.isCompletedEveryDayTask(quest.getS("QuestID"), taskid,
								new SimpleDateFormat("dd/MM/yyyy").format(new Date())) == false) {
							needtask = false;
							break;
						}
					}
				}
			}
		} else
			needtask = true;
		return needplkill && needmobkill && needitem && neednpc && needtask;
	}

	public ItemStack getTaskItem(Player p) {
		PlayerDataFile playerdata = plugin.getDataFile(p.getName());
		if (getTaskType().equals("USUAL")) {
			if (playerdata.isCompletedTask(quest.getS("QuestID"), getTaskID()))
				if (playerdata.isGivedReward(quest.getS("QuestID"), getTaskID()))
					return plugin.cfg.getCompletedTaskItem();
				else
					return quest.getItem("GUI.TaskItems." + taskslot);
		} else {
			if (playerdata.isCompletedEveryDayTask(quest.getS("QuestID"), getTaskID(),
					new SimpleDateFormat("dd/MM/yyyy").format(new Date())))
				if (playerdata.isGivedReward(quest.getS("QuestID"), getTaskID()))
					return plugin.cfg.getCompletedTaskItem();
				else
					return quest.getItem("GUI.TaskItems." + taskslot);
		}
		if (!isNeedTask())
			return quest.getItem("GUI.TaskItems." + taskslot);
		else {
			if (getNeededTasksID().isEmpty())
				return quest.getItem("GUI.TaskItems." + taskslot);
			else
				for (String taskid : getNeededTasksID())
					if (getTaskTypeForTaskID(taskid).equals("USUAL")) {
						if (!playerdata.isCompletedTask(quest.getS("QuestID"), taskid))
							return plugin.cfg.getLockTaskItem();
					} else if (!playerdata.isCompletedEveryDayTask(quest.getS("QuestID"), taskid,
							new SimpleDateFormat("dd/MM/yyyy").format(new Date())))
						return plugin.cfg.getLockTaskItem();
			return quest.getItem("GUI.TaskItems." + taskslot);
		}
	}

	public String getTaskTypeForTaskID(String taskid) {
		for (int i = 0; i < quest.getInt("GUI.Size"); i++)
			if (quest.isTaskItem(i) && quest.getS("GUI.TaskItems." + i + ".TaskID").equals(taskid))
				return quest.getS("GUI.TaskItems." + i + ".TaskType");
		return "USUAL";
	}

}
