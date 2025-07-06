package it.novaverse.cuboluckyblock.service;


import it.novaverse.cuboluckyblock.models.Party;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;

public final class PartyService {

    public void invite(CommandContext<PlayerSource> context) {

    }

    public void create(String leader) {
        Party party = new Party(leader);

    }

    public void disband(CommandContext<PlayerSource> context) {
        Player sender = context.sender().source();
//        Da fare che viene eliminato il party dal db;
    }

    public boolean hasParty(String name) {
        if (true) { //Da mettere la query che vede se il player è già in party
            return true;
        } else {
            return false;
        }
    }

    public boolean isLeader(CommandContext<PlayerSource> context) {
        Player sender = context.sender().source();
        if (true) { //Da fare la ricerca nel DB dei leader per il target
            return true;
        } else {
            return false;
        }
    }
}
