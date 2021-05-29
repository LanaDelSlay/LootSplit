package Lana.lootsplitter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("lootsplitter")
public interface LootSplitterConfig extends Config
{
	@ConfigItem(
			keyName = "numOfPlayers",
			name = "How many to split with",
			description = "How many players is the loot being split with? (Includes yourself)",
			position = 0
	)
	default int numOfPlayers()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "printGP",
			name = "Split GP Value",
			description = "Displays the split value of the drop in GP",
			position = 1
	)
	default boolean printGP()
	{
		return true;
	}

	@ConfigItem(
			keyName = "printItemAmt",
			name = "Split Item Amount",
			description = "Displays the split ammount of items dropped",
			position = 2
	)
	default boolean printItemAmt()
	{
		return true;
	}
}
