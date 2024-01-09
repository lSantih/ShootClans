package dev.santih.shootclans.clan.gui;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.rank.ClanRank;
import dev.santih.shootclans.clan.rank.CustomRank;
import dev.santih.shootclans.conversation.DeleteRankPrompt;
import dev.santih.shootclans.conversation.SetIdentifierPrompt;
import dev.santih.shootclans.conversation.SetPrefixPrompt;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.utils.Common;
import dev.santih.shootclans.utils.ItemBuilder;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class ManageRank extends Gui {

	private final ClanRank clanRank;

	private final Clan clan;
	private final DependencyFactory dependencyFactory;

	public ManageRank(Player player, final ClanRank clanRank, DependencyFactory dependencyFactory) {
		super(player, "test-gui", "Manage Rank!!", 6);

		this.clanRank = clanRank;
		this.clan = dependencyFactory.getPlayerManager().getClanPlayer(player).getClan().get();
		this.dependencyFactory = dependencyFactory;
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
		fillGui(new Icon(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("&bShootMC").build()));
		addItem(new Icon(new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).displayName("&bShootMC").build()), 0, 1, 9, 7, 8, 17, 36, 45, 46, 44, 52, 53);
		addItem(new Icon(new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).displayName("&bShootMC").build()), 2, 3, 4, 5, 6, 18, 27, 47, 48, 49, 50, 51, 26, 35);

		addItem(new Icon(new ItemBuilder(
				Material.GLOW_ITEM_FRAME)
				.displayName("&#5ac8fa► Cambiar Prefijo")
				.lore("", "&#999999Actual: " + clanRank.getDisplayName(), "", "&#80dfffClick Izquierdo para modificar").build()).onClick(click -> handleSetPrefix()), 20);

		addItem(new Icon(new ItemBuilder(
				Material.NAME_TAG)
				.displayName("&#5ac8fa► Cambiar Nombre")
				.lore("", "&#999999Actual: &f" + clanRank.getIdentifier(), "", (!(clanRank instanceof CustomRank) ? "&#80dfffNo modificable" : "&#80dfffClick Izquierdo para modificar")).build()).onClick(click -> handleSetIdentifier()), 22);

		addItem(new Icon(new ItemBuilder(
				Material.END_CRYSTAL)
				.displayName("&#5ac8fa► Permisos")
				.lore("", "&#999999Permisos: &f" + clanRank.getPermissions().size(), "", "&#80dfffClick Izquierdo para administrar").build()).onClick(click -> handlePermission()), 24);

		addItem(new Icon(new ItemBuilder(
				Material.ENCHANTED_GOLDEN_APPLE)
				.displayName("&#5ac8fa► Icono")
				.lore("", "&#999999Iconos admitidos: &fTodos", "", "&#80dfffArrastrar item para modificar").build()), 30);

		addItem(new Icon(new ItemBuilder(
				Material.BLAZE_POWDER)
				.displayName("&#5ac8fa► Eliminar Rango")
				.lore("", "&#ffff99⚠ &#999999Esta acción es irreversible", "", (!(clanRank instanceof CustomRank) ? "&#80dfffNo eliminable" : "&#80dfffClick Izquierdo para eliminar")).build()).onClick(click -> handleRankDelete()), 32);

		addItem(new Icon(new ItemBuilder(
				Material.BARRIER)
				.displayName("&#5ac8fa► Cerrar Menú")
				.build()).onClick(closeEvent -> closeEvent.getWhoClicked().closeInventory()), 49);
	}

	private void handleSetIdentifier() {
		if (!(clanRank instanceof CustomRank)) return;
		player.closeInventory();
		Common.startConversation(player, new SetIdentifierPrompt(dependencyFactory, (CustomRank) clanRank));
	}

	private void handleSetPrefix() {
		player.closeInventory();
		Common.startConversation(player, new SetPrefixPrompt(dependencyFactory, clanRank));
	}

	private void handleRankDelete() {
		player.closeInventory();
		if (!(clanRank instanceof CustomRank)) return;

		Common.startConversation(player, new DeleteRankPrompt(dependencyFactory, (CustomRank) clanRank, clan));
	}

	private void handlePermission() {
		player.closeInventory();
		new ManagePermissions(player, dependencyFactory.getRankManager(), clanRank).open();
	}
}
