package it.novaverse.cuboluckyblock.command;

import it.novaverse.cuboluckyblock.service.CommandService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractCommandContainer {

    protected final CommandService commandService;

    protected AbstractCommandContainer(@NotNull CommandService commandService) {
        Objects.requireNonNull(commandService);
        this.commandService = Objects.requireNonNull(commandService);
    }
}
