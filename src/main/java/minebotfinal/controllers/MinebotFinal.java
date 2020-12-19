package minebotfinal.controllers;


import minebotfinal.controllers.commands.PollListener;
import minebotfinal.controllers.commands.amongus.AmongUsListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static minebotfinal.jsonreaders.JSON.readJson;

public class MinebotFinal {
    public final static String CAREPICHA_ROLE_ID = "580426709581692928",
            OG_CAREPICHA_ROLE_ID = "708345730460549122",
            MINECRAFTERS_GUILD_ID = "580421667336224769";

    static JDA jda;
    String token, prefix;
    JSONObject config;
    Guild minecrafters;
    Role carepicha, ogCarepicha;

    public MinebotFinal() throws LoginException, IOException {
        try {
            config = new JSONObject(readJson("files/config.json"));
            token = config.get("token").toString();
            prefix = config.get("prefix").toString();

            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new AmongUsListener(prefix))
                    .addEventListeners(new PollListener(prefix))
                    .setActivity(Activity.listening(prefix + "help"))
                    .build();
            jda.awaitReady();

            minecrafters = jda.getGuildById(MINECRAFTERS_GUILD_ID);
            assert minecrafters != null;
            carepicha = minecrafters.getRoleById(CAREPICHA_ROLE_ID);
            ogCarepicha = minecrafters.getRoleById(OG_CAREPICHA_ROLE_ID);
        } catch (InterruptedException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread(new ConsoleInputListener(jda)).start();
    }

    public static JDA getJda() {
        return jda;
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