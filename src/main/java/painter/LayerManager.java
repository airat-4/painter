package painter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private File file;

    public LayerManager(int width, int height) {
        this.width = width;
        this.height = height;
        Layer layer = new Layer("Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        int maxCacheSize = 20;
        cache = new Cache<>(layer, maxCacheSize);
    }

    public LayerManager() {// TODO создание из буфера обмена

    }

    public LayerManager(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            this.file = file;
            BufferedImage image = ImageIO.read(fis);
            width = image.getWidth();
            height = image.getHeight();
            Layer layer = new Layer("Слой " + ++currentLayerId, width, height);
            layer.setImage(image);
            currentLayer = layer;
            layers.add(layer);
            int maxCacheSize = 20;
            cache = new Cache<>(layer, maxCacheSize);
        }
    }

    public void addLayer() {
        Layer layer = new Layer("Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        area = null;
    }

    public void remuveLayer() {
        layers.remove(currentLayer);
        currentLayer.setDeleted(true);
        cache.put(currentLayer);
        if (layers.size() != 0)
            currentLayer = layers.get(layers.size() - 1);
        else
            currentLayer = null;
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

    public void mergeVisibleLayer() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics graphics = image.getGraphics();
        for (Layer layer : layers) {
            if (layer.isVisible()) {
                graphics.drawImage(layer.getImage(), 0, 0, null);
            }
        }
        boolean first = true;
        for (int i = 0; i < layers.size(); i++) {
            if (first && layers.get(i).isVisible()) {
                first = false;
                layers.get(i).setImage(image);
                cache.put(layers.get(i));
                continue;
            }
            if (layers.get(i).isVisible()) {
                currentLayer = layers.get(i);
                remuveLayer();
                --i;
            }
        }
        for (Layer layer : layers) {
            if (layer.isVisible()) {
                currentLayer = layer;
                break;
            }
        }
        area = null;
    }

    public void setVisible(boolean visible) {
        currentLayer.setVisible(visible);
    }

    public void renameLayer(String name) {
        for (Layer layer : layers) {
            if (layer.getName().equals(name)) {
                return;
            }
        }
        currentLayer.setName(name);
    }


//    public boolean save(){
//        if(file == null)
//            return false;
//        try (FileOutputStream fos = new FileOutputStream(file)){
//            ImageIO.write()
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


}
