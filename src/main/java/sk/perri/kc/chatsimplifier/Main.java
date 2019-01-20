package sk.perri.kc.chatsimplifier;

import com.google.common.base.Strings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin implements Listener
{
    static Main self;
    List<String> blacklist;

    public void onEnable()
    {
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        getConfig().options().copyDefaults(true);
        saveConfig();

        blacklist = getConfig().getStringList("blacklist");

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Plugin sa aktivoval");
    }

    public void onDisable()
    {
        getLogger().info("Plugin sa deaktivioval");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatMessage(AsyncPlayerChatEvent event)
    {
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

        StringBuilder res = new StringBuilder();

        for(String slovo : msg.split(" "))
        {
            res.append(res.length() > 0 ? " " : "");
            if(blacklist.contains(slovo))
                res.append(Strings.repeat(getConfig().getString("block-char"), slovo.length()));
            else
                res.append(slovo);
        }

        event.setMessage(res.toString());
    }
}
