package model.Collision;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Quadtree {
    // Setting the max object per subdivided region and max iterations
    private static final int MAX_OBJECTS = 8;
    private static final int MAX_LEVELS = 5;

    private int level;
    private List<Collidable> objects;
    private Rectangle bounds;
    private Quadtree[] nodes;

    public Quadtree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;
        this.objects = new ArrayList<>();
        this.nodes = new Quadtree[4];
    }

    // Clears the tree
    public void clear() {
        objects.clear();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    // If the collidables in one region exceeds the max, then subdivide it once more and add 1 to the "depth" or level
    private void split() {
        int subWidth = bounds.width / 2;
        int subHeight = bounds.height / 2;
        int x = bounds.x;
        int y = bounds.y;

        nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    // Finds which quadrants the collidable should go to
    private int getIndex(Rectangle pRect) {
        int index = -1;
        double verticalMidpoint = bounds.x + (bounds.width / 2);
        double horizontalMidpoint = bounds.y + (bounds.height / 2);

        boolean topQuadrant = (pRect.y < horizontalMidpoint && pRect.y + pRect.height < horizontalMidpoint);
        boolean bottomQuadrant = (pRect.y > horizontalMidpoint);

        if (pRect.x < verticalMidpoint && pRect.x + pRect.width < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (pRect.x > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    // Inserts the collidable into the tree
    public void insert(Collidable collidable) {
        // If it has a child, then pass the collidable to the child
        if (nodes[0] != null) {
            int index = getIndex(collidable.getBounds());

            if (index != -1) {
                nodes[index].insert(collidable);
                return;
            }
        }

        // If it doesn't have child, add to current
        objects.add(collidable);

        // System.out.println("Inserted " + collidable + " into level " + level + " at bounds " + bounds);

        // If the current objects in the region exceeds the max, subdivide into 4 more regions inside
        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            // Relay all the objects in the current region and pass it onto the correct node, the clears the list of objects
            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i).getBounds());
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    // Returns the possible collisions by filling in the list passed into this method
    public List<Collidable> retrieve(List<Collidable> returnObjects, Collidable collidable) {
        int index = getIndex(collidable.getBounds());
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, collidable);
        }

        returnObjects.addAll(objects);

        return returnObjects;
    }
}
