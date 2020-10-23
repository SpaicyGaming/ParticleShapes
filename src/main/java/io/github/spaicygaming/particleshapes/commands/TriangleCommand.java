package io.github.spaicygaming.particleshapes.commands;

import io.github.spaicygaming.particleshapes.Message;
import io.github.spaicygaming.particleshapes.Triangle;
import io.github.spaicygaming.particleshapes.TrianglesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TriangleCommand implements CommandExecutor {

    private final ItemStack gadgetItemStack;
    private final TrianglesManager trianglesManager;

    public TriangleCommand(ItemStack gadgetItemStack, TrianglesManager trianglesManager) {
        this.gadgetItemStack = gadgetItemStack;
        this.trianglesManager = trianglesManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length == 1 && args[0].equalsIgnoreCase("togglefill")) {
                // Toggle fill state
                Optional<Triangle> triangleOptional = trianglesManager.getTriangle(player.getUniqueId());

                if (triangleOptional.isPresent()) {
                    triangleOptional.get().toggleFillState();
                    Message.TRIANGLE_FILL_TOGGLED.sendTo(player);
                } else {
                    Message.TRIANGLE_DRAW_FIRST.sendTo(player);
                }
            } else {
                // Give the gadget item to the player
                player.getInventory().addItem(gadgetItemStack);

                Message.CMD_GET_GADGET_SUCCESS.sendTo(player);
            }
            return true;
        } else {
            // Notifies that the command can be executed only by players
            Message.CMD_PLAYERS_ONLY.sendTo(commandSender);
            return false;
        }
    }
}
