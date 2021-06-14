package Lana.lootsplitter;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class runLootSplitter
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(LootSplitterPlugin.class);
		RuneLite.main(args);
	}
}