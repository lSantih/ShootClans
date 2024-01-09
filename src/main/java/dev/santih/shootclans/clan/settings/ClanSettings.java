package dev.santih.shootclans.clan.settings;

public interface ClanSettings<T> {

	String getName();

	ClanSettingType getType();

	T getCurrentValue();

	void setCurrentValue(T newValue);

}
