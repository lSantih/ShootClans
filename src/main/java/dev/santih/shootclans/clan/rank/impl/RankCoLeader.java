package dev.santih.shootclans.clan.rank.impl;

import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.clan.rank.ClanRank;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

public class RankCoLeader implements ClanRank {
	private final Set<Permission> permissions = EnumSet.of(
			Permission.INVITE
	);

	private String displayName = "&#ffa500&lCo-Leader";

	@Override
	public String getIdentifier() {
		return "Co-Leader";
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Set<Permission> getPermissions() {
		return permissions;
	}

	@Override
	public Material getIconMaterial() {
		return Material.ORANGE_CONCRETE;
	}

	@Override
	public boolean isLeader() {
		return false;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
