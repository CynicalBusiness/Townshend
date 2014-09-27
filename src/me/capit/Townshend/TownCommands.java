package me.capit.Townshend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TownCommands implements CommandExecutor {
	TownshendPlugin plugin;
	
	public TownCommands(TownshendPlugin plugin) {
		this.plugin = plugin;
	}
	
	public static List<HashMap<String,String>> help = new ArrayList<HashMap<String,String>>();
	static {
		HashMap<String,String> pg1 = new HashMap<String,String>(); 
			pg1.put("[?help] [page]", "Displays this help.");
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
