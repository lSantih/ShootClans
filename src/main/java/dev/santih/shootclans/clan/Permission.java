package dev.santih.shootclans.clan;

import dev.santih.shootclans.utils.MessageUtils;

import java.util.Arrays;
import java.util.List;

public enum Permission {
	INVITE("&#5ac8fa► Invitar Jugador", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran invitar otros miembros al clan")),
	SET_HOME("&#5ac8fa► Establecer Base", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran establecer la base del clan")),
	KICK("&#5ac8fa► Expulsar Jugador", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran expulsar otros miembros del clan")),
	HOME("&#5ac8fa► Home", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran teletransportarse al home del clan")),
	WITHDRAW_BALANCE("&#5ac8fa► Sacar Dinero", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran sacar dinero de la cuenta del clan")),
	DEPOSIT_BALANCE("&#5ac8fa► Depositar Dinero", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran depositar dinero en la cuenta del clan")),
	VIEW_BALANCE("&#5ac8fa► Ver Dinero", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran ver el dinero disponible del clan")),
	CLAIM_WAND("&#5ac8fa► Claimear", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran claimear territorio del clan")),
	UNCLAIM_WAND("&#5ac8fa► Quitar Claim", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran claimear territorio del clan")),
	ACCEPT_ALLY("&#5ac8fa► Aceptar Alianza", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran quitar el territorio del clan")),
	DENY_ALLY("&#5ac8fa► Denegar Alianza", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran denegar alianzas con otros clanes")),
	SEND_ALLY("&#5ac8fa► Aceptar Alianza", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran enviar alianzas con otros clanes")),
	REMOVE_ALLY("&#5ac8fa► Eliminar Alianza", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran eliminar alianzas existentes del clan")),
	MOB_DAMAGE("&#5ac8fa► Daño a entidades", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran dañar entidades dentro del territorio")),
	INTERACT("&#5ac8fa► Interactuar/Construir", Arrays.asList("", "&7Los jugadores con este permiso", "&7podran interactuar y construir dentro del territorio"));

	private String formattedName;
	private List<String> description;
	
	Permission(String formattedName, List<String> description) {
		this.formattedName = formattedName;
		this.description = description;
	}

	public String getFormattedName() {
		return MessageUtils.color(formattedName);
	}

	public List<String> getDescription() {
		return description;
	}
}
