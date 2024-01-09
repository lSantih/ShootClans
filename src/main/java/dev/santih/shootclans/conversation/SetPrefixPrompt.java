package dev.santih.shootclans.conversation;

import dev.santih.shootclans.clan.gui.ManageRank;
import dev.santih.shootclans.clan.rank.ClanRank;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetPrefixPrompt extends StringPrompt {

	private final DependencyFactory dependencyFactory;
	private final ClanRank rank;

	public SetPrefixPrompt(DependencyFactory dependencyFactory, final ClanRank rank) {
		this.dependencyFactory = dependencyFactory;
		this.rank = rank;
	}

	@NotNull
	@Override
	public String getPromptText(@NotNull ConversationContext context) {
		return MessageUtils.color("&eEscribe el nuevo prefijo para el rango.");
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

		rank.setDisplayName(MessageUtils.color(input));
		new ManageRank(player, rank, dependencyFactory).open();
		return END_OF_CONVERSATION;
	}
}
