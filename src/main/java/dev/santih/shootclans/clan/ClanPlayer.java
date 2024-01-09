package dev.santih.shootclans.clan;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class ClanPlayer {

	private final UUID uniqueID;
	private boolean leader;
	private Optional<Clan> clan;
	private final Player base;
	private EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);
	private Location firstPosition;
	private Location secondPosition;
	private int deaths;

	public ClanPlayer(final UUID uuid) {
		this.uniqueID = uuid;
		this.clan = Optional.empty();
		this.base = Bukkit.getPlayer(uuid);
		this.deaths = 0;

		//DEBUG ONLY -> MAKE ALL PLAYERS HAVE ALL PERMISSIONS.
		for (Permission value : Permission.values()) {
			permissions.add(value);
		}
	}

	public Optional<Clan> getClan() {
		return clan;
	}

	public void setLeader(boolean leader) {
		this.leader = leader;
	}

	public boolean isLeader() {
		return leader;
	}

	public void setClan(Optional<Clan> clan) {
		this.clan = clan;
	}

	public void addPermission(Permission permission) {
		permissions.add(permission);
	}

	public void removePermission(Permission permission) {
		permissions.remove(permission);
	}

	public boolean hasPermission(Permission permission) {
		return permissions.contains(permission);
	}

	public Player getBase() {
		return base;
	}

	public void setFirstPosition(Location firstPosition) {
		this.firstPosition = firstPosition;
	}

	public void setSecondPosition(Location secondPosition) {
		this.secondPosition = secondPosition;
	}

	public Location getFirstPosition() {
		return firstPosition;
	}

	public Location getSecondPosition() {
		return secondPosition;
	}

	public String getName() {
		return getBase().getName();
	}
}
