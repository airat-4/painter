package painter;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by airat on 26.11.15.
 */

public class LayerManager {
    private int currentLayerId = 0;
    private ArrayList<Layer> layers = new ArrayList<Layer>();
    private Layer currentLayer;
    private Cache<Layer> cache;
    private int width;
    private int height;
    private Rectangle area;

    public LayerManager(int width, int height) {
        this.width = width;
        this.height = height;
        Layer layer = new Layer("Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        cache = new Cache<Layer>(layer, 50);
    }

    public LayerManager() {// TODO создание из буфера обмена

    }

    public void addLayer() {
        Layer layer = new Layer("Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        cache.put(layer);
        area = null;
    }

    public void remuveLayer() {
        layers.remove(currentLayer);
        currentLayer.setDeleted(true);
        cache.put(currentLayer);
        if (layers.size() != 0)
            currentLayer = layers.get(layers.size() - 1);
        area = null;
    }

    public void upLayer() {
        for (int i = 0; i < layers.size() - 1; ++i) {
            if (currentLayer == layers.get(i)) {
                layers.remove(i);
                layers.add(i + 1, currentLayer);
                return;
            }
        }
        area = null;
    }

    public void downLayer() {
        for (int i = 1; i < layers.size(); ++i) {
            if (currentLayer == layers.get(i)) {
                layers.remove(i);
                layers.add(i - 1, currentLayer);
                return;
            }
        }
        area = null;
    }


}
