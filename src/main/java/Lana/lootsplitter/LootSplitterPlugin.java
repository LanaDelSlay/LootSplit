package Lana.lootsplitter;

import com.google.inject.Provides;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
        name = "Loot Splitter",
        description = "A plugin to tell you how much to split when bossing with friends.",
        tags = {"bossing","split", "duo", "trio"},
        enabledByDefault = true
)
public class LootSplitterPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private LootSplitterConfig config;

	@Inject
	private OverlayManager overlayManager;


    @Inject
    private LootSplitterPlugin plugin;

    @Override
    protected void startUp() throws Exception {
            log.debug("Loot Splitter started!");
	}

    @Override
    protected void shutDown() throws Exception {
        log.debug("Loot Splitter stopped!");
    }

    int gpValue;
    int quantity;
    String itemNameStr;
    Boolean firstMenuDisplayed = false;
    Boolean secondMenuDisplayed = false;



    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        String[] arr = new String[3];

        if (config.numOfPlayers() >= 1) {
             String messageReceived = chatMessage.getMessage().replace(",","");
            if (messageReceived.contains("<col=ef1020>Valuable drop:") && chatMessage.getType() == ChatMessageType.GAMEMESSAGE) {
            //if (messageReceived.contains("Valuable drop:")) {
                messageReceived = messageReceived.substring(11); //removes color
                if (messageReceived.contains(" x ")) { //The X determines if the drop was a single quantity drop or not.
                    Pattern p = Pattern.compile("\\d+|(?<= x )(.*)(?=\\()");
                    Matcher m = p.matcher(messageReceived);
                    int i = 0;
                    while (m.find()) {
                        // Either its single drop or multiple item drop depending on which value gets assigned where in the array
                        // arr[0] - Item Stack Size
                        // arr[1] - Item Name
                        // arr[2] - Total GP
                        arr[i] = m.group();
                        i++;
                    }
                    final int split = (Integer.parseInt(arr[2]) / (config.numOfPlayers() + 1));
                    final String itemName = arr[1];
                    final int itemQuantity = Integer.parseInt(arr[0]);
                    gpValue = split;
                    quantity = itemQuantity / 2;
                    itemNameStr = itemName;
                    if (config.printGP()) {
                        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=FFFFFF>" + "GP split for " + config.numOfPlayers() + " other player(s) is" + "<col=ef1020> " + String.format("%,d", split) + "<col=ef1020>gp", null);
                    }

                    if (config.printItemAmt()) {
                        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=FFFFFF>" + "Item split for " + config.numOfPlayers() + " other player(s) of " + itemName + " is: <col=ef1020>" + (itemQuantity / (config.numOfPlayers() + 1)), null);
                    }
                    if(config.showMenu() == true && firstMenuDisplayed == false) {

                        LootSplitterOverlay lootOverlay = new LootSplitterOverlay(gpValue, quantity, itemNameStr);
                        overlayManager.add(lootOverlay);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        overlayManager.remove(lootOverlay);
                                        firstMenuDisplayed = false;
                                    }
                                },
                                (config.menuTimeout()*1000)
                        );
                        firstMenuDisplayed = true;
                    } else if(config.showMenu() && firstMenuDisplayed) {

                        LootSplitterOverlay lso = new LootSplitterOverlay(gpValue, quantity, itemNameStr){
                            public String getName() {
                                return "LootSplitTwo";
                            }
                        };

                        overlayManager.add(lso);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        overlayManager.remove(lso);
                                        secondMenuDisplayed = false;
                                    }
                                },
                                (config.menuTimeout()*1000)
                        );

                    }

                }

                //For drops with one item
                else {
                    Pattern p = Pattern.compile("\\d+|(?<=Valuable drop: )(.*)(?=\\()");
                    Matcher m = p.matcher(messageReceived);
                    int i = 0;
                    while (m.find() && i < 4) {
                        //arr[0] - name
                        //arr[1] - gp value
                        arr[i] = m.group();
                        i++;
                    }

                    final String itemName = arr[0];
                    itemNameStr = itemName;

                    final int split = (Integer.parseInt(arr[1]) / (config.numOfPlayers() + 1));
                    gpValue = split;

                    if (config.printGP()) {
                        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + "GP split for " + config.numOfPlayers() + " other player(s) is" + "<col=22ff00> " + String.format("%,d", split) + "<col=ef1020>gp", null);
                    }
                    if(config.showMenu() && !firstMenuDisplayed) {
                        LootSplitterOverlay lootOverlay = new LootSplitterOverlay(gpValue, quantity, itemNameStr);

                        overlayManager.add(lootOverlay);
                        firstMenuDisplayed = true;
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        overlayManager.remove(lootOverlay);
                                        firstMenuDisplayed = false;
                                    }
                                },
                                (config.menuTimeout()*1000)
                        );
                    } else if(config.showMenu() && firstMenuDisplayed) {
                        LootSplitterOverlay lso = new LootSplitterOverlay(gpValue, quantity, itemNameStr){
                            public String getName() {
                                return "LootSplitTwo";
                            }
                        };
                        overlayManager.add(lso);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        overlayManager.remove(lso);
                                        secondMenuDisplayed = false;
                                    }
                                },
                                (config.menuTimeout()*1000)
                        );

                    }
                }
            }




        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick)
    {
    }


    @Provides
    LootSplitterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(LootSplitterConfig.class);
    }

}
