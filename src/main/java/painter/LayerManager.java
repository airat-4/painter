package painter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import painter.instruments.Pencil;

/**
 * Created by airat on 26.11.15.
 */
public class LayerManager {
    public static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private int currentLayerId = 0;
    private ArrayList<Layer> layers = new ArrayList<Layer>();
    private Layer currentLayer;
    private Cache<Layer> cache;
    private int width;
    private int height;
    private Rectangle area;
    private File file;
    private Instrument currentInstrument = InstrumentCollection.getInstrument("Карандаш");
    private int MAX_CACHE_SIZE = 20;
    
    public LayerManager(int width, int height) {
        currentInstrument.flush(this);
        this.width = width;
        this.height = height;
        Layer layer = new Layer(this, "Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        cache = new Cache<>(layer, MAX_CACHE_SIZE);
    }

    public LayerManager() throws UnsupportedFlavorException, IOException {
        currentInstrument.flush(this);
        Transferable t = clipboard.getContents(null);
        if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            Image image = (Image) t.getTransferData(DataFlavor.imageFlavor);
            width = image.getWidth(null);
            height = image.getHeight(null);
            Layer layer = new Layer(this, "Слой " + ++currentLayerId, width, height);
            currentLayer = layer;
            layers.add(layer);
            cache = new Cache<>(layer, MAX_CACHE_SIZE);
            layer.setImage(image);
        } else {
            throw new IOException("буффер обмена пуст");
        }
    }

    public LayerManager(File file) throws IOException {
        currentInstrument.flush(this);
        try (FileInputStream fis = new FileInputStream(file)) {
            this.file = file;
            BufferedImage image = ImageIO.read(fis);
            width = image.getWidth();
            height = image.getHeight();
            Layer layer = new Layer(this, "Слой " + ++currentLayerId, width, height);

            currentLayer = layer;
            layers.add(layer);
            cache = new Cache<>(layer, MAX_CACHE_SIZE);
            layer.setImage(image);
        }
    }

    public void addLayer() {
        flush();
        Layer layer = new Layer(this, "Слой " + ++currentLayerId, width, height);
        currentLayer = layer;
        layers.add(layer);
        area = null;
    }

    public Instrument getCurrentInstrument() {
        return currentInstrument;
    }

    public void removeLayer() {
        flush();
        layers.remove(currentLayer);
        currentLayer.setDeleted(true);
        if (layers.size() != 0) {
            currentLayer = layers.get(layers.size() - 1);
        } else {
            currentLayer = null;
        }
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
        flush();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics graphics = image.getGraphics();
        flush();
        for (Layer layer : layers) {
            if (layer.isVisible()) {
                graphics.drawImage(layer.getImage(), 0, 0, null);
            }
        }
        boolean first = true;
        for (int i = 0; i < layers.size();) {
            if (first && layers.get(i).isVisible()) {
                first = false;
                layers.get(i).setImage(image);
                i++;
                continue;
            }
            if (layers.get(i).isVisible()) {
                currentLayer = layers.get(i);
                removeLayer();

            } else {
                i++;
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

    public void setVisible(String layerName, boolean visible) {
        for (Layer layer : layers) {
            if (layer.getName().equals(layerName)) {
                layer.setVisible(visible);
                return;
            }
        }
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
        if(currentLayer == null)
            return ;
        Image instrumentImage = currentInstrument.flush(this);
        if (instrumentImage != null) {
            Image image = currentLayer.getImage();
            BufferedImage newImage
                    = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.drawImage(instrumentImage, 0, 0, null);
            currentLayer.setImage(newImage);
        }
    }

    public Image getImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics graphics = image.getGraphics();
        for (Layer layer : layers) {
            if (layer.isVisible()) {
                graphics.drawImage(layer.getImage(), 0, 0, null);
                if (layer.equals(currentLayer)) {
                    Image img = currentInstrument.getDevImage();
                    if (img != null) {
                        graphics.drawImage(img, 0, 0, null);
                    }
                }
            }

        }
        return image;
    }

    public void action(Action action, int x, int y) {
        Instrument instrument = currentInstrument;
        do {
            instrument = instrument.action(this, action, x, y);
            if (instrument != null) {
                flush();
                currentInstrument = instrument;
            }
        } while (instrument != null);
    }

    void putInCache(Layer layer) {
        cache.put(layer);
    }

    public void undo() {
        flush();
        int indexForInsert = layers.size();
        Layer prevLayer = cache.getPrev();
        if(prevLayer.isDeleted()) {
            prevLayer.setDeleted(false);
            layers.add(indexForInsert, prevLayer);
            prevLayer = cache.getPrev();
        }else{
            prevLayer.undo();
        }
    }

    public void redo() {
        Layer nextLayer = cache.getNext();
        nextLayer.redo();
    }

    public boolean saveAs(File file) {
        file = new File(file.getPath() + ".png");
        System.out.println(file.getPath() + ".png");

        flush();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String format = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
            ImageIO.write((RenderedImage) getImage(), format, fos);

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

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public Layer getCurrentLayer() {
        return currentLayer;
    }

    public void setCurrentLayer(String currentLayer) {
        flush();
        for (Layer layer : layers) {
            if (layer.getName().equals(currentLayer)) {
                this.currentLayer = layer;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getArea() {
        return area;
    }

  

    
}
