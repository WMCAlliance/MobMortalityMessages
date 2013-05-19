package im.wma.dev.MobMortalityMessages;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MobMortalityMessages extends JavaPlugin
{
	public HashMap<Player, Boolean> enabledMap;
	
	private PluginDescriptionFile pdf;
	private String version;
	private String name;
	private String author;
	private DamageHandler damageHandler;
	
	@Override
	public void onEnable()
	{
		pdf = this.getDescription();
		version = pdf.getVersion();
		name = pdf.getName();
		author = pdf.getAuthors().get(0);
		
		Bukkit.getLogger().info(name+" v"+version+" enabled!");
		enabledMap = new HashMap<Player, Boolean>();
		damageHandler = new DamageHandler(this);
		
		//Register the DamageHandler as a listener for events
		Bukkit.getServer().getPluginManager().registerEvents(damageHandler, this);
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getLogger().info(name+" v"+version+" disabled!");
		enabledMap = null;
		pdf = null;
		
		//Unregister the listener
		EntityDamageByEntityEvent.getHandlerList().unregister(this);
	}

	@Override
	public boolean onCommand(CommandSender aSender, Command aCmd, String aLabel, String[] aArgs)
	{

		//Check they've got permission to do this
		if (!aSender.hasPermission("mmm.messages"))
		{
			aSender.sendMessage("You don't have permission for that!");
			return false;
		}
		
		//The command we're interested in
		if (aCmd.getName().equalsIgnoreCase("mmm"))
		{
			if (aArgs.length < 1)
			{
				aSender.sendMessage("Incorrect number of parameters.");
				return false;
			}
			
			if (aArgs[0].equalsIgnoreCase("toggle"))
			{
				if (aSender instanceof Player)
				{
					addPlayer((Player) aSender);
					return true;
				}
				else
				{
					aSender.sendMessage("This command can only be run as a player!");
					return false;
				}
			}
			
			if (aArgs[0].equalsIgnoreCase("version"))
			{
				aSender.sendMessage(name);
				aSender.sendMessage(version);
				aSender.sendMessage(author);
			}
		}
		
		return false;
	}

	private void addPlayer(Player aPlayer)
	{
		//If they're already in the map then toggle the bool
		if (enabledMap.containsKey((aPlayer)))
		{
			Boolean flag = enabledMap.get(aPlayer);
			flag = !flag;
			enabledMap.put(aPlayer, flag);
			
			aPlayer.sendMessage("Mob health messages "+(flag ? "enabled." : "disabled."));
		}
		else	//Otherwise add them and set the bool to true
		{
			enabledMap.put(aPlayer, true);
			
			aPlayer.sendMessage("Mob health messages enabled.");
		}
	}
}
