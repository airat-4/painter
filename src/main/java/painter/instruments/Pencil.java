/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painter.instruments;

import java.awt.Image;
import painter.Action;
import painter.Instrument;
import painter.LayerManager;
import painter.Property;

/**
 *
 * @author airat
 */
public class Pencil extends Instrument{

    public Pencil(String name, boolean icon, Property... property) {
        super(name, icon, property);
    }

    @Override
    public Image getDevImage() {
        return null;
    }

    @Override
    public Instrument action(LayerManager layerManager, Action action, int x, int y) {
        return null;
    }
    
}