package me.capit.Townshend.group;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import me.capit.Townshend.TownshendPlugin;

public class Group implements Serializable {
	private static final long serialVersionUID = -2583730591598246831L;
	
	public class GroupCreationException extends Exception{
		private static final long serialVersionUID = -3697798468000643398L;
		public GroupCreationException(String msg){super(msg);}
	}
	
	public final UUID owner;
	public final String name;
	public final File FILE;
	
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	public Group(TownshendPlugin plugin, String fname) throws GroupCreationException{
		File file = new File(plugin.getDataFolder()+File.separator+"groups"+File.separator+fname);
		if (file.exists()){
			FILE = file;
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			owner = UUID.fromString(config.getString("owner"));
			name = config.getString("name");
			for (String str : config.getStringList("members")){
				members.add(UUID.fromString(str));
			}
			save();
		} else {
			throw new GroupCreationException("Could not load specified group '"+fname+"'");
		}
	}
	
	public Group(TownshendPlugin plugin, UUID owner, String name) throws IOException{
		this.owner = owner;
		this.name = name.toUpperCase();
		members.add(owner);
		
		File file = new File(plugin.getDataFolder()+File.separator+"groups"+File.separator+name.toUpperCase()+".yml");
		file.createNewFile();
		FILE = file;
		
		save();
	}
	
	public void addMember(UUID player){
		members.add(player);
	}
	
	public void removeMember(UUID player){
		members.remove(player);
	}
	
	public boolean isMember(UUID player){
		return members.contains(player);
	}
	
	public ArrayList<UUID> getMembers(){
		return members;
	}
	
	public void save(){
		YamlConfiguration config = YamlConfiguration.loadConfiguration(FILE);
		config.set("owner", owner.toString());
		config.set("name", name);
		
		List<String> ms = new ArrayList<String>();
		for (UUID id : members){
			ms.add(id.toString());
		}
		config.set("members", ms);
		try {
			config.save(FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
