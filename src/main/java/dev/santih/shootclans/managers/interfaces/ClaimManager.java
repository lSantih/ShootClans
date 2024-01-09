package dev.santih.shootclans.managers.interfaces;

import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.exception.MaxChunksExceededException;
import dev.santih.shootclans.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ClaimManager {
	void saveClaim(ClanPlayer clanPlayer) throws MaxChunksExceededException;

	boolean intersects(Cuboid newRegion);

	void showNearbyClaims(Player player, double distance);

	void setClaimBounds(Player player, Location firstPosition, Location secondPosition, Material material);

	void stopShowingNearbyClaims(Player player, double distance);

	boolean isInsideClaim(final Location location);

	ItemStack getWandItem();
}
