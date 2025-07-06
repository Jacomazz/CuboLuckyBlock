package it.novaverse.cuboluckyblock;

import ch.jalu.injector.InjectorBuilder;
import it.novaverse.cuboluckyblock.command.BaseCommandContainer;
import it.novaverse.cuboluckyblock.command.PartyCommandContainer;
import it.novaverse.cuboluckyblock.service.CommandService;
import it.novaverse.cuboluckyblock.service.ConfigService;
import it.novaverse.cuboluckyblock.service.PartyService;
import it.novaverse.cuboluckyblock.service.TranslationService;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import ch.jalu.injector.Injector;

@SuppressWarnings("UnstableApiUsage")
public final class CuboLuckyBlock extends JavaPlugin {

    private Injector injector;

    private ConfigService configService;
    private TranslationService translationService;
    private CommandService commandService;
    private PartyService partyService;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Create plugin folder
        getDataFolder().mkdir();

        // Initialize dependency injector
        injector = new InjectorBuilder()
                .addDefaultHandlers("it.novaverse.cuboluckyblock")
                .create();
        injector.register(CuboLuckyBlock.class, this);
        injector.register(Server.class, getServer());
        injector.register(PluginManager.class, getServer().getPluginManager());
        injector.register(BukkitScheduler.class, getServer().getScheduler());

        // Initialize services
        configService = injector.getSingleton(ConfigService.class);
        translationService = injector.getSingleton(TranslationService.class);
        commandService = injector.getSingleton(CommandService.class);
        partyService = injector.getSingleton(PartyService.class);

        // Register command containers
        injector.getSingleton(BaseCommandContainer.class);
        injector.getSingleton(PartyCommandContainer.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
