package me.capit.Townshend.aegis;

import java.util.ArrayList;

import me.capit.Townshend.TownshendPlugin;

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
			if (a.locationProtectedByAeigs(e.getBlock().getLocation())){
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
	}
	
	@EventHandler
	public void onSignPlace(SignChangeEvent e){
		String[] lines = e.getLines();
		if (lines[0].equalsIgnoreCase("[AEGIS]")){
			// TODO
		}
	}
}
