package dev.santih.shootclans.clan.rank.impl;

import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.clan.rank.ClanRank;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

public class RankCaptain implements ClanRank {
	private final Set<Permission> permissions = EnumSet.of(
			Permission.INVITE
	);

	private String displayName = "&#ffd24C&lCaptain";

	@Override
	public String getIdentifier() {
		return "Captain";
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
	public boolean isLeader() {
		return false;
	}

	@Override
	public Material getIconMaterial() {
		return Material.YELLOW_CONCRETE;
	}

	@Override
	public void setDisplayName(String newDisplay) {
		this.displayName = newDisplay;
	}
}
