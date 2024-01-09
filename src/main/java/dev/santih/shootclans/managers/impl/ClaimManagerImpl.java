package dev.santih.shootclans.managers.impl;

import dev.santih.shootclans.ShootClans;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.exception.MaxChunksExceededException;
import dev.santih.shootclans.managers.interfaces.ClaimManager;
import dev.santih.shootclans.spatial.QuadTree;
import dev.santih.shootclans.utils.Cuboid;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClaimManagerImpl implements ClaimManager {
	private final ItemStack wandItem = new ItemStack(Material.DIAMOND_HOE);

	private final ShootClans shootClans;

	public ClaimManagerImpl(final ShootClans shootClans) {
		this.shootClans = shootClans;
	}

	QuadTree quadtree = new QuadTree(0, new Cuboid(new Location(Bukkit.getWorld("world"), 0, 0, 0, 0F, 0F), new Location(Bukkit.getWorld("world"), 100000, 0, 100000, 0F, 0F)));

	@Override
	public void saveClaim(ClanPlayer clanPlayer) throws MaxChunksExceededException {
		Location firstPosition = clanPlayer.getFirstPosition();
		Location secondPosition = clanPlayer.getSecondPosition();
		Optional<Clan> optionalClan = clanPlayer.getClan();

		if (firstPosition == null || secondPosition == null || !optionalClan.isPresent()) {
			return;
		}


		firstPosition.setY(1);
		secondPosition.setY(256);

		int numChunks = calculateNumChunks(firstPosition, secondPosition);

		if (numChunks > 3) {
			throw new MaxChunksExceededException("You can only claim a maximum of 3 chunks!");
		}

		Cuboid region = new Cuboid(firstPosition, secondPosition);

		if (intersects(region)) {
			MessageUtils.sendCoreMessage(clanPlayer.getBase(), "&cYour claim overlaps with an existing claim!");
			return;
		}

		// Save the claim in the Clan object
		optionalClan.get().setClaim(region);
		quadtree.insert(region);
		clanPlayer.getBase().sendMessage(ChatColor.GREEN + "Claim saved!");
	}


	@Override
	public boolean intersects(Cuboid newRegion) {
		List<Cuboid> intersectingRegions = new ArrayList<>();
		quadtree.retrieve(intersectingRegions, newRegion);

		// Debug statements
		System.out.println("New region: " + newRegion);
		System.out.println("Intersecting regions: " + intersectingRegions);

		return !intersectingRegions.isEmpty();
	}

	@Override
	public void showNearbyClaims(Player player, double distance) {
		Cuboid searchRegion = new Cuboid(new Location(player.getWorld(), player.getLocation().getX() - distance, 0, player.getLocation().getZ() - distance), new Location(player.getWorld(), player.getLocation().getX() + distance, 256, player.getLocation().getZ() + distance));

		List<Cuboid> nearbyClaims = new ArrayList<>();
		quadtree.retrieve(nearbyClaims, searchRegion);

		System.out.println("Nearby claims: " + nearbyClaims.size());

		for (Cuboid claim : nearbyClaims) {
			setClaimBounds(player, claim.getPoint1(), claim.getPoint2(), Material.LIGHT_BLUE_STAINED_GLASS);
		}
	}

	@Override
	public void setClaimBounds(Player player, Location firstPosition, Location secondPosition, Material material) {
		if (firstPosition != null && secondPosition != null) {
			int minX = Math.min(firstPosition.getBlockX(), secondPosition.getBlockX());
			int minY = Math.min((int) firstPosition.getY(), (int) secondPosition.getY());
			int minZ = Math.min(firstPosition.getBlockZ(), secondPosition.getBlockZ());
			int maxX = Math.max(firstPosition.getBlockX(), secondPosition.getBlockX());
			int maxY = minY + 50;
			int maxZ = Math.max(firstPosition.getBlockZ(), secondPosition.getBlockZ());

			Bukkit.getConsoleSender().sendMessage(firstPosition.toString());
			Bukkit.getConsoleSender().sendMessage(secondPosition.toString());
			World world = firstPosition.getWorld();

			BlockData blockData = material.createBlockData();
			for (int y = minY; y <= maxY; y++) {
				player.sendBlockChange(new Location(world, minX, y, minZ), blockData);
				player.sendBlockChange(new Location(world, minX, y, maxZ), blockData);
				player.sendBlockChange(new Location(world, maxX, y, minZ), blockData);
				player.sendBlockChange(new Location(world, maxX, y, maxZ), blockData);
			}
		}
	}

	@Override
	public void stopShowingNearbyClaims(Player player, double distance) {
		Cuboid searchRegion = new Cuboid(new Location(player.getWorld(), player.getLocation().getX() - distance, 0, player.getLocation().getZ() - distance), new Location(player.getWorld(), player.getLocation().getX() + distance, 256, player.getLocation().getZ() + distance));

		List<Cuboid> nearbyClaims = new ArrayList<>();
		quadtree.retrieve(nearbyClaims, searchRegion);

		for (Cuboid claim : nearbyClaims) {
			setClaimBounds(player, claim.getPoint1(), claim.getPoint2(), Material.AIR);
		}
	}

	@Override
	public boolean isInsideClaim(Location location) {
		List<Cuboid> intersectingRegions = new ArrayList<>();
		quadtree.retrieve(intersectingRegions, new Cuboid(location, location));

		return !intersectingRegions.isEmpty();
	}

	private int calculateNumChunks(Location firstPosition, Location secondPosition) {
		int minX = Math.min(firstPosition.getBlockX(), secondPosition.getBlockX());
		int minZ = Math.min(firstPosition.getBlockZ(), secondPosition.getBlockZ());
		int maxX = Math.max(firstPosition.getBlockX(), secondPosition.getBlockX());
		int maxZ = Math.max(firstPosition.getBlockZ(), secondPosition.getBlockZ());

		return ((maxX - minX) >> 4 + 1) * ((maxZ - minZ) >> 4 + 1);
	}


	@Override
	public ItemStack getWandItem() {
		return wandItem;
	}

}
