package dev.santih.shootclans;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.commands.*;
import dev.santih.shootclans.listeners.PlayerListener;
import dev.santih.shootclans.listeners.WandListener;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactoryImpl;
import dev.santih.shootclans.managers.interfaces.ClaimManager;
import dev.santih.shootclans.managers.interfaces.ClanManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class ShootClans extends JavaPlugin {

	private DependencyFactory dependencyFactory;

	private PlayerManager playerManager;
	private ClanManager clanManager;

	private ClaimManager claimManager;
	public static String GRAY_COLOR = "&#999999";

	@Override
	public void onEnable() {
		this.dependencyFactory = new DependencyFactoryImpl(this);

		dependencyFactory.getDependencyManager().loadLibrary("invapi.jar");
		dependencyFactory.getDependencyManager().loadLibrary("acf-paper.jar");
		PaperCommandManager manager = new PaperCommandManager(this);

		this.playerManager = dependencyFactory.getPlayerManager();
		this.clanManager = dependencyFactory.getClanManager();
		this.claimManager = dependencyFactory.getClaimManager();

		new InventoryAPI(this).init();
		registerEvents();
		//Reload support
		dependencyFactory.getPlayerManager().cacheAllOnline();

		manager.getCommandContexts().registerContext(ClanPlayer.class, c -> {
			String arg = c.popFirstArg();
			Player player = Bukkit.getPlayerExact(arg);
			if (player == null) {
				return null;
			}
			return playerManager.getClanPlayer(player);
		});
		manager.getCommandCompletions().registerAsyncCompletion("clanplayers", new CommandCompletions.AsyncCommandCompletionHandler<>() {
			@Override
			public Collection<String> getCompletions(BukkitCommandCompletionContext context) {
				Player player = context.getPlayer();
				if (player == null) {
					return Collections.emptyList();
				}

				ClanPlayer sender = playerManager.getClanPlayer(player);
				if (!sender.getClan().isPresent()) {
					return Collections.emptyList();
				}

				return sender.getClan().get().getPlayers().stream()
						.map(ClanPlayer::getName)
						.collect(Collectors.toList());
			}
		});
		manager.getCommandCompletions().registerAsyncCompletion("clanpermissions", new CommandCompletions.AsyncCommandCompletionHandler<>() {
			@Override
			public Collection<String> getCompletions(BukkitCommandCompletionContext context) {
				return Arrays.stream(Permission.values())
						.map(Enum::name)
						.collect(Collectors.toList());
			}
		});

		// Register the @clans completion provider
		manager.getCommandCompletions().registerAsyncCompletion("clans", c -> clanManager.getClans().stream()
				.map(Clan::getName)
				.collect(Collectors.toList()));

		// Plugin startup logic


		//manager.registerCommand(new ClanCommand(clanManager, playerManager, requestManager, economyManager));
		manager.registerCommand(new ClanBankCommands(dependencyFactory));
		manager.registerCommand(new ClanHomeClaimCommands(dependencyFactory));
		manager.registerCommand(new ClanJoinLeaveCommands(dependencyFactory));
		manager.registerCommand(new ClanManagementCommands(dependencyFactory));
		manager.registerCommand(new ClanPermissionCommands(dependencyFactory));
		manager.registerCommand(new ClanAdminCommands(dependencyFactory));
		manager.registerCommand(new ClanAdminMemberCommands(dependencyFactory));
		manager.registerCommand(new ClanAllyCommands(dependencyFactory));
	}

	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			claimManager.stopShowingNearbyClaims(player, 200);
		});
		// Plugin shutdown logic
	}

	private void registerEvents() {
		Arrays.asList(
						new PlayerListener(dependencyFactory),
						new WandListener(dependencyFactory))
				.forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));

	}
}