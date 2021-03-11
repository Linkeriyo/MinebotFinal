package minebotfinal.controllers;


import minebotfinal.controllers.commands.PollListener;
import minebotfinal.controllers.commands.SimpleCommandsListener;
import minebotfinal.controllers.commands.amongus.AmongUsListener;
import minebotfinal.controllers.commands.playingmusic.MusicListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static minebotfinal.jsonreaders.JSON.readJson;

public class MinebotFinal {
    public final static String CAREPICHA_ROLE_ID = "580426709581692928",
            OG_CAREPICHA_ROLE_ID = "708345730460549122",
            MINECRAFTERS_GUILD_ID = "580421667336224769";

    private static JDA jda;
    private static String prefix;
    Guild minecrafters;
    Role carepicha, ogCarepicha;

    public MinebotFinal() throws LoginException, IOException {
        try {
            JSONObject config = new JSONObject(readJson("files/config.json"));
            String token = config.get("token").toString();
            prefix = config.get("prefix").toString();

            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new AmongUsListener(prefix))
                    .addEventListeners(new PollListener(prefix))
                    .addEventListeners(new MusicListener())
                    .addEventListeners(new SimpleCommandsListener(prefix))
                    .setActivity(Activity.listening(prefix + "help"))
                    .build();
            jda.awaitReady();

            minecrafters = jda.getGuildById(MINECRAFTERS_GUILD_ID);
            assert minecrafters != null;
            carepicha = minecrafters.getRoleById(CAREPICHA_ROLE_ID);
            ogCarepicha = minecrafters.getRoleById(OG_CAREPICHA_ROLE_ID);


/*
            MessageEmbed embed = new EmbedBuilder().setDescription("1. respetacion\n"
                    + "2. no s� consiste en respetarse\n"
                    + "3. si te dicen que est�s fuerte probablemente es mentira\n"
                    + "4. respeten a bad bunny (porque si no os banea vaya)\n"
                    + "5. no ligar con menores\n"
                    + "6. mete hellcase\n"
                    + "7. need for speed most wanted\n"
                    + "8. no nos gustan los chetos\n"
                    + "9. lo primero que tienen que hacer los ricos es invitar a los colegas\n"
                    + "10. si se te ocurre algo se lo dices a <@154268434090164226>")
                    .setFooter("gracias", "https://mui.today/__export/1583420503184/sites/mui/img/2019/12/02/bad-bunny.jpg_1899857922.jpg")
                    .setImage("https://cutewallpaper.org/21/minecraft-wallpaper-hd/Download-minecraft-wallpaper-hd.jpg")
                    .setAuthor("normas y consejos", "https://discordapp.com", null)
                    .setThumbnail("https://cdn.discordapp.com/attachments/674346221569310741/792041822561370122/doro.png")
                    .addField("che- check this out", "[@minecraftersooc](https://twitter.com/minecraftersooc)", false)
                    .addField("invitaci�n permanente", "[https://discord.com/invite/PJuJMJ6](https://discord.com/invite/PJuJMJ6)", false)
                    .build();

            jda.getTextChannelById("713842133232254977").sendMessage(embed).queue();
*/
        } catch (InterruptedException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread(new ConsoleInputListener(jda)).start();
    }

    public static JDA getJda() {
        return jda;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void main(String[] args) throws InterruptedException {
        boolean exit = false;
        while (!exit) {
            try {
                new MinebotFinal();
                exit = true;
            } catch (LoginException ex) {
                ex.printStackTrace();
                Thread.currentThread().wait(10000);
            } catch (FileNotFoundException ex) {
                System.err.println("No se ha encontrado el archivo files/config.json");
                exit = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}