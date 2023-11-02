package im.wma.dev.MobMortalityMessages;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MobMortalityMessages extends JavaPlugin {
    public HashMap<Player, Boolean> enabledMap;

    private PluginDescriptionFile pdf;
    private String version;
    private String name;
    private String author;
    private DamageHandler damageHandler;

    @Override
    public void onEnable() {
        pdf = this.getDescription();
        version = pdf.getVersion();
        name = pdf.getName();
        author = pdf.getAuthors().get(0);

        Bukkit.getLogger().info(name + " v" + version + " enabled!");
        enabledMap = new HashMap<Player, Boolean>();
        damageHandler = new DamageHandler(this);

        //Register the DamageHandler as a listener for events
        Bukkit.getServer().getPluginManager().registerEvents(damageHandler, this);

        // Anonymous implementation of "/mmm" root command.
        CommandBase<MobMortalityMessages> mmmCommand = new CommandBase<MobMortalityMessages>(this) {
            @Override
            public boolean runCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
                sender.sendMessage("MobMortalityMessages v" + getPlugin().getDescription().getVersion());
                return true;
            }

            @Override
            public List<String> tabCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
                return null;
            }
        };

        CommandToggle commandToggle = new CommandToggle(this);
        mmmCommand.registerSubCommand("toggle", commandToggle);

        CommandVersion commandReload = new CommandVersion(this);
        mmmCommand.registerSubCommand("version", commandReload);


        // Register "/check" command executor with Bukkit.
        getCommand("mmm").setExecutor(mmmCommand);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(name + " v" + version + " disabled!");
        enabledMap = null;
        pdf = null;

        //Unregister the listener
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
    }

    public class CommandToggle extends CommandBase<MobMortalityMessages> {
        public CommandToggle(MobMortalityMessages plugin) {
            super(plugin);
        }

        @Override
        public boolean runCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
            if (sender.hasPermission("mmm.messages")) {
                if (sender instanceof Player) {
                    addPlayer((Player) sender);
                    return true;
                } else {
                    sender.sendMessage("This command can only be run as a player!");
                    return false;
                }
            } else {
                sender.sendMessage("You don't have permission for that!");
            }
            return true;
        }

        @Override
        public List<String> tabCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
            return null;
        }
    }

    public class CommandVersion extends CommandBase<MobMortalityMessages> {
        public CommandVersion(MobMortalityMessages plugin) {
            super(plugin);
        }

        @Override
        public boolean runCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
            if (sender.hasPermission("mmm.messages")) {
                sender.sendMessage(name);
                sender.sendMessage(version);
                sender.sendMessage(author);
            } else {
                sender.sendMessage("You don't have permission for that!");
            }
            return true;
        }

        @Override
        public List<String> tabCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
            return null;
        }
    }

    private void addPlayer(Player aPlayer) {
        //If they're already in the map then toggle the bool
        if (enabledMap.containsKey((aPlayer))) {
            Boolean flag = enabledMap.get(aPlayer);
            flag = !flag;
            enabledMap.put(aPlayer, flag);

            aPlayer.sendMessage("Mob health messages " + (flag ? "enabled." : "disabled."));
        } else    //Otherwise add them and set the bool to true
        {
            enabledMap.put(aPlayer, true);

            aPlayer.sendMessage("Mob health messages enabled.");
        }
    }
}

