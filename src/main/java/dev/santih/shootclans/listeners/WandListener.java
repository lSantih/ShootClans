package dev.santih.shootclans.listeners;

import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.exception.MaxChunksExceededException;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.ClaimManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandListener implements Listener {

	private final PlayerManager playerManager;
	private final ClaimManager claimManager;
	private final Map<UUID, BukkitTask> particleTasks = new HashMap<>();

	public WandListener(DependencyFactory dependencyFactory) {
		this.playerManager = dependencyFactory.getPlayerManager();
		this.claimManager = dependencyFactory.getClaimManager();
	}

	@EventHandler
	public void handleMark(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		Action action = event.getAction();
		if (action == null) return;

		ItemStack item = event.getItem();
		if (item == null) return;

		if (item.isSimilar(claimManager.getWandItem())) {
			if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
				Block clickedBlock = event.getClickedBlock();
				Location location = clickedBlock.getLocation();

				ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
				if (!canClaim(player, clanPlayer)) {
					return;
				}

				if (action.equals(Action.LEFT_CLICK_BLOCK)) {
					handleLeftClick(player, clanPlayer, location);
				} else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
					handleRightClick(player, clanPlayer, location);
				}

				event.setCancelled(true);
			}
		}
	}

	private void handleLeftClick(Player player, ClanPlayer clanPlayer, Location location) {
		claimManager.setClaimBounds(player, location, null, Material.AIR);
		clanPlayer.setFirstPosition(location);
		clanPlayer.setSecondPosition(null); // Reset the second position
		MessageUtils.sendCoreMessage(player, "&aFirst position set.");
	}

	private void handleRightClick(Player player, ClanPlayer clanPlayer, Location location) {
		claimManager.setClaimBounds(player, location, clanPlayer.getFirstPosition(), Material.AIR);
		clanPlayer.setSecondPosition(location);
		MessageUtils.sendCoreMessage(player, "&aSecond position set.");

		// Start spawning particles only if both positions have been set
		claimManager.setClaimBounds(player, clanPlayer.getFirstPosition(), location, Material.LIGHT_BLUE_STAINED_GLASS);
	}

	@EventHandler
	public void handleInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();

		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null || !item.isSimilar(claimManager.getWandItem())) {
			return;
		}

		if (player.isSneaking() && action == Action.LEFT_CLICK_AIR) {
			ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
			if (!canClaim(player, clanPlayer)) {
				return;
			}

			if (clanPlayer.getFirstPosition() == null || clanPlayer.getSecondPosition() == null) {
				MessageUtils.sendCoreMessage(player, "&cYou must set both positions before claiming!");
				return;
			}

			try {
				claimManager.setClaimBounds(player, clanPlayer.getFirstPosition(), clanPlayer.getSecondPosition(), Material.AIR);
				claimManager.saveClaim(clanPlayer);
				player.getInventory().remove(claimManager.getWandItem());
			} catch (MaxChunksExceededException e) {
				MessageUtils.sendCoreMessage(player, "&cYou can only claim a maximum of 3 chunks!");
			}
		}
	}

	private boolean canClaim(Player player, ClanPlayer clanPlayer) {
		if (clanPlayer == null || !clanPlayer.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou aren't in a clan. You can not claim.");
			return false;
		}

		// Check if the player has permission to claim land
		//if (!clanPlayer.hasPermission(Permission.CLAIM_WAND)) {
		//MessageUtils.sendCoreMessage(player, "&cYou do not have permissions to claim.");
		//return false;
		//}

		return true;
	}

	private void stopParticleTask(Player player) {
		BukkitTask task = particleTasks.remove(player.getUniqueId());
		if (task != null) {
			task.cancel();
		}
	}
}
