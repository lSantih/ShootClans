package dev.santih.shootclans.conversation;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.gui.ManageRank;
import dev.santih.shootclans.clan.gui.TestGui;
import dev.santih.shootclans.clan.rank.CustomRank;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeleteRankPrompt extends StringPrompt {

	private final DependencyFactory dependencyFactory;
	private final CustomRank rank;

	private final Clan clan;

	public DeleteRankPrompt(DependencyFactory dependencyFactory, final CustomRank rank, final Clan clan) {
		this.dependencyFactory = dependencyFactory;
		this.rank = rank;
		this.clan = clan;
	}

	@NotNull
	@Override
	public String getPromptText(@NotNull ConversationContext context) {
		return MessageUtils.color("&eConfirma que quieres eliminar el rango. Para hacerlo, escribe " + rank.getIdentifier() + " en el chat o 'cancelar' para salir.");
	}

	@Nullable
	@Override
	public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
		final Player player = (Player) context.getForWhom();

		if (input.equalsIgnoreCase("cancelar")) {
			MessageUtils.sendCoreMessage(player, "&aConversación cancelada.");
			new ManageRank(player, rank, dependencyFactory).open();
			return END_OF_CONVERSATION;
		}

		
		if (!input.equals(rank.getIdentifier())) {
			player.sendMessage(MessageUtils.color("&cEscribe" + rank.getIdentifier() + " para eliminar el rango o 'cancelar' para volver al menu.-"));
			return this;
		}

		clan.getCustomRanks().remove(rank);
		new TestGui(player, clan, dependencyFactory).open();

		return END_OF_CONVERSATION;
	}
}
