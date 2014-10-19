package me.capit.Townshend.aegis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;

public class Aegis implements Serializable {
	private static final long serialVersionUID = 8282840445247050101L;
	public static final HashMap<Material, AegisModifier> mats = new HashMap<Material, AegisModifier>();
	public static final HashMap<Material, Integer> points = new HashMap<Material, Integer>();
	static {
		mats.put(Material.IRON_BLOCK, AegisModifier.IRON);
		
		points.put(Material.STONE, 8); // TODO Make this configurable.
	}
	
	private final Location center;
	private final int radius; // Height * 8
	private final AegisModifier modifier;
	private final String town;
	
	private final Chest chest;
	private final Sign sign;
	
	private int hp = 0;
	private ArrayList<Block> blocks = new ArrayList<Block>();

	public class AegisCreationException extends Exception{
		private static final long serialVersionUID = -833758527711233919L;
		public AegisCreationException(String msg){super(msg);}
	}
	public enum AegisModifier{
		IRON; // TODO!
	}
	
	public Aegis(Location loc, String town) throws AegisCreationException{
		this.town = town;
		Block sign = loc.getBlock();
		if (sign.getType()!=Material.WALL_SIGN){throw new AegisCreationException("Invalid creation call.");}
		
		Directional signBlock = (Directional) sign;
		Block base = sign.getRelative(signBlock.getFacing().getOppositeFace());
		Block chest = base.getRelative(BlockFace.UP);
		if (base.getType()!=Material.GOLD_BLOCK || base.getType()!=Material.CHEST){throw new AegisCreationException("Invalid base!");}
		center = base.getLocation();
		this.chest = (Chest) chest;
		this.sign = (Sign) sign;
		blocks.add(sign); blocks.add(base); blocks.add(chest);
		
		ArrayList<Block> bars = new ArrayList<Block>();
		Location chkLoc = chest.getLocation().add(0, 1, 0);
		while (chkLoc.getBlock().getType()==Material.IRON_FENCE){
			bars.add(chkLoc.getBlock());
			blocks.add(chkLoc.getBlock());
			chkLoc = chkLoc.add(0,1,0);
		}
		if (bars.size()<=0){throw new AegisCreationException("No iron bars found!");}
		radius = bars.size()*8;
		
		Block modifier = chkLoc.getBlock();
		if (!mats.containsKey(modifier.getType())){throw new AegisCreationException("Unknown modifier!");}
		this.modifier = mats.get(modifier.getType());
		blocks.add(modifier);
		
		updateSign();
	}
	
	public int getRadius(){
		return radius;
	}
	
	public String getTownName(){
		return town;
	}
	
	public Location getCenter(){
		return center;
	}
	
	public AegisModifier getModifier(){
		return modifier;
	}
	
	public int getHP(){
		return hp;
	}
	
	public void updateSign(){
		sign.setLine(0, modifier.toString().toUpperCase()+" AEGIS");
		sign.setLine(1, town);
		sign.setLine(2, hp+" HP");
		sign.setLine(3, "");
	}
	
	public boolean blockPartOfAegis(Block block){
		return blocks.contains(block);
	}
	
	public boolean locationProtectedByAeigs(Location loc){
		return Math.abs(center.getBlockX()-loc.getBlockX())<radius && Math.abs(center.getBlockZ()-loc.getBlockZ())<radius;
	}
	
	// Returns true if blocked, false if not.
	public boolean damage(){
		if (hp>0){
			hp--;
		} else {
			Inventory inv = chest.getInventory();
			ItemStack next = inv.getItem(0);
			for (int i=0; i<inv.getSize(); i++){
				next = inv.getItem(i);
				if (points.containsKey(next.getType())){break;}
			}
			if (!points.containsKey(next.getType())){return false;}
			hp = hp + points.get(next.getType());
			if (next.getAmount()>1){
				next.setAmount(next.getAmount()-1);
			} else {
				next.setType(Material.AIR);
			}
		}
		updateSign();
		return true;
	}
	
}
