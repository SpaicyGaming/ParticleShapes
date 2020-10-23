package io.github.spaicygaming.particleshapes;

import io.github.spaicygaming.particleshapes.commands.TriangleCommand;
import io.github.spaicygaming.particleshapes.listeners.PlayerInteractListener;
import io.github.spaicygaming.particleshapes.listeners.PlayerQuitListener;
import io.github.spaicygaming.particleshapes.util.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticleShapesPlugin extends JavaPlugin {

    private static ParticleShapesPlugin instance;

    @Override
    public void onDisable() {
        getLogger().info("Plugin enabled :)");
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Load messages
        ConfigurationSection messagesConfigSection = getConfig().getConfigurationSection("messages");
        Message.load(messagesConfigSection);

        // The gadget item stack set in the configuration file
        ItemStack gadgetItemStack = parseItemStack(getConfig().getConfigurationSection("gadget-item"));

        TrianglesManager trianglesManager = new TrianglesManager();

        // Register the command
        getCommand("triangle").setExecutor(new TriangleCommand(gadgetItemStack, trianglesManager));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(gadgetItemStack, trianglesManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(trianglesManager), this);

        getLogger().info("Plugin enabled :)");
    }

    public static ParticleShapesPlugin getInstance() {
        return instance;
    }

    // TODO move in an utility class (?)
    private ItemStack parseItemStack(ConfigurationSection configurationSection) {
        ItemStack itemStack = new ItemStack(Material.valueOf(configurationSection.getString("material")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        itemMeta.setDisplayName(Color.format(configurationSection.getString("display-name")));
        itemMeta.setLore(Color.format(configurationSection.getStringList("lore")));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
