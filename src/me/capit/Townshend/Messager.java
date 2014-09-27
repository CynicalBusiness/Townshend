package me.capit.Townshend;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messager {
	public enum MessageLevel{
		INFO, DEBUG, WARNING, ERROR;
		
		public ChatColor color(){
			switch(this){
			case WARNING:
				return ChatColor.YELLOW;
			case ERROR:
				return ChatColor.DARK_RED;
			default:
				return ChatColor.WHITE;
			}
		}
	}
	
	public static void send(MessageLevel l, CommandSender p, String msg){
		String prefix = l.color()+l.toString()+": "+ChatColor.WHITE;
		p.sendMessage(prefix+msg);
	}
	
	public static void sendRaw(CommandSender p, String msg){
		p.sendMessage(msg);
	}
	
	public static void sendInfo(CommandSender p, String msg){
		send(MessageLevel.INFO, p, msg);
	}
	public static void sendWarning(CommandSender p, String msg){
		send(MessageLevel.WARNING, p, msg);
	}
	public static void sendDebug(CommandSender p, String msg){
		send(MessageLevel.DEBUG, p, msg);
	}
	public static void sendError(CommandSender p, String msg){
		send(MessageLevel.ERROR, p, msg);
	}
	
}
