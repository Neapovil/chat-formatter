package com.github.neapovil.chatformatter.hook;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import com.github.neapovil.permissionsyaml.PermissionsYaml;
import com.github.neapovil.permissionsyaml.resource.PlayersResource;

public final class PermissionsYamlHook
{
    @Nullable
    public static String getGroup(Player player)
    {
        final PermissionsYaml permissionsyaml = PermissionsYaml.instance();
        final Optional<PlayersResource.Player> optionalpermissionsplayer = permissionsyaml.playersResource.find(player.getUniqueId());

        if (optionalpermissionsplayer.isEmpty())
        {
            return null;
        }

        return optionalpermissionsplayer.get().prefix();
    }
}
