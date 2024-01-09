package dev.santih.shootclans.clan.request;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.exception.AlreadyInThisClanException;
import dev.santih.shootclans.exception.NotInClanException;

import java.util.Optional;

public record InviteRequest(ClanPlayer sender, ClanPlayer receiver) implements Request {

	@Override
	public ClanRequestType getRequestType() {
		return ClanRequestType.INVITE;
	}

	@Override
	public void processRequest(ClanPlayer player) throws AlreadyInClanException, AlreadyInThisClanException, NotInClanException {
		Clan clan = sender.getClan().orElseThrow(() -> new NotInClanException("Sender is not in a clan"));
		clan.addPlayer(player);
		player.setClan(Optional.of(clan));
	}
}
