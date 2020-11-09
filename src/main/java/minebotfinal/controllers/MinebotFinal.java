package minebotfinal.controllers;


import minebotfinal.controllers.commands.amongus.AmongUsListener;
import minebotfinal.controllers.commands.ruleta.RuletaListener;
import minebotfinal.controllers.console.ConsoleInputListener;
import minebotfinal.controllers.commands.simple.SimpleCommandsListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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

    public MinebotFinal() throws LoginException, FileNotFoundException {
        try {
            config = new JSONObject(readJson("files/config.json"));
            token = config.get("token").toString();
            prefix = config.get("prefix").toString();

            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new SimpleCommandsListener(prefix))
                    .addEventListeners(new RuletaListener(prefix))
                    .addEventListeners(new AmongUsListener(prefix))
                    .setActivity(Activity.listening(prefix + "help"))
                    .build();
            jda.awaitReady();

            minecrafters = jda.getGuildById(minecraftersGuildId);
            assert minecrafters != null;
            carepicha = minecrafters.getRoleById(carepichaRoleId);
            ogCarepicha = minecrafters.getRoleById(ogCarepichaRoleId);
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread(new ConsoleInputListener(jda)).start();
    }

    public static void main(String[] args) throws InterruptedException {
        boolean cool = false;
        while (!cool) {
            try {
                new MinebotFinal();
                cool = true;
            } catch (LoginException ex) {
                ex.printStackTrace();
                Thread.currentThread().wait(10000);
            } catch (FileNotFoundException ex) {
                System.err.println("No se ha encontrado el archivo files/config.json");
                cool = true;
            }
        }

    }
}