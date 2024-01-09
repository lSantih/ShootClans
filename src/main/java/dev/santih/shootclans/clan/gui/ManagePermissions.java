package dev.santih.shootclans.clan.gui;

import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.clan.rank.ClanRank;
import dev.santih.shootclans.managers.interfaces.RankManager;
import dev.santih.shootclans.utils.ItemBuilder;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;
import java.util.List;

public class ManagePermissions extends Gui {

	private final RankManager rankManager;

	private final ClanRank rank;

	public ManagePermissions(Player player, final RankManager rankManager, final ClanRank rank) {
		super(player, "test-gui", "Editando permisos", 6);
		this.rank = rank;
		this.rankManager = rankManager;
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
		fillGui(Material.AIR);
		fillGui(new Icon(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("&bShootMC").build()));
		addItem(new Icon(new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).displayName("&bShootMC").build()), 0, 1, 9, 7, 8, 17, 36, 45, 46, 44, 52, 53);
		addItem(new Icon(new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).displayName("&bShootMC").build()), 2, 3, 4, 5, 6, 18, 27, 47, 48, 49, 50, 51, 26, 35);

		addItem(new Icon(new ItemBuilder(
				Material.ENCHANTED_GOLDEN_APPLE)
				.displayName("&#5ac8fa► Icono")
				.lore("", "&#999999Iconos admitidos: &fTodos", "", "&#80dfffArrastrar item para modificar").build()), 4);

		int slot = 19;
		for (Permission permission : Permission.values()) {
			boolean rankHasPermission = rank.hasPermission(permission);
			if(slot == 25) slot = 28;
			if(slot == 34) break;

			List<String> permissionLore = new ArrayList<>(permission.getDescription());
			permissionLore.add("");
			permissionLore.add(rankHasPermission ? "&#ff3300Click izquierdo para denegar acceso." : "&#66ff33Click izquierdo para permitir acceso.");
			addItem(slot, new Icon(new ItemBuilder
					(rankHasPermission ? Material.LIME_CONCRETE : Material.RED_CONCRETE)
					.displayName(permission.getFormattedName())
					.lore(permissionLore).build()).onClick(click -> handlePermission(permission, rank, event)));
			slot++;
		}

		addItem(new Icon(new ItemBuilder(
				Material.TIPPED_ARROW)
				.displayName("&#5ac8fa► Volver atras")
				.build()).onClick(closeEvent -> closeEvent.getWhoClicked().closeInventory()), 49);
	}

	private void handlePermission(Permission permission, ClanRank clanRank, InventoryOpenEvent event) {
		if (clanRank.hasPermission(permission)) {
			clanRank.removePermission(permission);
			onOpen(event);
			return;
		}

		clanRank.addPermission(permission);
		onOpen(event);
	}
}