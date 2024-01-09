package dev.santih.shootclans.utils;

import dev.santih.shootclans.ShootClans;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

	private static ConversationFactory factory;

	static {
		factory = new ConversationFactory(ShootClans.getPlugin(ShootClans.class));
		factory.withLocalEcho(false);
	}

	public static <T extends Prompt> void startConversation(final Player player, T conversation) {
		factory.withFirstPrompt(conversation).buildConversation(player).begin();
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
			//colorSet.add(ChatColor(match.substring(1)));
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

	public static void tell(Player player, String text) {
		player.sendMessage(color(text));
	}
}
