package minebotfinal;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window {
    private JButton button1;
    private JPanel panel1;
    public MinebotFinal minebot;

    public Window() {

        try {
            minebot = new MinebotFinal();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minebot.toggleMuteForOwnersChannel();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Window");
        frame.setContentPane(new Window().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(78, 60);
        frame.pack();
        frame.setVisible(true);
    }
}
