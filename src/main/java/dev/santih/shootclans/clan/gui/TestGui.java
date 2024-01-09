package dev.santih.shootclans.clan.gui;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.rank.ClanRank;
import dev.santih.shootclans.clan.rank.CustomRank;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.managers.interfaces.RankManager;
import dev.santih.shootclans.utils.ItemBuilder;
import dev.santih.shootclans.utils.MessageUtils;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class TestGui extends Gui {

	private final RankManager rankManager;
	private final Clan clan;

	private final PlayerManager playerManager;

	private final DependencyFactory dependencyFactory;

	public TestGui(Player player, final Clan clan, DependencyFactory dependencyFactory) {
		super(player, "test-gui", "Test Title!!", 6);
		this.rankManager = dependencyFactory.getRankManager();
		this.clan = clan;
		this.playerManager = dependencyFactory.getPlayerManager();
		this.dependencyFactory = dependencyFactory;
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
		fillGui(new Icon(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("&bShootMC").build()));
		addItem(new Icon(new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).displayName("&bShootMC").build()), 0, 1, 9, 36, 45, 7, 8, 17, 44, 53);
		addItem(new Icon(new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).displayName("&bShootMC").build()), 2, 10, 18, 27, 6, 16, 26, 35);

		ClanRank member = rankManager.getMemberRank();
		ClanRank captain = rankManager.getCaptainRank();
		ClanRank coLeader = rankManager.getCoLeaderRank();
		ClanRank leader = rankManager.getLeaderRank();

		addItem(new Icon(member.getIcon()).onClick(click -> handleEdit(member)), 20);
		addItem(new Icon(captain.getIcon()).onClick(click -> handleEdit(captain)), 21);
		addItem(new Icon(coLeader.getIcon()).onClick(click -> handleEdit(coLeader)), 23);
		addItem(new Icon(leader.getIcon()).onClick(click -> handleEdit(leader)), 24);


		addItem(new Icon(new ItemBuilder(
				Material.LIME_STAINED_GLASS_PANE)
				.displayName("&#99ff33Crear Rango")
				.lore("&#999999Este slot se encuentra libre.", "", "&eClick Izquierdo para crear rango").build()).onClick(clickEvent -> handleCreate(clickEvent)), 38, 39, 40, 41, 42);


		int slot = 38;
		for (final ClanRank clanRank : clan.getCustomRanks()) {
			if (slot == 42) break;

			addItem(new Icon(clanRank.getIcon()).onClick(rank -> handleEdit(clanRank)), slot);
			slot++;
		}
	}

	private void handleCreate(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();

		if (playerManager.getClanPlayer(player) == null) {
			MessageUtils.sendCoreMessage(player, "&cTu perfil no esta cargado. Contacta a un administrador.");
			player.closeInventory();
			return;
		}

		final ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		if (!clanPlayer.getClan().isPresent()) return;

		int rankNumber = (event.getSlot() - 37);
		final CustomRank createdRank = rankManager.createCustomRank(clan);
		createdRank.setIdentifier("Rango #" + rankNumber);
		addItem(new Icon(createdRank.getIcon()).onClick(manageEvent -> handleEdit(createdRank)), event.getSlot());
	}

	private void handleEdit(final ClanRank clanRank) {
		Bukkit.getConsoleSender().sendMessage(String.valueOf(clanRank));
		new ManageRank(player, clanRank, dependencyFactory).open();
	}
}