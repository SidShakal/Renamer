package me.ridan.renamer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private Pattern singleAmpersandPattern = Pattern.compile("(?<!&)&(?!&)");
	private String singleAmpersandReplacement = "§";
	private Pattern doubleAmpersandPattern = Pattern.compile("&&");
	private String doubleAmpersandReplacement = "&";
	
	private String handleAmpersands(String str){
		// Replace lone ampersands ('&') in input with section symbols ('§'),
		// unless an ampersand is followed by another ampersand, in which case
		// replace the pair with just one ampersand.
		// Then return the result.
		String transformedStr = str;
		
		Matcher singleAmpersandMatcher = singleAmpersandPattern.matcher(transformedStr);
		transformedStr = singleAmpersandMatcher.replaceAll(singleAmpersandReplacement);
		
		Matcher doubleAmpersandMatcher = doubleAmpersandPattern.matcher(transformedStr);
		transformedStr = doubleAmpersandMatcher.replaceAll(doubleAmpersandReplacement);
		
		return transformedStr;
	}
	
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
				sender.sendMessage(ChatColor.GREEN + "/renamer set - Set item's name");
				sender.sendMessage(ChatColor.GREEN + "/renamer setlore - Set item's lore");
				sender.sendMessage(ChatColor.GREEN + "/renamer help <command>");
				return true;
			}
			if(args[0].equalsIgnoreCase("set")){
				if(!sender.hasPermission("renamer.set")){
					sender.sendMessage(ChatColor.RED + "You don't have permission!");
					return true;
				}
				if(!(sender instanceof Player)){
					sender.sendMessage("Console cannot rename an item!");
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
				ItemMeta im = p.getInventory().getItemInHand().getItemMeta();
				String mes = args[1];
				for(int i = 2; i < args.length; i++){
					mes += " " + args[i];
				}
				mes = handleAmpersands(mes);
				im.setDisplayName(mes);
				p.getInventory().getItemInHand().setItemMeta(im);
				sender.sendMessage(ChatColor.DARK_GREEN + "Name set!");
				return true;
			}
			if(args[0].equalsIgnoreCase("setlore")){
				if(!sender.hasPermission("renamer.setlore")){
					sender.sendMessage(ChatColor.RED + "You don't have permission!");
					return true;
				}
				if(!(sender instanceof Player)){
					sender.sendMessage("Console cannot set lore of an item!");
					return true;
				}
				if(args.length<2){
					sender.sendMessage(ChatColor.RED + "Please specify a lore! (For next line, type \\n)");
					return true;
				}
				
				Player p = (Player) sender;
				ItemStack item = p.getItemInHand();
				if(item == null || item.getType() == Material.AIR){
					sender.sendMessage(ChatColor.DARK_GREEN + "Hand cannot be edited!");
					return true;
				}
				String mes = args[1];
				for(int i = 2; i < args.length; i++){
					mes += " " + args[i];
				}
				mes = handleAmpersands(mes);
				mes = mes.replace("%player%", sender.getName());
				String lore[] = mes.split("\\\\n");
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
					sender.sendMessage(ChatColor.DARK_GREEN + "Usage: /renamer set <name>");
					return true;
				}
				if(args[1].equalsIgnoreCase("setlore")){
					sender.sendMessage(ChatColor.DARK_GREEN + "Command setlore, Permission: renamer.setlore, Supports item color (& symbol)");
					sender.sendMessage(ChatColor.DARK_GREEN + "For next line, type \\n in the name. %player% gets replaced with sender's name");
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
