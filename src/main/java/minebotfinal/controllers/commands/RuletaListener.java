package minebotfinal.controllers.commands;

import minebotfinal.controllers.MinebotFinal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RuletaListener extends ListenerAdapter {

    String prefix;

    public RuletaListener(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        JDA jda = event.getJDA();
        Guild minecrafters = jda.getGuildById(MinebotFinal.MINECRAFTERS_GUILD_ID);
        Role ogCarepicha = jda.getRoleById(MinebotFinal.OG_CAREPICHA_ROLE_ID);
        Role carepicha = jda.getRoleById(MinebotFinal.CAREPICHA_ROLE_ID);

        if (msg.getContentRaw().startsWith(prefix + "ruleta")) {
            List<Member> members = minecrafters.getMembersWithRoles(ogCarepicha);
            members.addAll(minecrafters.getMembersWithRoles(carepicha));
            int member = (int) (Math.random() * members.size());
            boolean gone = (int) (Math.random() * 2) == 0;
            msg.getChannel().sendMessage("¿es " + members.get(member).getUser().getName() + " el próximo en irse de minecrafters?").queue();
            if (gone) {
                msg.getChannel().sendMessage("sii").queue();
            } else {
                msg.getChannel().sendMessage("noo").queue();
            }
        }
    }
}
