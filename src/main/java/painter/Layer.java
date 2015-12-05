package painter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by airat on 26.11.15.
 */
public class Layer {
    private String name;
    private boolean visible = true;
    private Cache<Image> cache;
    private boolean deleted;
    private LayerManager layerManager;

    public Layer(LayerManager layerManager, String name, int width, int height) {
        this.layerManager = layerManager;
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
        return cache.get();
    }

    public void setImage(Image image) {
        layerManager.putInCache(this);
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
        if (deleted)
            layerManager.putInCache(this);
        this.deleted = deleted;
    }
    
    
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        Layer layer = (Layer) obj;
        return name.equals(layer.name);
    }
}
