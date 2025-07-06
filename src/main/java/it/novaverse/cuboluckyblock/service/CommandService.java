package it.novaverse.cuboluckyblock.service;

import it.novaverse.cuboluckyblock.CuboLuckyBlock;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.help.result.CommandEntry;
import org.incendo.cloud.minecraft.extras.ImmutableMinecraftHelp;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.minecraft.extras.caption.ComponentCaptionFormatter;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.translations.LocaleExtractor;
import org.incendo.cloud.translations.TranslationBundle;
import org.incendo.cloud.translations.bukkit.BukkitTranslationBundle;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class CommandService {

    private final PaperCommandManager<Source> commandManager;
    private final ImmutableMinecraftHelp<Source> help;

    @Inject
    public CommandService(CuboLuckyBlock plugin, TranslationService translationService) {
        commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
                .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
                .buildOnEnable(plugin);

        // Translations
        var localeExtractor = new LocaleExtractor<Source>() {
            @Override
            public @NonNull Locale extract(@NonNull Source sender) {
                return translationService.getLocale(sender.source());
            }
        };
        commandManager.captionRegistry().registerProvider(BukkitTranslationBundle.bukkit(localeExtractor));
        commandManager.captionRegistry().registerProvider(TranslationBundle.resourceBundle("cloud.lang.messages", localeExtractor));

        // Help handler
        help = MinecraftHelp.<Source>builder()
                .commandManager(commandManager)
                .audienceProvider(Source::source)
                .commandPrefix("/clb help")
                .messageProvider(MinecraftHelp.captionMessageProvider(
                        commandManager.captionRegistry(),
                        ComponentCaptionFormatter.miniMessage()
                ))
                .build();
        commandManager.captionRegistry().registerProvider(MinecraftHelp.defaultCaptionsProvider());

        // Command exception handlers
        MinecraftExceptionHandler.create(Source::source)
                .defaultHandlers()
                .registerTo(commandManager);
    }

    public <S extends Source> @NotNull Command<S> register(@NotNull String name, @NotNull Function<Command.Builder<Source>, Command.Builder<S>> builder) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(builder);
        var builderInstance = commandManager.commandBuilder(name);
        var command = builder.apply(builderInstance).build();
        commandManager.command(command);
        return command;
    }

    public void queryCommandHelp(@NotNull String query, @NotNull Source source) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(source);
        help.queryCommands(query, source);
    }

    public List<Suggestion> getHelpSuggestions(@NotNull Source sender) {
        Objects.requireNonNull(sender);
        return commandManager.createHelpHandler()
                .queryRootIndex(sender)
                .entries()
                .stream()
                .map(CommandEntry::syntax)
                .map(Suggestion::suggestion)
                .toList();
    }
}
