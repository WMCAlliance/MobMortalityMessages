package im.wma.dev.MobMortalityMessages;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageHandler implements Listener
{
	// Reference to the main plugin
	private MobMortalityMessages plugin;
	
	public DamageHandler(MobMortalityMessages p)
	{
		plugin = p;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent evt)
	{
		// Make sure we're dealing with a player attacking a mob
		if (!(evt.getDamager() instanceof Player))
			return;
		if (!(evt.getEntity() instanceof Creature))
			return;
		
		Player player = (Player) evt.getDamager();
		Creature mob = (Creature) evt.getEntity();
		
		//Makes all negatives into zeroes
		int finalHealth = (mob.getHealth()-evt.getDamage() < 0 ? 0 : mob.getHealth()-evt.getDamage());
		
		// If the player has set themselves to receive mob health messages
		if (plugin.enabledMap.containsKey(player))
		{
			if (plugin.enabledMap.get(player)==true)
			{
				player.sendMessage(mob.getType()+" health: "+finalHealth+'/'+mob.getMaxHealth()+", took "+evt.getDamage()+" damage.");
			}
		}
	}
}
