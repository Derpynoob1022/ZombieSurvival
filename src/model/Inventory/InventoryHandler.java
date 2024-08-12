package model.Inventory;

import model.Handler.StateHandler.StateHandler;
import model.Items.DroppedItem;
import model.Entities.Player;
import model.Handler.MouseHandler;
import model.Items.Arrow;
import model.Items.Weapons.Ranged.IceBow;
import model.Items.Weapons.Ranged.WoodenBow;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.Helper.setup;
import static ui.GamePanel.*;

// Handles the inventory game state
public class InventoryHandler implements StateHandler {
    private static InventoryHandler inventoryHandler = new InventoryHandler();
    private Slot[] inventory;
    private Inventoriable selectedItem;
    private boolean grabbedItem = false;
    private BufferedImage hotbarBackground;
    private boolean itemPlacedOrActionTaken = false;
    private static final int MAX_STACK_SIZE = 99;

    private InventoryHandler() {
        inventory = new Slot[33];
        int x = SCREEN_WIDTH / 2 - TILESIZE * 4;
        int y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        for (int i = 0; i < 8; i++) {
            inventory[i] = new Slot(x, y, false);
            x += TILESIZE;
        }

        y -= (TILESIZE + 10);

        for (int g = 1; g < 4; g++) {
            x = SCREEN_WIDTH / 2 - TILESIZE * 4;
            for (int i = 0; i < 8; i++) {
                inventory[g * 8 + i] = new Slot(x, y, false);
                x += TILESIZE;
            }
            y -= TILESIZE;
        }

        y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        // Trash slot
        // Although probably better if I make another class that is only responsible for disposing the trash
        inventory[32] = new Slot(x, y, true);

        // Give the player a golden sword
        inventory[0].setItem(new WoodenBow());
        inventory[1].setItem(new Stack(new Arrow(), 99));
        inventory[2].setItem(new Stack(new Arrow(), 50));

        hotbarBackground = setup("background/hotbar", TILESIZE, TILESIZE);
    }

    public static InventoryHandler getInstance() {
        return inventoryHandler;
    }

    public Slot[] getInventory() {
        return inventory;
    }

    public Inventoriable getItem(int i) {
        return inventory[i].getItem();
    }

    public Inventoriable getSelectedItem() {
        return selectedItem;
    }

    public boolean getGrabbedItem() {
        return grabbedItem;
    }

