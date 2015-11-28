package painter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by airat on 26.11.15.
 */
public class Layer {
    private static final BufferedImage NOT_VISIBLE_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    private String name;
    private boolean visible = true;
    private Cache<Image> cache;
    private boolean deleted;

    public Layer(String name, int width, int height) {
        this.name = name;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        int maxCacheSize = 10;
        cache = new Cache<Image>(image, maxCacheSize);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return visible ? cache.get() : NOT_VISIBLE_IMAGE;
    }

    public void setImage(Image image) {
        cache.put(image);
    }

    public void undo() {
        cache.getPrev();
    }

    public void redo() {
        cache.getNext();
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object obj) {
        Layer layer = (Layer) obj;
        return name.equals(layer.name);
    }
}
