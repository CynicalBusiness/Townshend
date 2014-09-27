package me.capit.Townshend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.capit.Townshend.town.Town;
import me.capit.Townshend.town.Town.TownCreationException;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TownshendPlugin extends JavaPlugin {
	public static List<Town> towns = new ArrayList<Town>();
	public static Logger logger = null;
	public static ConsoleCommandSender console = null;
	
	public static final String header = ChatColor.DARK_GRAY+"#"+ChatColor.YELLOW+"-----"+
			ChatColor.DARK_GRAY+"["+ChatColor.WHITE+" Townshend "+ChatColor.DARK_GRAY+"]"
			+ChatColor.YELLOW+"------------------------------------------"+ChatColor.DARK_GRAY+"#";
	
	public static final String footer = ChatColor.DARK_GRAY+"#"+
			ChatColor.YELLOW+"------------------------------------------------------------"+
			ChatColor.DARK_GRAY+"#";
	
	public static File townsDir = null;
	
	@Override
	public void onEnable(){
		logger = getLogger();
		console = this.getServer().getConsoleSender();
		
		console.sendMessage(header);
		
		console.sendMessage(ChatColor.WHITE+"Loading "+ChatColor.YELLOW+"file "+ChatColor.WHITE+"data...");
		saveDefaultConfig();
		reloadConfig();
		
		townsDir = new File(this.getDataFolder().getAbsolutePath()+File.separator+"towns");
		if (!townsDir.exists() || !townsDir.isDirectory()){
			console.sendMessage(ChatColor.WHITE+"Looks like this is a first-time run. "+ChatColor.LIGHT_PURPLE+"Hang on...");
			townsDir.mkdir();
		}
		
		for (File town : townsDir.listFiles()){
			try {
				Town t = new Town(this, town.getName());
				towns.add(t);
				console.sendMessage(ChatColor.WHITE+" > Loaded data for "+ChatColor.LIGHT_PURPLE
						+t.getName()+ChatColor.WHITE+".");
			} catch (TownCreationException e) {
				e.printStackTrace();
			}
		}
		
		console.sendMessage(ChatColor.WHITE+"Done loading!");
		
		console.sendMessage(footer);
	}
	
	@Override
	public void onDisable(){
		console.sendMessage(header);
		
		console.sendMessage(ChatColor.WHITE+"Wrapping up core data...");
		
		console.sendMessage(ChatColor.WHITE+"Wrapped up.");
		
		console.sendMessage(footer);
	}
	
	public static int getNextID(){
		int lowest = 0;
		for (Town town : towns){
			lowest = town.ID>lowest ? town.ID : lowest;
		}
		return lowest;
	}
}