    // Returns true if able to add to inventory, false otherwise
    public boolean addItem(DroppedItem droppedItem) {
        // If it is stackable, try to find an existing stack
        if (droppedItem.getItem().getIsStackable()) {
            for (int i = 0; i < inventory.length; i++) {
                Inventoriable curItem = inventory[i].getItem();
                if (curItem != null && curItem.getItem().equals(droppedItem.getItem())) {
                    if (curItem instanceof Stack) {
                        // Find a stack that's not full
                        if (((Stack) curItem).getCount() < MAX_STACK_SIZE) {
                            int leftOverCount = ((Stack) curItem).addItems(droppedItem.getCount());
                            // If stack is full but didn't pick up everything
                            if (leftOverCount != 0) {
                                droppedItem.setCount(leftOverCount);
                                // Iterate with reduced count to find another slot
                                addItem(droppedItem);
                            }
                            return true;
                        }
                    }
                }
            }
            // If you can't find an existing stack, puts it in the inventory
            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i].getItem() == null) {
                    inventory[i].setItem(new Stack(droppedItem.getItem(), droppedItem.getCount()));
                    return true;
                }
            }
        } else {
            // If it's not stackable, just find put it in the inventory
            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i].getItem() == null) {
                    inventory[i].setItem(droppedItem.getItem());
                    return true;
                }
            }
        }
        // Can't find a spot
        return false;
    }

    public void update() {
        // TODO: add functionality where you could right click to drop 1 of the stack or right click to grab one of the item out of a stack
        if (MouseHandler.getInstance().isLeftClicked()) {
            double mouseX = MouseHandler.getInstance().getX();
            double mouseY = MouseHandler.getInstance().getY();
            itemPlacedOrActionTaken = false;

            // If didn't grab an item
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
                // If is currently grabbing an item
                // TODO: add functionality where if i exit the inventory, the grabbed item gets put back to where it was picked up
                for (Slot slot : inventory) {
                    // Place item in a blank slot
                    if (slot.getHitbox().contains(mouseX, mouseY)) {
                        if (slot.getItem() == null) {
                            slot.setItem(selectedItem);
                            selectedItem = null;
                            grabbedItem = false;
                        } else {
                            // Item already exists in the slot
                            if (slot.isTrash()) {
                                // Trash
                                slot.setItem(selectedItem);
                                selectedItem = null;
                                grabbedItem = false;
                            } else {
                                // Replace item and pick up the new item
                                Inventoriable temp = slot.getItem();
                                // If it stacks with the slot it is hovered over, then it would stack together
                                if (temp instanceof Stack && selectedItem instanceof Stack && temp.getItem().equals(selectedItem.getItem())) {
                                    int selectedCount = ((Stack) selectedItem).getCount();
                                    int leftOverCount = ((Stack) temp).addItems(((Stack) selectedItem).getCount());
                                    // If the hovered stack is going to be full, make the stack full and reduce the count of the currently selected stack
                                    if (leftOverCount != 0) {
                                        ((Stack) selectedItem).removeItems(selectedCount - leftOverCount);
                                    } else {
                                        // If it stacks, then remove from grabbed item
                                        selectedItem = null;
                                        grabbedItem = false;
                                    }
                                } else {
                                    // Swaps item with the hovered slot if unstackable
                                    slot.setItem(selectedItem);
                                    selectedItem = temp;
                                }
                            }
                        }
                        itemPlacedOrActionTaken = true;
                        break;  // Exit loop after placing an item or taking an action
                    }
                }

                if (!itemPlacedOrActionTaken) {
                    // Throws the item out
                    drop(selectedItem);
                    selectedItem = null;
                    grabbedItem = false;
                }
            }
            // reset the click after registering it
            MouseHandler.getInstance().resetLeftClicked();
        }
    }

    public void draw(Graphics2D g2) {
        // Darkens the background
        g2.setColor(Color.darkGray);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));

        g2.setColor(Color.RED);

        int x = SCREEN_WIDTH / 2 - TILESIZE * 4;;
        int y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        int i; // Inventory index

        // Drawing inventory background
        // Include the trash slot
        for (i = 0; i < 9; i++) {
            g2.drawImage(hotbarBackground, x, y, null);
            x += TILESIZE;
        }

        y -= (TILESIZE + 10);

        for (int g = 0; g < 3; g++) {
            x = SCREEN_WIDTH / 2 - TILESIZE * 4;
            for (i = 0; i < 8; i++) {
                g2.drawImage(hotbarBackground, x, y, null);
                x += TILESIZE;
            }
            y -= TILESIZE;
        }

        // Reset x and y
        x = SCREEN_WIDTH / 2 - TILESIZE * 4;
        y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        // Draws the hotbar
        for (i = 0; i < 8; i++) {  // 8 columns
            drawItem(g2, Player.getInstance().getInventory()[i], x, y);
            x += TILESIZE;
        }

        // Drawing the trash slot
        drawItem(g2, Player.getInstance().getInventory()[32], x, y);

        // Highlights the trash slot
        g2.setColor(Color.RED);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.2f));
        g2.fillRect(SCREEN_WIDTH / 2 + TILESIZE * 4, SCREEN_HEIGHT / 2 + TILESIZE * 2, TILESIZE, TILESIZE);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));

        y -= (TILESIZE + 10);

        // Drawing the items
        for (int g = 1; g < 4; g++) {  // 3 rows
            x = SCREEN_WIDTH / 2 - TILESIZE * 4;  // Reset x for each row
            for (i = 0; i < 8; i++) {  // 8 columns
                drawItem(g2, Player.getInstance().getInventory()[g * 8 + i], x, y);
                x += TILESIZE;
            }
            y -= TILESIZE;
        }

        // Drawing the grabbed item
        if (InventoryHandler.getInstance().getGrabbedItem()) {
            int mouseX = (int) MouseHandler.getInstance().getX();
            int mouseY = (int) MouseHandler.getInstance().getY();
            Inventoriable item = InventoryHandler.getInstance().getSelectedItem();
            int centeringX = item.getImage().getWidth() / 2;
            int centeringY = item.getImage().getHeight() / 2;
            g2.drawImage(item.getImage(), mouseX - centeringX, mouseY - centeringY, null);

            // If the item is a stack of something, it will display the count of the stack
            if (item instanceof Stack) {
                if (((Stack) item).getCount() > 1) {
                    g2.drawString(Integer.toString(((Stack) item).getCount()), mouseX - centeringX + 20, mouseY - centeringY + 28);
                }
            }
        }
    }

    public void drop(Inventoriable item) {
        DroppedItem currentDroppedItem = new DroppedItem(item, Player.getInstance().getPosX(), Player.getInstance().getPosY());
        getDroppedItems().add(currentDroppedItem);
    }

    public Arrow getArrow() {
        for (Slot slot : inventory) {
            if (slot.getItem() instanceof Stack) {
                Stack temp = (Stack) slot.getItem();
                if (temp.getItem() instanceof Arrow) {
                    Arrow arrow = (Arrow) temp.getItem();
                    if (!temp.removeItems(1)) {
                        slot.setItem(null); // Remove the stack from the slot
                    }
                    return arrow;
                }
            }
        }
        return null;
    }

    public void reset() {
        inventory = new Slot[33];
        int x = SCREEN_WIDTH / 2 - TILESIZE * 4;
        int y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        for (int i = 0; i < 8; i++) {
            inventory[i] = new Slot(x, y, false);
            x += TILESIZE;
        }

        y -= (TILESIZE + 10);

        for (int g = 1; g < 4; g++) {
            x = SCREEN_WIDTH / 2 - TILESIZE * 4;
            for (int i = 0; i < 8; i++) {
                inventory[g * 8 + i] = new Slot(x, y, false);
                x += TILESIZE;
            }
            y -= TILESIZE;
        }

        y = SCREEN_HEIGHT / 2 + TILESIZE * 2;

        // Trash slot
        inventory[32] = new Slot(x, y, true);

        // Give the player a golden sword
        inventory[0].setItem(new IceBow());
        inventory[1].setItem(new Stack(new Arrow(), 100));
    }

    public void drawItem(Graphics2D g2, Slot slot, int x, int y) {
        if (slot != null && slot.getItem() != null) {
            BufferedImage slotImage = slot.getItem().getImage();
            // Centering the image
            int centeringX = (TILESIZE - slotImage.getWidth()) / 2;
            int centeringY = (TILESIZE - slotImage.getHeight()) / 2;
            g2.drawImage(slotImage, x + centeringX, y + centeringY, null);

            // If the item is a stack of something, it will display the count of the stack
            if (slot.getItem() instanceof Stack) {
                if (((Stack) slot.getItem()).getCount() > 1) {
                    g2.drawString(Integer.toString(((Stack)slot.getItem()).getCount()), x + TILESIZE - 20, y + TILESIZE - 20);
                }
            }
        }
    }
}
