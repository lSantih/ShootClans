package dev.santih.shootclans.listeners;

import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.ClaimManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	private final PlayerManager playerManager;
	private final ClaimManager claimManager;

	public PlayerListener(DependencyFactory dependencyFactory) {
		this.playerManager = dependencyFactory.getPlayerManager();
		this.claimManager = dependencyFactory.getClaimManager();
	}

	@EventHandler
	private void handleJoin(final PlayerJoinEvent event) {
		playerManager.loadPlayer(event.getPlayer());
	}

	@EventHandler
	private void handleQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();

		playerManager.unloadPlayer(player);
		claimManager.stopShowingNearbyClaims(player, 200);

	}
}
