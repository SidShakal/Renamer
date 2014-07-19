package me.ridan.renamer;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Renamer extends JavaPlugin {
	
	public void onEnable(){
		Bukkit.getServer().getLogger().info("[Renamer] Renamer enabled!");
	}
	
	public void onDisable(){
		Bukkit.getServer().getLogger().info("[Renamer] Renamer disabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("renamer")){
			if(args.length==0){
				sender.sendMessage(ChatColor.AQUA + "=[Renamer]=");
				sender.sendMessage(ChatColor.GREEN + "/renamer - Main menu");
				sender.sendMessage(ChatColor.GREEN + "/renamer set - Set items name");
				sender.sendMessage(ChatColor.GREEN + "/renamer setlore - Set the lore of an item");
				sender.sendMessage(ChatColor.GREEN + "/renamer help <command>");
				return true;
			}
			if(args[0].equalsIgnoreCase("set")){
				if(!sender.hasPermission("renamer.set")){
					sender.sendMessage(ChatColor.RED + "You dont have permission!");
					return true;
				}
				if(!(sender instanceof Player)){
					sender.sendMessage("Console cannot rename a item!");
					return true;
				}
				if(args.length<2){
					sender.sendMessage(ChatColor.RED + "Please specify a name!");
					return true;
				}
				Player p = (Player) sender;
				ItemStack item = p.getItemInHand();
				if(item == null || item.getType() == Material.AIR){
					sender.sendMessage(ChatColor.DARK_GREEN + "Hand cannot be edited!");
					return true;
				}
				args[1] = args[1].replace("&", "§");
				ItemMeta im = p.getInventory().getItemInHand().getItemMeta();
				String mes = "";
				for(int i = 1; i < args.length; i++){
					mes += args[i] + " ";
				}
				im.setDisplayName(mes);
				p.getInventory().getItemInHand().setItemMeta(im);
				return true;
			}
			if(args[0].equalsIgnoreCase("setlore")){
				if(!sender.hasPermission("renamer.setlore")){
					sender.sendMessage(ChatColor.RED + "You dont have permission!");
					return true;
				}
				if(!(sender instanceof Player)){
					sender.sendMessage("Console cannot set lore of a item!");
					return true;
				}
				if(args.length<2){
					sender.sendMessage(ChatColor.RED + "Please specify a lore! (For next line, type /n)");
					return true;
				}
				
				Player p = (Player) sender;
				ItemStack item = p.getItemInHand();
				if(item == null || item.getType() == Material.AIR){
					sender.sendMessage(ChatColor.DARK_GREEN + "Hand cannot be edited!");
					return true;
				}
				String mes = "";
				for(int i = 1; i < args.length; i++){
					mes += args[i] + " ";
				}
				mes = mes.replace("&", "§");
				mes = mes.replace("%player%", sender.getName());
				String lore[] = mes.split("/n");
				sender.sendMessage(lore.length + "");
				ItemMeta im = p.getInventory().getItemInHand().getItemMeta();
				ArrayList<String> lorelist = new ArrayList<String>();
				for(int i = 0; i < lore.length; i++){
					lorelist.add(lore[i]);
				}
				im.setLore(lorelist);
				p.getInventory().getItemInHand().setItemMeta(im);
				sender.sendMessage(ChatColor.DARK_GREEN + "Lore set!");
				return true;
			}
			if(args[0].equalsIgnoreCase("help")){
				if(args.length<2){
					sender.sendMessage(ChatColor.RED + "Please specify a command!");
					return true;
				}
				if(args[1].equalsIgnoreCase("set")){
					sender.sendMessage(ChatColor.DARK_GREEN + "Command set, Permission: renamer.set, Supports item color (& symbol)");
					sender.sendMessage(ChatColor.DARK_GREEN + "Usage: /renamer set <Name>");
					return true;
				}
				if(args[1].equalsIgnoreCase("setlore")){
					sender.sendMessage(ChatColor.DARK_GREEN + "Command setlore, Permission: renamer.setlore, Supports item color (& symbol)");
					sender.sendMessage(ChatColor.DARK_GREEN + "For next line, type /n in the name. %player% gets replaced with sender's name");
					sender.sendMessage(ChatColor.DARK_GREEN + ",, gets replaced with a space");
					sender.sendMessage(ChatColor.DARK_GREEN + "Usage: /renamer setlore <lore>");
					return true;
				}
				sender.sendMessage(ChatColor.RED + "" + args[1] + " not found! Use: set, setlore");
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "Unknown command! Do /renamer for help");
		return true;
	}
	
}
