package Lana.lootsplitter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("lootsplitter")
public interface LootSplitterConfig extends Config
{
	@ConfigSection(
			name = "Display In Chat",
			description = "Options to configure how this will output text into the chat box!",
			position = -2,
			closedByDefault = true
	)
	String chatSection = "chatSection";

	@ConfigItem(
			keyName = "numOfPlayers",
			name = "How many to split with",
			description = "How many players is the loot being split with? (Includes yourself, so 1 = 2 players total)",
			position = 0,
			section = chatSection
	)
	default int numOfPlayers()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "printGP",
			name = "Split GP Value",
			description = "Displays the split value of the drop in GP",
			position = 1,
			section = chatSection
	)
	default boolean printGP()
	{
		return true;
	}

	@ConfigItem(
			keyName = "printItemAmt",
			name = "Split Item Amount",
			description = "Displays the split ammount of items dropped",
			position = 2,
			section = chatSection
	)
	default boolean printItemAmt()
	{
		return true;
	}

	@ConfigSection(
			name = "Display On Screen",
			description = "Options to controll how the information is displayed on screen!",
			position = -2,
			closedByDefault = true
	)
	String menuSection = "menuSection";

	@ConfigItem(
			keyName = "showMenu",
			name = "Show Onscreen Overlay",
			description = "This will display the info on screen as opposed to in the chat box! ",
			position = 3,
			section = menuSection
	)
	default boolean showMenu()
	{
		return true;
	}

	@ConfigItem(
			keyName = "menuTimeout",
			name = "Menu Timeout Duration",
			description = "How long it takes for the menu to disappear after displaying loot split!",
			position = 4,
			section = menuSection
	)
	default int menuTimeout()
	{
		return 30;
	}

}
