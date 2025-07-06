package it.novaverse.cuboluckyblock.command;

import it.novaverse.cuboluckyblock.service.CommandService;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Objects;

public class BaseCommandContainer extends AbstractCommandContainer {

    @Inject
    BaseCommandContainer(CommandService commandService) {
        super(commandService);

        // clb help
        var helpCommand = commandService.register("clb", builder -> {
            return builder.literal("help")
                    .permission("clb.command.help")
                    .optional(
                            "query",
                            StringParser.greedyStringParser(),
                            DefaultValue.constant(""),
                            SuggestionProvider.blocking((ctx, in) -> commandService.getHelpSuggestions(ctx.sender()))
                    )
                    .handler(this::helpHandler);
        });
        // help
        commandService.register("help", builder -> builder.proxies(helpCommand));
    }

    private void helpHandler(@NotNull CommandContext<Source> context) {
        Objects.requireNonNull(context);
        commandService.queryCommandHelp(context.get("query"), context.sender());
    }
}
