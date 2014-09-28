package me.capit.Townshend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.capit.Townshend.town.Town;
import me.capit.Townshend.town.Town.TownCreationException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCommands implements CommandExecutor {
	TownshendPlugin plugin;
	
	public TownCommands(TownshendPlugin plugin) {
		this.plugin = plugin;
	}
	
	public static List<HashMap<String,String>> help = new ArrayList<HashMap<String,String>>();
	static {
		HashMap<String,String> pg1 = new HashMap<String,String>(); 
			pg1.put("[?help] [page]", "Displays this help.");
			pg1.put("create <name>", "Create's a town with name <name>. Must be Alphanumeric.");
			pg1.put("delete", "Deletes your town if you currently own it.");
			pg1.put("info [town=you]", "Provides information on a town.");
			pg1.put("list [page=1]", "Lists all of the towns in order of ID.");
		help.add(pg1);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
		if (cmd.getName().equalsIgnoreCase("townshend")){
			if (args.length>0){
				if (args[0].equalsIgnoreCase("help")){
					if (args.length==1){
						displayHelp(s);
					} else {
						displayHelp(s,Integer.parseInt(args[1]));
					}
				} else if (args[0].equalsIgnoreCase("create")|| args[0].equalsIgnoreCase("new")){
					if (s instanceof Player && s.hasPermission("townshend.player.create")){
						if (args.length==2){
							if (TownshendPlugin.getTownOfPlayer(((Player) s).getUniqueId())==null){
								if (TownshendPlugin.getTownByName(args[1])==null && args[1].matches("^[A-Za-z0-9_ ]{6,16}$")){
									try {
										Town t = new Town(plugin, ((Player) s).getUniqueId(), args[1]);
										TownshendPlugin.towns.add(t);
										t.save();
										Messager.sendSuccess(s, "Created town "+ChatColor.YELLOW+t.getName()+ChatColor.WHITE+".");
										TownshendPlugin.console.sendMessage(ChatColor.YELLOW+s.getName()+ChatColor.WHITE+
												" created town "+ChatColor.YELLOW+t.getName()+ChatColor.WHITE+".");
									} catch (TownCreationException| IOException e) {
										Messager.sendError(s, "There was an error forming the data file for your town.");
										Messager.sendRaw(s, "      Please let an administrator know this happened.");
										e.printStackTrace();
									}
								} else {
									Messager.sendError(s, "Sorry, the name '"+args[1]+"' is already taken or invalid.");
								}
							} else {
								Messager.sendError(s, "You're already in a town!");
							}
						} else {
							Messager.sendError(s, "Invalid argument count. Try "+ChatColor.YELLOW
									+"/town help"+ChatColor.WHITE+".");
						}
					} else {
						Messager.sendError(s, "Sorry, you can't create a town.");
					}
				} else if (args[0].equalsIgnoreCase("disband") || args[0].equalsIgnoreCase("delete")){
					if (s instanceof Player && s.hasPermission("townshend.player.create")){
						if (args.length==1){
							Town t = TownshendPlugin.getTownOfPlayer(((Player) s).getUniqueId());
							if (t.isOwner(((Player) s).getUniqueId())){
								TownshendPlugin.deleteTown(t);
								TownshendPlugin.console.sendMessage(ChatColor.YELLOW+s.getName()+ChatColor.WHITE+
										" deleted town "+ChatColor.YELLOW+t.getName()+ChatColor.WHITE+".");
							} else {
								Messager.sendError(s, "Only the owner can delete towns.");
							}
						} else {
							// TODO Admin delete.
							Messager.sendDebug(s, "TODO: Admin town deletion.");
						}
					}
				} else if (args[0].equalsIgnoreCase("info")){
					if (s.hasPermission("townshend.player.info")){
						if (args.length==1){
							Town town = TownshendPlugin.getTownOfPlayer(((Player) s).getUniqueId());
							if (town!=null){
								return onCommand(s,cmd,lbl,new String[]{"info", town.getName()});
							} else {
								Messager.sendError(s, "You're not currently in a town.");
							}
						} else {
							Town town = TownshendPlugin.getTownByName(args[1]);
							if (town!=null){
								Messager.sendRaw(s, TownshendPlugin.gHeader);
								Messager.sendRaw(s, ChatColor.YELLOW+town.getName()+": "+
										ChatColor.GRAY+"\""+town.getDesc()+"\"");
								String plist = "";
								for (UUID id : town.getPlayers()){
									OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(id);
									String color = p.isOnline() ? "a" : (p.isBanned() ? "c" : "f");
									String tag = town.isOwner(id) ? plugin.getConfig().getString("TOWNS.OWNER_TAG") : 
										(town.isMod(id) ? plugin.getConfig().getString("TOWNS.MODERATOR_TAG") : 
											plugin.getConfig().getString("TOWNS.MEMBER_TAG"));
									plist += "&"+color+tag+p.getName()+", ";
								}
								plist = plist.substring(0, plist.length()-2);
								Messager.sendRaw(s, ChatColor.translateAlternateColorCodes('&', 
										"&eMembers: &f"+plist));
							} else {
								Messager.sendError(s, "That town does not exist.");
							}
						}
					}
				} else {
					Messager.sendError(s, "Unknown argument.");
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
