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
		String arr[] = new String[3];
		if(config.numOfPlayers() >= 1 ) {
			final String messageReceived = chatMessage.getMessage();
			if (messageReceived.contains("<col=ef1020>Valuable drop:")&&chatMessage.getType() == ChatMessageType.GAMEMESSAGE) {
				System.out.println("HERHEHR");
				messageReceived.replaceAll("<col=ef1020>","");

				if(messageReceived.contains(" x ")) { //The X determines if the drop was a single quantity drop or not.
					Pattern p = Pattern.compile("\\d+|(?<= x )(.*)(?=\\()");
					Matcher m = p.matcher(messageReceived);
					int i = 0;
					while(m.find()) {
						// Either its single drop or multiple item drop depending on which value gets assigned where in the array
						// arr[0] - Item Stack Size
						// arr[1] - Item Name
						// arr[2] - Total GP
						arr[i] = m.group();
						i++;
					}
					final int split = (Integer.parseInt(arr[2]) / (config.numOfPlayers()+1));
					final String itemName = arr[1];
					final int itemQuantity = Integer.parseInt(arr[0]);
					if(config.printGP()){
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "GP split for " + config.numOfPlayers() + " other player(s) is" +"<col=22ff00> "+ String.format ("%,d", split) + "<col=ef1020>gp", null);
					}

						if(config.printItemAmt()){
							client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "Item split for " + config.numOfPlayers() + " other player(s) of" + itemName + " is: <col=22ff00>" + (itemQuantity / (config.numOfPlayers()+1)), null);
						}
					}

				//For drops with one item
				 else {
					Pattern p = Pattern.compile("\\d+|(?<=Valuable Drop: )(.*)(?=\\()");
					Matcher m = p.matcher(messageReceived);
					int i = 0;
					while (m.find() && i < 4) {
						//arr[0] - name
						//arr[1] - gp value
						System.out.println(m.group());
						arr[i] = m.group();
						i++;
					}

					final String itemName = arr[0];
					final int split = (Integer.parseInt(arr[1]) / config.numOfPlayers() + 1);

					if (config.printGP()) {
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "GP split for " + config.numOfPlayers() + " other player(s) is" + "<col=22ff00> " + String.format("%,d", split) + "<col=ef1020>gp", null);
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
