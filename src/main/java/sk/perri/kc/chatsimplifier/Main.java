package sk.perri.kc.chatsimplifier;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener, CommandExecutor
{
    static Main self;
    List<String> blacklist;
    List<String> busy = new ArrayList<>();

    public void onEnable()
    {
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        getConfig().options().copyDefaults(true);
        saveConfig();

        blacklist = getConfig().getStringList("blacklist");

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("busy").setExecutor(this);

        getLogger().info("Plugin sa aktivoval");
    }

    public void onDisable()
    {
        getLogger().info("Plugin sa deaktivioval");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(AsyncPlayerChatEvent event)
    {

        // Check multiple chars
        String msg = event.getMessage();
        StringBuilder sb = new StringBuilder();

        char last = '0';
        int count = 0;
        for(char s : msg.toCharArray())
        {
            if(s == last)
                count++;
            else
            {
                last = s;
                count = 1;
            }

            if(count >= getConfig().getInt("char-trash-holder"))
                continue;

            sb.append(s);
        }
        msg = sb.toString();

        // Check blacklist
        StringBuilder res = new StringBuilder();

        for(String slovo : msg.split(" "))
        {
            res.append(res.length() > 0 ? " " : "");
            if(blacklist.contains(ChatColor.stripColor(slovo)))
                res.append(Strings.repeat(getConfig().getString("block-char"), slovo.length()));
            else
                res.append(slovo);
        }

        // Check busy
        boolean cancel[] = {false};

        for(String slovo : msg.split(" "))
        {
            busy.forEach(k ->
            {
                if(slovo.toLowerCase().contains(k))
                {
                    cancel[0] = true;
                }
            });
        }

        if(cancel[0] && !event.getPlayer().hasPermission("chatsimplifier.overbusy"))
        {
            event.getPlayer().sendMessage(
                ChatColor.translateAlternateColorCodes('&', getConfig().getString("msg.busy-msg")));
            event.setCancelled(true);
        }

        event.setMessage(res.toString());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("busy"))
        {
            if(sender.hasPermission("chatsimplifier.busy"))
            {
                if(busy.contains(sender.getName().toLowerCase()))
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        getConfig().getString("msg.busy-off")));
                    busy.remove(sender.getName().toLowerCase());
                }
                else
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        getConfig().getString("msg.busy-on")));
                    busy.add(sender.getName().toLowerCase());
                }
            }
        }

        return true;
    }
}
