package dev.santih.shootclans.conversation;

import dev.santih.shootclans.clan.gui.ManageRank;
import dev.santih.shootclans.clan.rank.CustomRank;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.RankManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetIdentifierPrompt extends StringPrompt {
	private final RankManager rankManager;

	private final DependencyFactory dependencyFactory;
	private final CustomRank rank;

	public SetIdentifierPrompt(DependencyFactory dependencyFactory, final CustomRank rank) {
		this.dependencyFactory = dependencyFactory;
		this.rankManager = dependencyFactory.getRankManager();
		this.rank = rank;
	}

	@NotNull
	@Override
	public String getPromptText(@NotNull ConversationContext context) {
		return MessageUtils.color("&eEscribe el nuevo nombre para el rango.");
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
		if (rankManager.getRankByIdentifier(input).isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYa existe un rango con ese nombre. Selecciona otro o escribe cancelar para salir de este chat.");
			return this;
		}

		if (isNumber(input)) {
			MessageUtils.sendCoreMessage(player, "&cEl nombre no puede contener numeros. Selecciona otro o escribe cancelar para salir de este chat.");
			return this;
		}

		rank.setIdentifier(input);
		new ManageRank(player, rank, dependencyFactory).open();
		return END_OF_CONVERSATION;
	}

	private boolean isNumber(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
