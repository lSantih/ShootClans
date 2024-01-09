package dev.santih.shootclans.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {
	public static final Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
	public static final char COLOR_CHAR = '&';

	public static void sendCoreMessage(final Player player, final String message) {
		player.sendMessage(color(message));
	}

	public static void sendCoreMessage(final CommandSender player, final String message) {
		player.sendMessage(color(message));
	}

	public static void sendPrivateMessage(Player sender, Player recipient, String message) {
		String formattedMessage = color("&7[&d" + sender.getName() + " &7-> &dme&7] " + message);
		recipient.sendMessage(formattedMessage);

		formattedMessage = color("&7[&dme &7-> &d" + recipient.getName() + "&7] " + message);
		sender.sendMessage(formattedMessage);
	}

	public static String color(String s) {
		s = ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, s);
		s = findAndReplaceRegex("!#[0-9,a-f,A-F]{6}", s);
		s = findAndReplaceRegex("&#[0-9,a-f,A-F]{6}", s);
		s = ChatColor.translateAlternateColorCodes('&', s);
		//System.out.println("String is: " + s);
		return s;
	}

	private static String findAndReplaceRegex(String regex, String input) {
		int i = 0;
		ArrayList<String> matches = new ArrayList<>();
		ArrayList<ChatColor> colorSet = new ArrayList<>();
		Matcher patternMatcher = Pattern.compile(regex).matcher(input);
		while (patternMatcher.find()) {
			matches.add(patternMatcher.group());
		}
		for (String match : matches) {
			//colorSet.add(ChatColor.of(match.substring(1)));
		}
		Iterator<String> matchIterator = matches.iterator();
		Iterator<ChatColor> colorIterator = colorSet.iterator();
		while (matchIterator.hasNext() && colorIterator.hasNext()) {
			input = input.replaceFirst(matchIterator.next(), colorIterator.next().toString());
		}
		return input;
	}

	public static List<String> color(final List<String> original) {
		final List<String> colorizedList = new ArrayList<>();
		original.forEach(line -> colorizedList.add(color(line)));
		return colorizedList;
	}
}
