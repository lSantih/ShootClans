package dev.santih.shootclans.clan;

import dev.santih.shootclans.clan.rank.ClanRank;
import dev.santih.shootclans.clan.rank.impl.RankCustom;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class Clan {
	private double balance;
	private boolean friendlyFire;
	private String name;
	private Set<ClanPlayer> players;
	private Set<String> allies; // Storing allies by clan name
	private Location clanHome;
	private Cuboid claim;
	private Set<ClanRank> customRanks = new HashSet<>();

	public Clan(final String name) {
		this.name = name;
		this.players = new HashSet<>();
		this.allies = new HashSet<>();
	}

	public void addPlayer(ClanPlayer player) throws AlreadyInClanException {
		if (player.getClan().isPresent()) {
			throw new AlreadyInClanException("Player is already in another clan");
		}
		if (players.contains(player)) return;

		players.add(player);
		player.setClan(Optional.of(this));
	}

	public String getName() {
		return name;
	}

	public boolean isFriendlyFire() {
		return friendlyFire;
	}

	public Set<ClanPlayer> getPlayers() {
		return players;
	}

	public Set<String> getAllies() {
		return allies;
	}

	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public boolean isLeader(ClanPlayer player) {
		return player.isLeader() && players.contains(player);
	}

	public void removePlayer(ClanPlayer player) {
		if (!players.contains(player)) {
			// Handle player not in clan
			return;
		}
		players.remove(player);
		player.setClan(Optional.empty());
		player.setLeader(false);
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void addBalance(double amount) {
		balance += amount;
	}

	public void removeBalance(double amount) {
		balance -= amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setClaim(Cuboid claim) {
		this.claim = claim;
	}

	public Cuboid getClaim() {
		return claim;
	}

	public void setLocation(Location location) {
		this.clanHome = location;
	}

	public void teleport(Player player) {
		player.teleport(clanHome);
	}

	public Location getClanHome() {
		return clanHome;
	}

	public void saveData(FileConfiguration config) {
		config.set("name", name);
		config.set("friendlyFire", friendlyFire);
		config.set("balance", balance);

		if (clanHome != null) {
			config.set("clanLocation.world", clanHome.getWorld().getName());
			config.set("clanLocation.x", clanHome.getX());
			config.set("clanLocation.y", clanHome.getY());
			config.set("clanLocation.z", clanHome.getZ());
			config.set("clanLocation.yaw", clanHome.getYaw());
			config.set("clanLocation.pitch", clanHome.getPitch());
		}

		if (claim != null) {
			Location point1 = claim.getPoint1();
			Location point2 = claim.getPoint2();
			config.set("claim.world", point1.getWorld().getName());
			config.set("claim.x1", point1.getX());
			config.set("claim.y1", point1.getY());
			config.set("claim.z1", point1.getZ());
			config.set("claim.x2", point2.getX());
			config.set("claim.y2", point2.getY());
			config.set("claim.z2", point2.getZ());
		}

		List<String> playerUUIDs = new ArrayList<>();
		for (ClanPlayer player : players) {
			playerUUIDs.add(player.getBase().getUniqueId().toString());
		}
		config.set("players", playerUUIDs);
	}

	// Ally Logic

	public void addAlly(String allyClanName) {
		allies.add(allyClanName);
	}

	public void removeAlly(String allyClanName) {
		allies.remove(allyClanName);
	}

	public Optional<ClanPlayer> getLeader() {
		return players.stream().filter(player -> player.isLeader()).findFirst();
	}

	public Set<ClanRank> getCustomRanks() {
		return customRanks;
	}

	public Optional<ClanRank> getRankByIdentifier(final String identifier) {
		return customRanks.stream().filter(rank -> rank.getIdentifier().equals(identifier)).findFirst();
	}

	public void addCustomRank(final RankCustom rankCustom) {
		customRanks.add(rankCustom);
	}

	public boolean isAlly(String clanName) {
		return allies.contains(clanName);
	}
}