package com.github.neapovil.chatformatter;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChatFormatter extends JavaPlugin
{
    private static ChatFormatter instance;

    @Override
    public void onEnable()
    {
        instance = this;
    }

    public static ChatFormatter instance()
    {
        return instance;
    }
}
