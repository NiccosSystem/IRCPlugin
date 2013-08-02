package uk.niccossystem.ircplugin;

import java.io.IOException;
import net.niccossystem.skypebot.SkypeBot;
import net.niccossystem.skypebot.plugin.Plugin;
import org.jibble.pircbot.IrcException;

public class IRCPlugin extends Plugin {

    @Override
    public String author() {
        return "NiccosSystem";
    }

    @Override
    public boolean enable() {
        try {
            SkypeBot.hooks().registerListener(new ChatListener(this), this);
        }
        catch (IOException | IrcException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String version() {
        return "0.1";
    }

}
