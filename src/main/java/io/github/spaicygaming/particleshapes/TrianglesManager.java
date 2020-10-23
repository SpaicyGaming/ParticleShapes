package io.github.spaicygaming.particleshapes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Wraps a Map containing all triangles associated to the players who drew them
 */
public class TrianglesManager {

    private final Map<UUID, Triangle> triangles = new HashMap<>();

    /**
     * Gets the triangle drawn by a player
     *
     * @param playerUuid the uuid of the player
     * @return an empty optional if there isn't a triangle associated to the player
     */
    public Optional<Triangle> getTriangle(UUID playerUuid) {
        return Optional.ofNullable(triangles.get(playerUuid));
    }

    /**
     * Associates a triangle to a player
     *
     * @param playerUuid the uuid of the player
     * @param triangle   the triangle
     */
    public void setTriangle(UUID playerUuid, Triangle triangle) {
        triangles.put(playerUuid, triangle);
    }

    /**
     * Removes the triangle drawing (if exists) and delete its reference to the player.
     *
     * @param playerUuid the uuid of the player who drew the triangle
     */
    public void removeTriangle(UUID playerUuid) {
        if (triangles.containsKey(playerUuid)) {
            triangles.get(playerUuid).stopDrawing();
            triangles.remove(playerUuid);
        }
    }

}
