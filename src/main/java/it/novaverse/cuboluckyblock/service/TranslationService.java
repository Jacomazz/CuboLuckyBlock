package it.novaverse.cuboluckyblock.service;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Objects;

public class TranslationService {

    private final ConfigService configService;

    private Locale defaultLocale;

    @Inject
    TranslationService(ConfigService configService) {
        this.configService = configService;
        reload();
    }

    public Locale getLocale(@NotNull CommandSender sender) {
        Objects.requireNonNull(sender);
        if (sender instanceof Player player) {
            return player.locale();
        }
        return defaultLocale;
    }

    public void reload() {
        this.defaultLocale = Locale.ENGLISH; // TODO: config
    }
}
