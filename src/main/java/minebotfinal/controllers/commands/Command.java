package minebotfinal.controllers.commands;

import minebotfinal.exceptions.ServerExclusiveCommandException;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;

public abstract class Command {
    protected boolean serverExclusive;
    protected Permission[] requiredPermissions;
    protected MessageChannel messageChannel;
    protected Guild currentGuild;

    /**
     * Checks if the bot has the required permission for this command.
     *
     * @return permissions needed that the bot lacks.
     */
    protected ArrayList<Permission> checkPermissions() {
        Member self = currentGuild.getSelfMember();
        ArrayList<Permission> toReturn = new ArrayList<>();

        for (Permission perm : requiredPermissions) {
            if (!self.hasPermission(perm)) {
                toReturn.add(perm);
            }
        }

        return toReturn;
    }

    /**
     * Sends a message in the text channel that called the command with the permissions that lacks.
     */
    protected void sendPermissionsMessage() {
        MessageBuilder mb = new MessageBuilder().setContent("se requieren los permisos: ");
        for (int i = 0; i < requiredPermissions.length; i++) {
            if (i == 0) {
                mb.append(requiredPermissions[i].getName());
            } else {
                mb.append(", ").append(requiredPermissions[i].getName());
            }
        }
        messageChannel.sendMessage(mb.build()).queue();
    }

    /**
     * Sends a message in the text channel that called the command if the command is server-exclusive.
     */
    protected void checkServerExclusiveMessage() {
        if (serverExclusive && messageChannel.getType() != ChannelType.TEXT) {
            messageChannel.sendMessage("este comando solo se puede usar en un servidor").queue();
            throw new ServerExclusiveCommandException();
        }
    }
}