package io.github.spaicygaming.particleshapes.listeners;

import io.github.spaicygaming.particleshapes.Message;
import io.github.spaicygaming.particleshapes.Triangle;
import io.github.spaicygaming.particleshapes.TrianglesManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private final ItemStack gadgetItemStack;
    private final TrianglesManager trianglesManager;

    public PlayerInteractListener(ItemStack gadgetItemStack, TrianglesManager trianglesManager) {
        this.gadgetItemStack = gadgetItemStack;
        this.trianglesManager = trianglesManager;
    }

    @SuppressWarnings("unused")
    @EventHandler
    void onPlayerClickBlock(PlayerInteractEvent event) {
        // Avoids double execution (this event is triggered for both right and left hands)
        if (event.getHand() != EquipmentSlot.HAND)
            return;

        // Returns if the player didn't right-click a block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Returns if the item in hand isn't the gadget item
        ItemStack itemInHand = event.getItem();
        if (itemInHand == null || !itemInHand.isSimilar(gadgetItemStack)) {
            return;
        }

        //noinspection ConstantConditions
        Location clickedLocation = event.getClickedBlock().getLocation();

        // todo add config option to set max vertexes distance

        Player player = event.getPlayer();
        Optional<Triangle> triangleOptional = handleVertexRegistration(player, clickedLocation);

        if (triangleOptional.isPresent()) {
            Triangle triangle = triangleOptional.get();

            // Sends message if the vertex has been set with success
            Message.VERTEX_SET_SUCCESS.sendTo(player,
                    "{x}", String.valueOf(clickedLocation.getX()),
                    "{y}", String.valueOf(clickedLocation.getY()),
                    "{z}", String.valueOf(clickedLocation.getZ()));

            if (triangle.draw()) {
                Message.TRIANGLE_DRAWN.sendTo(player);
            }
        }

    }

    /**
     * Handles a triangle vertex registration.
     * Sends messages to the given player.
     *
     * @param player         the player who tries to register a new vertex
     * @param vertexLocation the Location of the new vertex
     * @return an empty optional if the given location was already a vertex of player's triangle
     */
    private Optional<Triangle> handleVertexRegistration(Player player, Location vertexLocation) {
        // The triangle to associate with the player
        Optional<Triangle> putTriangle = Optional.empty();

        UUID playerUuid = player.getUniqueId();
        Optional<Triangle> triangleOptional = trianglesManager.getTriangle(playerUuid);
        if (triangleOptional.isPresent()) {
            Triangle currentTriangle = triangleOptional.get();

            if (currentTriangle.isAVertex(vertexLocation)) {
                // Notifies that the given point is already a vertex
                Message.VERTEX_SET_ALREADY.sendTo(player);
            } else if (!Objects.equals(vertexLocation.getWorld(), currentTriangle.getWorld())) {
                // The new vertex is in a different world than the current triangle
                Message.VERTEX_DIFFERENT_WORLD.sendTo(player);
                // Cancels the current triangle drawing and creates a new triangle instance with only one vertex
                currentTriangle.stopDrawing();
                putTriangle = Optional.of(new Triangle(null, null, vertexLocation));
            } else {
                // Cancels the current triangle drawing and creates a new triangle instance with the new vertex
                currentTriangle.stopDrawing();
                putTriangle = Optional.of(currentTriangle.changeVertex(vertexLocation));
            }
        } else {
            // Creates a new triangle instance with only one vertex
            putTriangle = Optional.of(new Triangle(null, null, vertexLocation));
        }

        // Puts the triangle in the map if something changed
        putTriangle.ifPresent(triangle -> trianglesManager.setTriangle(playerUuid, triangle));

        return putTriangle;
    }

}