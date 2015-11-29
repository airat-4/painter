package painter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private Instrument currentInstrument;
    private int MAX_CACHE_SIZE = 20;
    public LayerManager(int width, int height) {
        this.width = width;
        this.height = height;
        Layer layer = new Layer(this, "Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        cache = new Cache<>(layer, MAX_CACHE_SIZE);
    }

    public LayerManager() {// TODO создание из буфера обмена

    }

    public LayerManager(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            this.file = file;
            BufferedImage image = ImageIO.read(fis);
            width = image.getWidth();
            height = image.getHeight();
            Layer layer = new Layer(this, "Слой " + ++currentLayerId, width, height);
            layer.setImage(image);
            currentLayer = layer;
            layers.add(layer);
            cache = new Cache<>(layer, MAX_CACHE_SIZE);
        }
    }

    public void addLayer() {
        Layer layer = new Layer(this, "Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        area = null;
    }

    public void removeLayer() {
        layers.remove(currentLayer);
        currentLayer.setDeleted(true);
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
        flush();
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
                continue;
            }
            if (layers.get(i).isVisible()) {
                currentLayer = layers.get(i);
                removeLayer();
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

    public boolean renameLayer(String name) {
        for (Layer layer : layers) {
            if (layer.getName().equals(name)) {
                return false;
            }
        }
        currentLayer.setName(name);
        return true;
    }

    public void setInstrument(String name) {
        flush();
        currentInstrument = InstrumentCollection.getInstrument(name);
    }

    private void flush() {
        Image InstrumentImage = currentInstrument.flush(this);
        if (InstrumentImage != null) {
            Image image = currentLayer.getImage();
            BufferedImage newImage =
                    new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            currentLayer.setImage(newImage);
        }
    }

    public Image getImage() {
        final int INDENT = 10;
        BufferedImage image = new BufferedImage(width + INDENT, height + INDENT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(width + 1, 0, INDENT, height + INDENT);
        graphics.fillRect(0, height + 1, width + INDENT, INDENT);
        graphics.setColor(Color.black);
        graphics.draw3DRect(-1, -1, width + 1, height + 1, true);
        graphics.drawImage(getRealImage(), 0, 0, null);
        return image;
    }

    private Image getRealImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics graphics = image.getGraphics();
        for (Layer layer : layers) {
            graphics.drawImage(layer.getImage(), 0, 0, null);
            if (layer.equals(currentLayer)) {
                graphics.drawImage(currentInstrument.getDevImage(), 0, 0, null);
            }
        }
        return image;
    }

    public void action(Action action, int x, int y) {
        currentInstrument.action(this, action, x, y);
    }

    void putInCache(Layer layer) {
        cache.put(layer);
    }

    public void undo() {
        flush();
        int indexForInsert = layers.size() - 1;
        Layer prevLayer = cache.getPrev();
        while (prevLayer.isDeleted()) {
            prevLayer.setDeleted(false);
            layers.add(indexForInsert, prevLayer);
            prevLayer = cache.getPrev();
        }
        prevLayer.undo();
    }

    public void redo() {
        Layer nextLayer = cache.getNext();
        nextLayer.redo();
    }

    public boolean saveAs(File file) {
        if (file.getName().indexOf('.') == -1) {
            file = new File(file.getPath() + File.pathSeparator + ".png");
            System.out.println(file.getPath() + File.pathSeparator + ".png");
        }
        flush();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String format = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
            ImageIO.write((RenderedImage) getRealImage(), format, fos);

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean save() {
        if (file == null) {
            return false;
        }
        return saveAs(file);
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }

}
