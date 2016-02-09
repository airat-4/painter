package painter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import painter.instruments.*;

/**
 * Created by airat on 28.11.15.
 */
public class InstrumentCollection {
    private static final ArrayList<Instrument> instruments = new ArrayList<>();

    static {
        //---------   Перечень инструмнтов   ----------
        Property property = new Property(PropertyType.SLIDER, "Толщина кисти", 10, 100);
        Property property1 = new Property(PropertyType.COLOR, "Цвет кисти   ", Color.BLACK);
        add(new Pencil("Карандаш", true, property, property1));
        
        property = new Property(PropertyType.SLIDER, "Толщина линии", 10, 100);
        property1 = new Property(PropertyType.COLOR, "Цвет линии   ", Color.BLACK);
        add(new Line("Линия", true, property, property1));
        
        add(new Select("Выделить", true));
        
        add(new Copy("Копировать", false));
        
        add(new  Past("Вставить", false));
        
        add(new  Cut("Вырезать", false));
    }

    private InstrumentCollection() {
    }

    public static Collection<Instrument> getInstrumentCollection() {
        return instruments;
    }

    public static boolean add(Instrument instrument) {
        if (getInstrument(instrument.getName()) != null)
            return false;
        instruments.add(instrument);
        return true;
    }

    public static Instrument getInstrument(String name) {
        for (Instrument instrument : instruments) {
            if (instrument.getName().equals(name)) {
                return instrument;
            }
        }
        return null;
    }

}
