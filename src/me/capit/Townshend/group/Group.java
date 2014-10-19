package me.capit.Townshend.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Group implements Serializable {
	private static final long serialVersionUID = -2583730591598246831L;
	
	public final UUID owner;
	public final String name;
	
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	public Group(UUID owner, String name){
		this.owner = owner;
		this.name = name.toUpperCase();
		members.add(owner);
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
}
