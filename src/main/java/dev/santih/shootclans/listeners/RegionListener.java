package dev.santih.shootclans.listeners;

import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.ClaimManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class RegionListener implements Listener {

	private final DependencyFactory dependencyFactory;

	private final ClaimManager claimManager;

	public RegionListener(DependencyFactory dependencyFactory) {
		this.dependencyFactory = dependencyFactory;

		this.claimManager = dependencyFactory.getClaimManager();
	}

	public void handleBlockBreak(final BlockBreakEvent event) {
		final Location location = event.getBlock().getLocation();
		final Player player = event.getPlayer();
		if (claimManager.isInsideClaim(location)) {
			MessageUtils.sendCoreMessage(player, "&cClaim detectado.");
		}
	}

	public void handleBlocKplace(final BlockPlaceEvent event) {

	}
}
