package it.novaverse.cuboluckyblock.command;

import it.novaverse.cuboluckyblock.service.CommandService;
import it.novaverse.cuboluckyblock.service.PartyService;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;

import java.util.Objects;

import static it.novaverse.cuboluckyblock.util.ComponentUtils.mm;

public class PartyCommandContainer extends AbstractCommandContainer {

    private final PartyService partyService;

    @Inject
    PartyCommandContainer(CommandService commandService, PartyService partyService) {
        super(commandService);

        this.partyService = partyService;

        // clb party invite
        var inviteCommand = commandService.register("clb", builder -> {
            return builder.literal("party")
                    .literal("invite")
                    .senderType(PlayerSource.class)
                    .permission("clb.command.party.invite")
                    .commandDescription(Description.description("Invita un giocatore nel tuo party"))
                    .required(
                            "player",
                            PlayerParser.playerParser(),
                            Description.description("Chi vorresti invitare")
                    )
                    .handler(this::inviteHandler);
        });
        // party invite
        commandService.register("party", builder -> builder.literal("invite").proxies(inviteCommand));
        // invite
        commandService.register("invite", builder -> builder.proxies(inviteCommand));

        // clb party accept
        var acceptCommand = commandService.register("clb", builder -> {
            return builder.literal("party")
                    .literal("accept")
                    .senderType(PlayerSource.class)
                    .permission("clb.command.party.accept")
                    .commandDescription(Description.description("Accetta l'invito di giocatore nel suo party"))
                    .required(
                            "player",
                            PlayerParser.playerParser(),
                            Description.description("Il giocatore che ti ha invitato nel suo party")
                    )
                    .handler(this::acceptHandler);
        });
        // party accept
        commandService.register("party", builder -> builder.literal("accept").proxies(acceptCommand));

        // clb party create
        var createCommand = commandService.register("clb", builder -> {
            return builder.literal("party")
                    .literal("create")
                    .senderType(PlayerSource.class)
                    .permission("clb.command.party.create")
                    .commandDescription(Description.description("Comando per creare un party"))
                    .handler(this::createHandler);
        });
        // party create
        commandService.register("party", builder -> builder.literal("create").proxies(createCommand));

        // clb party disband
        var disbandCommand = commandService.register("clb", builder -> {
            return builder.literal("party")
                    .literal("disband")
                    .senderType(PlayerSource.class)
                    .permission("clb.command.party.disband")
                    .commandDescription(Description.description("Comando per eliminare il proprio party"))
                    .handler(this::disbandHandler);
        });
        // party disband
        commandService.register("party", builder -> builder.literal("disband").proxies(disbandCommand));
    }

    private void inviteHandler(@NotNull CommandContext<PlayerSource> context) {
        Objects.requireNonNull(context);
        var sender = context.sender().source();
        Player target = context.get("player");
        if (sender == target) {
            sender.sendMessage(mm("<red>Non puoi invitare te stesso al party!</red>"));
            return;
        }

        // Fare controllo se il player è già nel party + spostare tutto nel service
        sender.sendMessage(mm(
                "<yellow>Hai invitato <b><target>!</b> </yellow>",
                Placeholder.unparsed("target", target.getName()))
        );
        // Da fare messaggio cliccabile per entrare nel team del giocatore
        target.sendMessage(mm(
                "<yellow><b><sender></b> ti ha invitato nel suo party! </yellow>",
                Placeholder.unparsed("sender", sender.getName()))
        );
    }

    private void acceptHandler(@NonNull CommandContext<PlayerSource> context) {
        Objects.requireNonNull(context);
        var sender = context.sender().source();
        Player target = context.get("player");
        // Da fare controllo per nome scritto in maniera errata e se il giocatore ha effettivamente invitato chi manda il comando
        sender.sendMessage(mm("<yellow>Sei entrato nel party di <b><target></b> </yellow>"));
    }

    private void createHandler(@NotNull CommandContext<PlayerSource> context) {
        Objects.requireNonNull(context);
        if (partyService.hasParty(context.sender().source().getName())) {
            context.sender().source().sendMessage(mm("<red>Hai già un party!!!</red>"));
        }
        String leaderName = context.sender().source().getName();
        partyService.create(leaderName);
    }

    private void disbandHandler(@NotNull CommandContext<PlayerSource> context) {
        Objects.requireNonNull(context);
        Player sender = context.sender().source();
        if (partyService.isLeader(context)) {
            partyService.disband(context);
        } else {
            sender.sendMessage(mm("<red>Non hai il permesso di eseguire questo comando<red>"));
        }
    }
}