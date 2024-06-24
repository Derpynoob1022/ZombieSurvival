package model;

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

    // TODO: implement grabbing item in inventory
    public void update() {
        if (MouseHandler.getInstance().isClicked()) {
            Double mouseX = MouseHandler.getInstance().x;
            Double mouseY = MouseHandler.getInstance().y;

            if (!grabbedItem) {
                for (Slot slot : inventory) {
                    if (slot.getHitbox().contains(mouseX, mouseY)) {
                        if (slot.getItem() != null) {
                            selectedItem = slot.getItem();
                            grabbedItem = true;
                            slot.removeItem();
                            // System.out.println("grabbed item");
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
                            // System.out.println("replaced empty slot");
                        } else if (slot.getItem() != null) {
                            Item temp = slot.getItem();
                            slot.setItem(selectedItem);
                            selectedItem = temp;
                        // System.out.println("picked up new item");
                        }
                        break;  // Exit loop after placing an item
                    }
                }
            }

            MouseHandler.getInstance().resetClicked();
        }
    }
}
