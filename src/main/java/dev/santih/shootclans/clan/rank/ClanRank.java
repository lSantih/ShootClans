package dev.santih.shootclans.clan.rank;

import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface ClanRank {

	String getIdentifier();

	String getDisplayName();

	Set<Permission> getPermissions();

	boolean isLeader();

	Material getIconMaterial();

	default ItemStack getIcon() {
		return new ItemBuilder(getIconMaterial())
				.displayName(getDisplayName())
				.lore(
						"",
						"&#5ac8fa► Identificador: &f" + getIdentifier(),
						"&#5ac8fa► Permisos: &#00ff00" + (isLeader() ? Permission.values().length : getPermissions().size()) + "&8/&#ff3300" + Permission.values().length,
						"",
						"&#999999Este rango es un rango predeterminado.",
						"",
						"&#80dfffClick Izquierdo para modificar"
				).build();
	}

	default void addPermission(Permission permission) {
		if (permission == null || getPermissions().contains(permission)) return;
		getPermissions().add(permission);
	}

	default void removePermission(Permission permission) {
		if (permission == null || !getPermissions().contains(permission)) return;
		getPermissions().remove(permission);
	}

	default boolean hasPermission(Permission permission) {
		return permission != null && getPermissions().contains(permission);
	}

	void setDisplayName(String newDisplay);
}
