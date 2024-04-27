package com.github.neapovil.chatformatter;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.neapovil.chatformatter.chat.ChatRenderer;
import com.github.neapovil.core.Core;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.util.MCUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class ChatFormatter extends JavaPlugin implements Listener
{
    private static ChatFormatter instance;
    public ConfigResource configResource;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public final MiniMessage miniMessage = MiniMessage.miniMessage();
    public final Path configPath = this.getDataFolder().toPath().resolve("config.json");

    @Override
    public void onEnable()
    {
        instance = this;

        this.load();

        this.getServer().getPluginManager().registerEvents(this, this);

        new CommandAPICommand("chatformatter")
                .withPermission("chatformatter.command.reload")
                .withArguments(new LiteralArgument("reload"))
                .executes((sender, args) -> {
                    this.load().whenComplete((result, ex) -> {
                        if (ex == null)
                        {
                            sender.sendMessage("Config reloaded");
                        }
                        else
                        {
                            sender.sendRichMessage("<red>Unable to reload config: " + ex.getMessage());
                        }
                    });
                })
                .register();
    }

    public static ChatFormatter instance()
    {
        return instance;
    }

    private CompletableFuture<String> load()
    {
        final Core core = Core.instance();
        return core.loadResource(this, this.configPath).whenCompleteAsync((result, ex) -> {
            if (result != null)
            {
                this.configResource = this.gson.fromJson(result, ConfigResource.class);
            }
        }, MCUtil.MAIN_EXECUTOR);
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
