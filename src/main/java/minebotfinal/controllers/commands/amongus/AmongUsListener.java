package minebotfinal.controllers.commands.amongus;

import minebotfinal.exceptions.ServerExclusiveCommandException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AmongUsListener extends ListenerAdapter {

    String prefix;

    public AmongUsListener(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //TODO check if the user is carepicha or og carepicha

        Message msg = event.getMessage();
        if (event.getMessage().getContentRaw().startsWith(prefix + "amongus")) {
            if (!AmongUsMuter.isRunning()) {
                try {
                    new AmongUsMuter(msg).run();
                } catch (PermissionException | ServerExclusiveCommandException ignored) {
                }
            } else {
                msg.getChannel().sendMessage("alguien ya está utilizando este comando").queue();
            }
        }
    }
}