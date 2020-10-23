package io.github.spaicygaming.particleshapes.listeners;

import io.github.spaicygaming.particleshapes.TrianglesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final TrianglesManager trianglesManager;

    public PlayerQuitListener(TrianglesManager trianglesManager) {
        this.trianglesManager = trianglesManager;
    }

    @SuppressWarnings("unused")
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // remove player's triangle if exists
        trianglesManager.removeTriangle(player.getUniqueId());
    }

}
