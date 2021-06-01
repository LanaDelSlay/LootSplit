package Lana.lootsplitter;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class LootSplitterOverlay extends OverlayPanel {
    private final Client client;
    private final LootSplitterPlugin plugin;
    private final LootSplitterConfig config;

    @Inject
    private LootSplitterOverlay(Client client, LootSplitterPlugin plugin, LootSplitterConfig config, String str) {

        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);

        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }


    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().add(TitleComponent.builder().text("Loot Split:").build());
        panelComponent.getChildren().add(LineComponent.builder().left("Item Name: " + plugin.itemNameStr).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Split Value: " + String.format("%,d", plugin.gpValue)).build());
        if(plugin.quantity != 0) {
            panelComponent.getChildren().add(LineComponent.builder().left("Split Quantity: " +String.format("%,d", plugin.quantity) ).build());
        }

        return super.render(graphics);
    }
}
