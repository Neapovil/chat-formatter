package com.github.neapovil.chatformatter.chat;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.github.neapovil.chatformatter.ChatFormatter;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public final class ChatRenderer implements io.papermc.paper.chat.ChatRenderer
{
    private final ChatFormatter plugin = ChatFormatter.instance();

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer)
    {
        final String format = source.isOp() ? plugin.configResource.formatOp : plugin.configResource.format;

        final Component component = plugin.miniMessage.deserialize(format,
                Placeholder.component("player_name", sourceDisplayName));

        return component.replaceText(b -> {
            if (plugin.getServer().getPluginManager().getPlugin("PermissionsYaml") == null)
            {
                b.matchLiteral("<permissionsyaml>").replacement("");
            }
            else
            {
                final String stringgroup = com.github.neapovil.chatformatter.hook.PermissionsYamlHook.getGroup(source);

                if (stringgroup == null)
                {
                    b.matchLiteral("<permissionsyaml>").replacement("");
                }
                else
                {
                    b.matchLiteral("<permissionsyaml>").replacement(plugin.miniMessage.deserialize(stringgroup).appendSpace());
                }
            }
        }).append(message);
    }
}
