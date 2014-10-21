package me.capit.Townshend.aegis;

import java.io.File;
import java.util.ArrayList;

import me.capit.Townshend.Messager;
import me.capit.Townshend.TownshendPlugin;
import me.capit.Townshend.aegis.Aegis.AegisCreationException;
import me.capit.Townshend.group.Group;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class AegisHandler implements Listener {
	TownshendPlugin plugin;
	
	public AegisHandler(TownshendPlugin p){
		plugin=p;
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent e){
		ArrayList<Aegis> protections = new ArrayList<Aegis>();
		for (Aegis a : TownshendPlugin.aegises){
			if (a.locationProtectedByAeigs(e.getBlock().getLocation()) 
					&& TownshendPlugin.getGroupByName(a.getGroup())!=null
					&& !TownshendPlugin.getGroupByName(a.getGroup()).isMember(e.getPlayer().getUniqueId())){
				protections.add(a);
			}
		}
		if (protections.size()>=1){
			Aegis lowest = null;
			for (Aegis a : protections){
				if (lowest==null || (lowest.getHP()>a.getHP() && a.getHP()>0)) lowest=a;
			}
			if (lowest!=null && lowest.damage()) e.setCancelled(true);
		}
		if (!e.isCancelled()){
			Block b = e.getBlock();
			Aegis aegis = null;
			for (Aegis a : TownshendPlugin.aegises){
				if (a.blockPartOfAegis(b)){aegis = a; break;}
			}
			if (aegis!=null){
				TownshendPlugin.aegises.remove(aegis);
				File file = new File(plugin.getDataFolder().getPath()+File.separator+aegis.ID+".yml");
				if (file.exists()) file.delete();
			}
		}
	}
	
	@EventHandler
	public void onSignPlace(SignChangeEvent e){
		String[] lines = e.getLines();
		if (lines[0].equalsIgnoreCase("[AEGIS]")){
			Group g = TownshendPlugin.getGroupByName(lines[1]);
			if (g!=null){
				try {
					Aegis a = new Aegis(e.getBlock().getLocation(), g.name);
					TownshendPlugin.aegises.add(a);
				} catch (AegisCreationException ex){
					Messager.sendError(e.getPlayer(), ex.getLocalizedMessage());
				}
			}
		}
	}
}
