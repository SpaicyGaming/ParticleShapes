package io.github.spaicygaming.particleshapes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Triangle {

    /**
     * The distance between particles in lines
     */
    private static final float PARTICLES_DISTANCE = 0.4f;

    /**
     * A vertex of the triangle
     */
    private final Location vertexA, vertexB, vertexC;

    /**
     * The world the triangle is in
     */
    private final World world;

    /**
     * The triangle drawing task. Can be null
     */
    private BukkitTask drawingTask;

    /**
     * true -> the particle effect fills in the area of triangle when drawn
     */
    private boolean fillState = false;

    /**
     * Class constructor.
     * All three vertices must be in the same world. This class assumes the given vertices respect this prerequisite.
     *
     * @param vertexA a triangle vertex
     * @param vertexB a triangle vertex
     * @param vertexC another triangle vertex (can't be null)
     */
    public Triangle(@Nullable Location vertexA, @Nullable Location vertexB, @NotNull Location vertexC) {
        this.vertexA = vertexA != null ? vertexA.clone() : null;
        this.vertexB = vertexB != null ? vertexB.clone() : null;
        this.vertexC = vertexC.clone();

        this.world = this.vertexC.getWorld();
    }

    // todo insert a table  with vertex conversion examples in the javadocs below

    /**
     * Creates a new triangle instance. The current vertexB becomes vertexA, vertexC becomes vertexB,
     * and the given vertexLocation becomes vertexC.
     * <p>
     * The given location must be in the world of the triangle.
     * Throws an unchecked IllegalArgumentException if this prerequisite isn't met.
     *
     * @param vertexLocation the {@link Location} of the new vertex
     * @return a new triangle instance with the updated vertex
     */
    public Triangle changeVertex(Location vertexLocation) {
        if (!this.getWorld().equals(vertexLocation.getWorld())) {
            throw new IllegalArgumentException("The given vertex location is in a different world than the others");
        }

        // Return a new triangle instance with the updated vertices
        return new Triangle(vertexB, vertexC, vertexLocation);
    }

    /**
     * @param point Location of the point to check
     * @return true if the given point is a vertex of the triangle
     */
    public boolean isAVertex(Location point) {
        return point.equals(vertexA) || point.equals(vertexB) || point.equals(vertexC);
    }

    /**
     * @return true if the Triangle has all 3 vertices
     */
    public boolean hasAllVertices() {
        return vertexA != null && vertexB != null && vertexC != null;
    }

    /**
     * @return the {@link World} the triangle is in
     */
    public World getWorld() {
        return world;
    }

    /**
     * Toggle whether the particle effect fills in the area of triangle when drawn
     */
    public void toggleFillState() {
        this.fillState = !this.fillState;
    }

    /**
     * Draws the triangle
     *
     * @return true if the triangle was drawn with success
     */
    public boolean draw() {
        if (!hasAllVertices())
            return false;

        drawingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(ParticleShapesPlugin.getInstance(), () -> {
            Vector vectorA = vertexA.toVector();
            Vector vectorB = vertexB.toVector();
            Vector vectorC = vertexC.toVector();

            drawSegment(vectorA.clone(), vectorB.clone());
            drawSegment(vectorB.clone(), vectorC.clone());
            drawSegment(vectorC.clone(), vectorA.clone());

            // Draw the area if enabled
            if (fillState) {
                drawArea(vectorA.clone(), vectorB.clone(), vectorC.clone());
            }

        }, 0L, 10L);

        return true;
    }

    /**
     * Draws the area of the triangle
     *
     * @param a vertex
     * @param b vertex
     * @param c vertex
     */
    private void drawArea(Vector a, Vector b, Vector c) {
        Vector segmentAB = b.clone().subtract(a);
        Vector segmentBC = c.clone().subtract(b);
        Vector segmentAC = c.clone().subtract(a);

        double angleA = segmentAB.angle(segmentAC);
        double angleB = segmentAB.angle(segmentBC);

        // TODO sin may be 0 (is angle == 0 || == pi)
        double dAC = PARTICLES_DISTANCE / Math.abs(Math.sin(angleA));
        double dBC = PARTICLES_DISTANCE / Math.abs(Math.sin(angleB));

        double segmentACLength = segmentAC.length();

        segmentAC.normalize().multiply(dAC);
        segmentBC.normalize().multiply(dBC);

        // draw segments parallels to AB
        int iterationsNumber = (int) (segmentACLength / dAC);
        for (int i = 0; i < iterationsNumber; i++) {
            Vector pointOnAC = a.add(segmentAC);
            Vector pointOnBC = b.add(segmentBC);

            drawSegment(pointOnAC.clone(), pointOnBC.clone());
        }
    }

    /**
     * Draws a line of particles between two vectors.
     *
     * @param start  one of the two vectors
     * @param finish the other vector
     */
    private void drawSegment(Vector start, Vector finish) {
        Vector segmentVector = finish.subtract(start);
        double segmentLength = segmentVector.length();

        segmentVector.normalize().multiply(PARTICLES_DISTANCE);

        // Iterate from start to finish
        int iterationsNumber = (int) (segmentLength / PARTICLES_DISTANCE);
        for (int i = 0; i < iterationsNumber; i++) {
            Vector point = start.add(segmentVector);

            // TODO
            // TODO reuse the points
            // TODO

            // snow_shovel (?)
            world.spawnParticle(Particle.FLAME, point.getX() + 0.5, point.getY(), point.getZ() + 0.5, 1, 0d, 0d, 0d, 0d);
        }
    }

    /**
     * Cancels, if present, the triangle drawing
     */
    public void stopDrawing() {
        if (drawingTask != null && !drawingTask.isCancelled())
            drawingTask.cancel();
    }

}
