package painter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by airat on 28.11.15.
 */
public abstract class Instrument {
    protected BufferedImage realImage;
    protected Properties properties;

    public Instrument(Property... property) {
        this.properties = new Properties(property);
    }

    public Image flush() {
        Image image = realImage;
        realImage = null;
        return image;
    }

    public abstract Image getDevImage();

    public Properties getProperties() {
        return properties;
    }

    public abstract Instrument action(LayerManager layerManager, Action action, int x, int y);
}
