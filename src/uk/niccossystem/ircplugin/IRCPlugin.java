package uk.niccossystem.ircplugin;

import java.io.IOException;

import org.jibble.pircbot.IrcException;

import uk.niccossystem.skypebot.SkypeBot;
import uk.niccossystem.skypebot.plugin.Plugin;

public class IRCPlugin extends Plugin {

	@Override
	public String author() {
		return "NiccosSystem";
	}

	@Override
	public boolean enable() {
		try {
			SkypeBot.hooks().registerListener(new ChatListener(this), this);
		} catch (IOException | IrcException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String version() {
		return "0.1";
	}

}
