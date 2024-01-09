package dev.santih.shootclans.clan.request;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
public record AllyRequest(ClanPlayer sender, ClanPlayer receiver) implements Request {
	@Override
	public ClanRequestType getRequestType() {
		return ClanRequestType.ALLIANCE;
	}

	@Override
	public void processRequest(ClanPlayer player) {
		player.getClan().ifPresent(clan -> {
			Clan senderClan = sender.getClan().orElse(null);
			Clan receiverClan = receiver.getClan().orElse(null);

			if (senderClan != null && receiverClan != null) {
				senderClan.addAlly(receiverClan.getName());
				receiverClan.addAlly(senderClan.getName());
			}
		});
	}
}
