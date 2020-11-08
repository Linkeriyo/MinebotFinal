package minebotfinal.controllers;


import minebotfinal.controllers.commands.amongus.AmongUsListener;
import minebotfinal.controllers.listeners.ConsoleInputListener;
import minebotfinal.controllers.listeners.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static minebotfinal.jsonreaders.JSON.readJson;

public class MinebotFinal {
    public final static String carepichaRoleId = "580426709581692928",
            ogCarepichaRoleId = "708345730460549122",
            minecraftersGuildId = "580421667336224769";

    JDA jda;
    String token, prefix;
    JSONObject config;
    Guild minecrafters;
    Role carepicha, ogCarepicha;

    public MinebotFinal() throws LoginException {
        try {
            config = new JSONObject(readJson("files/config.json"));
            token = config.get("token").toString();
            prefix = config.get("prefix").toString();

            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new MessageListener(prefix))
                    .addEventListeners(new AmongUsListener(prefix))
                    .build();
            jda.awaitReady();

            minecrafters = jda.getGuildById(minecraftersGuildId);
            assert minecrafters != null;
            carepicha = minecrafters.getRoleById(carepichaRoleId);
            ogCarepicha = minecrafters.getRoleById(ogCarepichaRoleId);
        } catch (FileNotFoundException ex) {
            System.err.println("No se ha encontrado el archivo files/config.json");
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread(new ConsoleInputListener(jda)).start();
    }

    public static void main(String[] args) {
        try {
            new MinebotFinal();
        } catch (LoginException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}