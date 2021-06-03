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

     int gpValue;
     int quantity;
     String itemName;
    public LootSplitterOverlay(int gpValue, int quantity, String itemName) {
        this.gpValue = gpValue;
        this.quantity = quantity;
        this.itemName = itemName;
    }


    @Inject

    public LootSplitterOverlay() {

        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
    }



    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().add(TitleComponent.builder().text("Loot Split:").build());
        panelComponent.getChildren().add(LineComponent.builder().left("Item Name: " + itemName).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Split Value: " + String.format("%,d", gpValue)).build());
        if(quantity != 0) {
            panelComponent.getChildren().add(LineComponent.builder().left("Split Quantity: " +String.format("%,d", quantity) ).build());
        }

        return super.render(graphics);
    }
}
