package it.novaverse.cuboluckyblock.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ComponentUtils {

    private ComponentUtils() {
    }

    public static Component mm(final @NotNull String message, final TagResolver... tagResolvers) {
        Objects.requireNonNull(message);
        return MiniMessage.miniMessage().deserialize(message, tagResolvers);
    }
}
