package me.capit.Townshend.group;

import java.util.ArrayList;
import java.util.HashMap;

import me.capit.Townshend.Messager;
import me.capit.Townshend.TownshendPlugin;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCommands implements CommandExecutor {
	TownshendPlugin plugin;
	
	public GroupCommands(TownshendPlugin plugin) {
		this.plugin = plugin;
	}
	
	public static ArrayList<HashMap<String,String>> help = new ArrayList<HashMap<String,String>>();
	static {
		HashMap<String,String> pg1 = new HashMap<String,String>(); 
			pg1.put("[?help] [page]", "Displays this help.");
			pg1.put("create <group>", "Create's a group with name <group>. Must be Alphanumeric.");
			pg1.put("delete <group>", "Deletes the group <group> given that you own it.");
			pg1.put("add <player> <group>", "Adds player <player> to group <group>.");
			pg1.put("remove <player> <group>", "Kicks the player <player> from the group <group>.");
		help.add(pg1);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
		if (cmd.getName().equalsIgnoreCase("group") && s instanceof Player){
			if (args.length>0){
				if (args[0].equalsIgnoreCase("help")){
					if (args.length==1){
						displayHelp(s);
					} else {
						displayHelp(s,Integer.parseInt(args[1]));
					}
				} else if (args[0].equalsIgnoreCase("create")){
					if (args.length==2 && s.hasPermission("townshend.group.create")){
						if (TownshendPlugin.getGroupByName(args[1])==null){
							Group group = new Group(((Player) s).getUniqueId(), args[1]);
							TownshendPlugin.groups.add(group);
							Messager.sendInfo(s, "Successfully created group "+group.name+".");
						} else {
							Messager.sendError(s, "Group already exists.");
						}
					} else {
						Messager.sendError(s, "Invalid argument count or insufficient permissions.");
					}
				} else if (args[0].equalsIgnoreCase("delete")){
					if (args.length==2 && s.hasPermission("townshend.group.delete")){
						Group group = TownshendPlugin.getGroupByName(args[1]);
						if (group!=null && group.owner==((Player) s).getUniqueId()){
							TownshendPlugin.groups.remove(group);
							Messager.sendInfo(s, "Successfully deleted group.");
						} else {
							Messager.sendError(s, "Group does not exist or you do not own that group.");
						}
					} else {
						Messager.sendError(s, "Invalid argument count or insufficient permissions.");
					}
				} else if (args[0].equalsIgnoreCase("add")){
					if (args.length==3 && s.hasPermission("townshend.group.add")){
						Group group = TownshendPlugin.getGroupByName(args[1]);
						if (group!=null && group.owner==((Player) s).getUniqueId()){
							OfflinePlayer targ = plugin.getServer().getOfflinePlayer(args[2]);
							if (targ!=null){
								group.addMember(targ.getUniqueId());
								Messager.sendError(s, "Successfully added "+targ.getName()+" to group.");
							} else {
								Messager.sendError(s, "Unable to locate player.");
							}
						} else {
							Messager.sendError(s, "Group does not exist or you do not own that group.");
						}
					} else {
						Messager.sendError(s, "Invalid argument count or insufficient permissions.");
					}
				} else if (args[0].equalsIgnoreCase("remove")){
					if (args.length==3 && s.hasPermission("townshend.group.remove")){
						Group group = TownshendPlugin.getGroupByName(args[1]);
						if (group!=null && group.owner==((Player) s).getUniqueId()){
							OfflinePlayer targ = plugin.getServer().getOfflinePlayer(args[2]);
							if (targ!=null){
								group.removeMember(targ.getUniqueId());
								Messager.sendError(s, "Successfully removed "+targ.getName()+" from group.");
							} else {
								Messager.sendError(s, "Unable to locate player.");
							}
						} else {
							Messager.sendError(s, "Group does not exist or you do not own that group.");
						}
					} else {
						Messager.sendError(s, "Invalid argument count or insufficient permissions.");
					}
				} else {
					Messager.sendError(s, "Unknown subcommand. Try /group help.");
				}
			} else {
				return onCommand(s,cmd,lbl,new String[]{"help"});
			}
			return true;
		}
		return false;
	}
	
	public void displayHelp(CommandSender p, int page){
		if (page>0 && help.size()>=page){
			HashMap<String,String> data = help.get(page-1);
			Messager.sendRaw(p, TownshendPlugin.gHeader);
			for (String key : data.keySet()){
				Messager.sendRaw(p, ChatColor.YELLOW+"/town "+key);
				Messager.sendRaw(p, "      "+ChatColor.GRAY+data.get(key));
			}
			Messager.sendRaw(p, TownshendPlugin.gFooter);
		} else {
			Messager.sendError(p, "Unknown or invalid page number.");
		}
	}
	
	public void displayHelp(CommandSender p){
		displayHelp(p,1);
	}
}
