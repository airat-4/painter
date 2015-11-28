package painter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by airat on 28.11.15.
 */
public class InstrumentCollection {
    private static final ArrayList<Instrument> instruments = new ArrayList<>();

    static {
        //---------   Перечень инструмнтов   ----------

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
