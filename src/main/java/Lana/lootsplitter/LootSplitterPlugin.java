package Lana.lootsplitter;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Loot Splitter"
)
public class LootSplitterPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private LootSplitterConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.debug("Loot Splitter started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.debug("Loot Splitter stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{

		if(config.numOfPlayers() >= 1) {

			final String messageReceived = chatMessage.getMessage();

			if (messageReceived.contains("<col=ef1020>Valuable drop:")&&chatMessage.getType() == ChatMessageType.GAMEMESSAGE) {
				if(countOccurences(messageReceived,'(',0) <= 1){ //Checking how many ('s are in the string (should be one, can be two).
					final String coinMessage = messageReceived.substring(messageReceived.indexOf("(") + 1, messageReceived.indexOf(" coins"));
					final int coinValue = Integer.parseInt(coinMessage.replaceAll(",", ""));
					final int split = (coinValue / (config.numOfPlayers()+1));
					if(config.printGP()){
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "GP split for " + config.numOfPlayers() + " other player(s) is" +"<col=22ff00> "+ String.format ("%,d", split) + "<col=ef1020>gp", null);
					}
					if (!messageReceived.contains(" x ")) { //Single item drops don't need quantity split.

					} else {
						final String items = messageReceived.substring(messageReceived.indexOf(":") + 1, messageReceived.indexOf("(") - 1);
						final String itemName = items.substring(items.indexOf("x") + 1);
						final String itemQuantity = items.substring(0, items.indexOf("x"));
						if(config.printItemAmt()){
							client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "Item split for " + config.numOfPlayers() + " other player(s) of" + itemName + " is: <col=22ff00>" + (Integer.parseInt(itemQuantity.replaceAll(" ", "")) / (config.numOfPlayers()+1)), null);
						}
					}
				} else { //If two parentheses within the text (as in drop has it Dragon Bolts (unf) for example) we skip the first one!
					final String coinMessage = messageReceived.substring(messageReceived.indexOf("(", messageReceived.indexOf("(")+ 1) + 1, messageReceived.indexOf(" coins"));
					final int coinValue = Integer.parseInt(coinMessage.replaceAll(",", ""));
					final int split = (coinValue / (config.numOfPlayers()+1));
					if(config.printGP()){
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "GP split for " + config.numOfPlayers() + " other player(s) is" +"<col=22ff00> "+ String.format ("%,d", split) + "<col=ef1020>gp", null);
					}
					if (!messageReceived.contains(" x ")) { //Single item drops don't need quantity split.

					} else {
						final String items = messageReceived.substring(messageReceived.indexOf(":") + 1, messageReceived.indexOf("(") - 1);
						final String itemName = items.substring(items.indexOf("x") + 1);
						final String itemQuantity = items.substring(0, items.indexOf("x"));
						if(config.printItemAmt()){
							client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "Item split for " + config.numOfPlayers() + " other player(s) of" + itemName + " is: <col=22ff00>" + (Integer.parseInt(itemQuantity.replaceAll(" ", "")) / (config.numOfPlayers()+1)), null);
						}
					}
				}

			}
		}
	}
	private static int countOccurences(
			String someString, char searchedChar, int index) {
		if (index >= someString.length()) {
			return 0;
		}

		int count = someString.charAt(index) == searchedChar ? 1 : 0;
		return count + countOccurences(
				someString, searchedChar, index + 1);
	}

	@Provides
	LootSplitterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LootSplitterConfig.class);
	}
}
