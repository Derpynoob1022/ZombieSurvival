package model.Handler;

import model.Items.GoldenSword;
import model.Items.Item;
import model.Slot;

import static ui.GamePanel.*;

public class InventoryHandler {
    private static InventoryHandler inventoryHandler = new InventoryHandler();
    private Slot[] inventory;
    private Item selectedItem;
    private boolean grabbedItem = false;

    private InventoryHandler() {
        inventory = new Slot[32];
        int x;
        int y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        for (int g = 0; g < 4; g++) {
            x = SCREEN_WIDTH / 2 - TILESIZE * 4;
            for (int i = 0; i < 8; i++) {
                inventory[g * 8 + i] = new Slot(x, y);
                x += TILESIZE;
            }
            y -= TILESIZE;
        }
        inventory[0].setItem(new GoldenSword());
    }

    public static InventoryHandler getInstance() {
        return inventoryHandler;
    }

    public Slot[] getInventory() {
        return inventory;
    }

    public Item getItem(int i) {
        return inventory[i].getItem();
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public boolean getGrabbedItem() {
        return grabbedItem;
    }

    public void addItem(int i, Item item) {
        inventory[i].setItem(item);
    }

    public void update() {
        if (MouseHandler.getInstance().isClicked()) {
            double mouseX = MouseHandler.getInstance().getX();
            double mouseY = MouseHandler.getInstance().getY();

            if (!grabbedItem) {
                for (Slot slot : inventory) {
                    if (slot.getHitbox().contains(mouseX, mouseY)) {
                        if (slot.getItem() != null) {
                            selectedItem = slot.getItem();
                            grabbedItem = true;
                            slot.removeItem();
                            break;  // Exit loop after grabbing an item
                        }
                    }
                }
            } else {
                for (Slot slot : inventory) {
                    if (slot.getHitbox().contains(mouseX, mouseY)) {
                        if (slot.getItem() == null) {
                            slot.setItem(selectedItem);
                            selectedItem = null;
                            grabbedItem = false;
                        } else {
                            Item temp = slot.getItem();
                            slot.setItem(selectedItem);
                            selectedItem = temp;
                        }
                        break;  // Exit loop after placing an item
                    }
                }
            }

            MouseHandler.getInstance().resetClicked();
        }
    }
}
