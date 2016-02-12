/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painter.instruments;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import painter.Action;
import painter.Instrument;
import painter.InstrumentCollection;
import painter.Layer;
import painter.LayerManager;
import painter.Property;
import sun.awt.datatransfer.ClipboardTransferable;
import sun.awt.datatransfer.SunClipboard;

/**
 *
 * @author airat
 */
public class Copy extends Instrument {

    public Copy(String name, boolean icon, Property... property) {
        super(name, icon, property);
    }

    @Override
    public Image getDevImage() {
        return null;
    }

    @Override
    public Instrument action(LayerManager layerManager, Action action, int x, int y) {
        if (action == Action.NONE) {
            
            Rectangle area = layerManager.getArea();
            if(area== null) return InstrumentCollection.getInstrument("Выделить");
            if (area.width > 0 && area.height > 0) {
                Image image = new BufferedImage(area.width, area.height, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics graphics = image.getGraphics();
                graphics.drawImage(((BufferedImage) layerManager.getCurrentLayer().getImage()).getSubimage(area.x, area.y, area.width, area.height), 0, 0, null);
                LayerManager.clipboard.setContents(new TransferableImage(image), null);
            }
        }
        return InstrumentCollection.getInstrument("Выделить");
    }

    private static class TransferableImage implements Transferable {

        private final Image image;

        public TransferableImage(Image image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }

}
