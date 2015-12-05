package painter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import painter.instruments.Pencil;

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
