package dev.santih.shootclans.clan.rank.impl;

import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.clan.rank.CustomRank;
import dev.santih.shootclans.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Set;


public class RankCustom implements CustomRank {
	private final Set<Permission> permissions;
	private String identifier;
	private String displayName;
	private Material iconMaterial;

	public RankCustom() {
		this.permissions = EnumSet.noneOf(Permission.class);
		this.identifier = "Nombre por defecto";
		this.displayName = "&fPrefijo por defecto";
		this.iconMaterial = Material.ENCHANTED_GOLDEN_APPLE;
	}

	@Override
	public String getIdentifier() {
		return identifier;
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
		return iconMaterial;
	}

	@Override
	public boolean isLeader() {
		return false;
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(getIconMaterial())
				.displayName(getDisplayName())
				.lore(
						"",
						"&#5ac8fa► Identificador: &f" + getIdentifier(),
						"&#5ac8fa► Permisos: &#00ff00" + getPermissions().size() + "&8/&#ff3300" + Permission.values().length,
						"",
						"&#999999Este rango es un rango personalizado.",
						"",
						"&#80dfffClick Izquierdo para modificar"
				).build();
	}

	@Override
	public void setIdentifier(String newIdentifier) {
		this.identifier = newIdentifier;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
