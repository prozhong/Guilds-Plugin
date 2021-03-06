package me.glaremasters.guilds.listeners;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import me.glaremasters.guilds.Main;
import org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by GlareMasters on 9/22/2017.
 */
public class AnnouncementListener implements Listener {

    public static Set<UUID> ALREADY_INFORMED = new HashSet<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String URL = "https://glaremasters.me/guilds/announcements/" + Main.getInstance().getDescription().getVersion();
        Player player = event.getPlayer();
        Main.getInstance().getServer().getScheduler()
                .scheduleSyncDelayedTask(Main.getInstance(), () -> {
                    if (Main.getInstance().getConfig().getBoolean("announcements.in-game")) {
                        if (player.isOp()) {
                            if (!ALREADY_INFORMED.contains(player.getUniqueId())) {
                                try {
                                    URL url = new URL(URL);
                                    URLConnection con = url.openConnection();
                                    con.setRequestProperty("User-Agent",
                                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                                    InputStream in = con.getInputStream();
                                    String encoding = con.getContentEncoding();
                                    encoding = encoding == null ? "UTF-8" : encoding;
                                    String body = IOUtils.toString(in, encoding);
                                    player.sendMessage(body);
                                    ALREADY_INFORMED.add(player.getUniqueId());
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }
                }, 70L);


    }

}
