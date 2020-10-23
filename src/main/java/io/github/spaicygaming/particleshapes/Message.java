package io.github.spaicygaming.particleshapes;

import io.github.spaicygaming.particleshapes.util.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public enum Message {

    CHAT_PREFIX,
    VERTEX_SET_SUCCESS,
    VERTEX_SET_ALREADY,
    TRIANGLE_DRAWN,
    TRIANGLE_DRAW_FIRST,
    TRIANGLE_FILL_TOGGLED,
    CMD_PLAYERS_ONLY,
    CMD_GET_GADGET_SUCCESS,
    VERTEX_DIFFERENT_WORLD,
    ;

    private final String key;
    private String text;

    Message() {
        this.key = super.name().toLowerCase().replace('_', '-');
    }

    /**
     * Loads messages from the given configuration section.
     * Their lowercase {@link #name()} (with '_' replaced with '-') are used as keys.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static void load(ConfigurationSection defaultMessages) {
        // Default messages
        assert defaultMessages != null;

        // Loads the message from the configuration file
        for (Message message : values()) {
            String messageText = defaultMessages.getString(message.key);
            message.setText(messageText);
        }
    }

    private void setText(String value) {
        try {
            this.text = Color.format(value);
        } catch (IllegalArgumentException exception) {
            // Notify message missing from the config
            ParticleShapesPlugin.getInstance().getLogger().severe("The message \"" + key + "\" is missing from the config.yml");
            this.text = "message missing from the config " + this.name();
        }
    }

    /**
     * @return the colored message text specified in the configuration file
     */
    public String getText() {
        return text;
    }

    /**
     * @return the colored message preceded by the chat prefix
     */
    public String getMessage() {
        return CHAT_PREFIX.getText() + text;
    }

    /**
     * Sends the colored message to the given CommandSender
     *
     * @param user         the user to send the message to
     * @param placeholders strings to replace. Strings with even indexes are replaced with the following
     *                     odd number  (i placeholder, i+1 value, starting from 0)
     */
    public void sendTo(CommandSender user, String... placeholders) {
        /*
        + -> placeholder
        * -> value
        + * + * + * + *
        0 1 2 3 4 5 6 7  etc.
         */

        String message = this.getMessage();
        if (placeholders != null && placeholders.length > 0) {
            for (int i = 0; i < placeholders.length; i += 2) {
                message = message.replace(placeholders[i], placeholders[i + 1]);
            }
        }

        user.sendMessage(message);
    }

}
