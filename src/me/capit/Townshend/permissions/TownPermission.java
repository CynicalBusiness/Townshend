package me.capit.Townshend.permissions;

public class TownPermission {
	private final String[] permData;
	
	public TownPermission(String permission){
		permData = permission.split(".");
	}
	
	@Override
	public String toString(){
		String base = "";
		for (String s : permData){
			base+=s+".";
		}
		return base.substring(0, base.length()-2);
	}
	
	public String[] getRawData(){
		return permData;
	}
	
	public boolean matches(TownPermission perm){
		for (int i=0; i<Math.max(permData.length, perm.getRawData().length); i++){
			if (permData.length-1>=i && perm.getRawData().length-1>=i){
				if (permData[i].equalsIgnoreCase("*") || perm.getRawData()[i].equalsIgnoreCase("*")) return true;
				if (!permData[i].equalsIgnoreCase(perm.getRawData()[i])) return false;
			}
		}
		return true;
	}
	
	public boolean matches(String perm){
		return matches(new TownPermission(perm));
	}
	
}
