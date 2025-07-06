package it.novaverse.cuboluckyblock.models;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Party {

    @NotNull
    private String leader;
    @NotNull
    private Set<String> membri;

    public Party(@NotNull String leader) {
        this.leader = Objects.requireNonNull(leader);
        this.membri = new HashSet<>();
        this.membri.add(leader);
    }

    public @NotNull String getLeader() {
        return leader;
    }

    public void setLeader(@NotNull String leader) {
        this.leader = Objects.requireNonNull(leader);
    }

    public @NotNull Set<String> getMembri() {
        return membri;
    }

    public void setMembri(@NotNull Set<String> membri) {
        Objects.requireNonNull(membri);
        this.membri = membri;
    }
}
