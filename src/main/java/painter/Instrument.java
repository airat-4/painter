package painter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by airat on 28.11.15.
 */
public abstract class Instrument {
    protected BufferedImage realImage;
    protected Properties properties;
    private String name;

    public Instrument(String name, Property... property) {
        this.name = name;
        this.properties = new Properties(property);
    }

    public Image flush() {
        Image image = realImage;
        realImage = null;
        return image;
    }

    public abstract Image getDevImage();

    public final Properties getProperties() {
        return properties;
    }

    public abstract Instrument action(LayerManager layerManager, Action action, int x, int y);

    public final String getName() {
        return name;
    }

    public Icon getIcon() {
        return new ImageIcon(getClass().getResource(name + ".png"));
    }

    @Override
    public boolean equals(Object obj) {
        Instrument instrument = (Instrument) obj;
        return instrument.name.equals(name);
    }
}
