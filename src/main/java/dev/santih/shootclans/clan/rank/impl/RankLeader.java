package dev.santih.shootclans.clan.rank.impl;

import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.clan.rank.ClanRank;
import org.bukkit.Material;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class RankLeader implements ClanRank {
	private final Set<Permission> permissions = Collections.unmodifiableSet(EnumSet.noneOf(Permission.class));

	private String displayName = "&#ff6347&lLeader";

	@Override
	public String getIdentifier() {
		return "Leader";
	}

	@Override
	public String getDisplayName() {
		return "&#ff6347&lLeader";
	}

	@Override
	public void addPermission(Permission permission) {
		return;
	}

	@Override
	public void removePermission(Permission permission) {
		return;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return true;
	}

	@Override
	public Set<Permission> getPermissions() {
		return permissions;
	}

	@Override
	public boolean isLeader() {
		return true;
	}

	@Override
	public Material getIconMaterial() {
		return Material.RED_CONCRETE;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
