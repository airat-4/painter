package painter;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by airat on 29.11.15.
 */
public class Controller {
    private static Controller controller = new Controller();
    private LayerManager layerManager;
    private int scale = 100;

    private Controller() {
    }

    public static Controller getInstance() {
        return controller;
    }

    public int coordinateScale(int x) {
        return x * scale / 100;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void create(int width, int height) {
        layerManager = new LayerManager(width, height);
    }

    public boolean open(File file) {
        try {
            layerManager = new LayerManager(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean pastOfBuffer() {// TODO
        layerManager = new LayerManager();
        return false;
    }


    public void addLayer() {
        layerManager.addLayer();
    }

    public void undo() {
        layerManager.undo();
    }

    public boolean saveAs(File file) {
        return layerManager.saveAs(file);
    }

    public void mergeVisibleLayer() {
        layerManager.mergeVisibleLayer();
    }

    public Image getImage() {
        Image image = layerManager.getImage();
        Image scaledImage = image.getScaledInstance(
                coordinateScale(image.getWidth(null)),
                coordinateScale(image.getHeight(null)),
                Image.SCALE_SMOOTH);
        return scaledImage;
    }

    public void redo() {
        layerManager.redo();
    }

    public void setVisible(boolean visible) {
        layerManager.setVisible(visible);
    }

    public void removeLayer() {
        layerManager.removeLayer();
    }

    public boolean renameLayer(String name) {
        return layerManager.renameLayer(name);
    }

    public void upLayer() {
        layerManager.upLayer();
    }

    public void setInstrument(String name) {
        layerManager.setInstrument(name);
    }

    public void downLayer() {
        layerManager.downLayer();
    }

    public boolean save() {
        return layerManager.save();
    }


}