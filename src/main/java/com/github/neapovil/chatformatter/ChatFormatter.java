package com.github.neapovil.chatformatter;

import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.neapovil.chatformatter.chat.ChatRenderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class ChatFormatter extends JavaPlugin implements Listener
{
    private static ChatFormatter instance;
    public ConfigResource configResource;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable()
    {
        instance = this;

        try
        {
            this.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(this, this);

        new CommandAPICommand("chatformatter")
                .withPermission("chatformatter.command.reload")
                .withArguments(new LiteralArgument("reload"))
                .executes((sender, args) -> {
                    try
                    {
                        this.load();
                        sender.sendMessage("Config reloaded");
                    }
                    catch (IOException e)
                    {
                        sender.sendRichMessage("<red>Unable to reload config");
                        this.getLogger().severe(e.getMessage());
                    }
                })
                .register();
    }

    public static ChatFormatter instance()
    {
        return instance;
    }

    public void load() throws IOException
    {
        this.saveResource("config.json", false);
        final String string = Files.readString(this.getDataFolder().toPath().resolve("config.json"));
        this.configResource = this.gson.fromJson(string, ConfigResource.class);
    }

    public void save() throws IOException
    {
        final String string = this.gson.toJson(this.configResource);
        Files.write(this.getDataFolder().toPath().resolve("config.json"), string.getBytes());
    }

    @EventHandler
    private void onAsyncChat(AsyncChatEvent event)
    {
        event.renderer(new ChatRenderer());
    }

    public final class ConfigResource
    {
        public String format;
        public String formatOp;
    }
}
