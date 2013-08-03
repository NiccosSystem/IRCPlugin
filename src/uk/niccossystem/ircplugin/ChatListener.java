package uk.niccossystem.ircplugin;

import java.io.IOException;
import java.util.Arrays;
import net.niccossystem.skypebot.SkypeBot;
import net.niccossystem.skypebot.hook.HookHandler;
import net.niccossystem.skypebot.hook.chat.ChatHook;
import net.niccossystem.skypebot.plugin.PluginListener;
import net.niccossystem.skypebot.command.CommandContainer;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import com.skype.Chat;
import com.skype.SkypeException;

public class ChatListener extends PircBot implements PluginListener {

    private final IRCPlugin plugin;
    private Chat uhcChat;

    public ChatListener(IRCPlugin instance) throws NickAlreadyInUseException, IOException, IrcException {
        setName("UHCSkypeChat");
        setVerbose(false);
        this.connect("irc.esper.net");
        this.joinChannel("#ultrahardcore");
        plugin = instance;
    }

    @HookHandler
    public void onChat(ChatHook hook) {
        if (uhcChat == null) {
            uhcChat = hook.getChat();
        }

        if (hook.getChat() != uhcChat) {
            return;
        }
        if (hook.getMessage().startsWith(">")) {
            return;
        }

        for (String m : hook.getMessage().split("\n")) {
            SkypeBot.log(m);
            sendMessage("#ultrahardcore", ">" + hook.getSenderDisplayName() + ": " + m);
        }
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (uhcChat == null) {
            return;
        }
        if (message.startsWith("# ")) {
            return;
        }
        try {
            uhcChat.send(">" + sender + ": " + message);
        }
        catch (SkypeException e) {
            SkypeBot.log("Could not send message! (Not hooked into Skype?");
        }
        if (message.startsWith(SkypeBot.getSettingValue("commandPrefix"))) {
            String[] splitMessage = message.split(" ");
            String[] parameters = Arrays.copyOfRange(splitMessage, 1, splitMessage.length);
            String command = splitMessage[0].replaceFirst(SkypeBot.getSettingValue("commandPrefix"), "");
            CommandContainer cc = new CommandContainer(parameters, sender, command, uhcChat);
            SkypeBot.cmds().executeCommand(cc);
        }
    }

}
