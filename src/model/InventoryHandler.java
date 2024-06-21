package model;

import static ui.GamePanel.*;

public class InventoryHandler {
    private static InventoryHandler inventoryHandler = new InventoryHandler();
    private Slot[] inventory;
    private Slot selectedSlot;
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

    public void addItem(int i, Item item) {
        inventory[i].setItem(item);
    }

    // TODO: implement grabbing item in inventory
    public void update() {
        if (MouseHandler.getInstance().isClicked()) {
            Double mouseX = MouseHandler.getInstance().x;
            Double mouseY = MouseHandler.getInstance().y;
            if (!grabbedItem) {
                for (Slot i : inventory) {
                    if (i.getHitbox().contains(mouseX, mouseY)) {
                        selectedSlot = i;
                        grabbedItem = true;
                    }
                }
            } else {
                for (Slot i : inventory) {
                    if (i.getHitbox().contains(mouseX, mouseY)) {
                        if (i.getItem() == null) {

                        }
                    }
                }
            }
        }
    }
}
