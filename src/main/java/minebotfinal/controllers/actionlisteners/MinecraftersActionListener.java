package minebotfinal.controllers.actionlisteners;

import minebotfinal.controllers.MinebotFinal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MinecraftersActionListener extends ListenerAdapter {

    JDA jda;

    public MinecraftersActionListener() {
        jda = MinebotFinal.getJda();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent evt) {
        if (evt.getGuild().getId().equals(MinebotFinal.MINECRAFTERS_GUILD_ID)) {

            Member member = evt.getMember();
            Role pinta = jda.getRoleById(630609747569147906L);

            if (pinta != null) {
                evt.getGuild().addRoleToMember(evt.getMember(), pinta).queue();
            } else {
                System.err.println("ID of \"pinta\" role has changed. Not assigned to new member: " + member.getEffectiveName());
            }
        }
    }
}
