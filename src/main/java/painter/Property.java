package painter;

/**
 * Created by airat on 28.11.15.
 */
public class Property {
    public final PropertyType type;
    public final String name;
    public final Object otherSettings;
    public Object value;

    public Property(PropertyType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
        otherSettings = null;
    }

    public Property(PropertyType type, String name, Object value, Object otherSettings) {
        this.value = value;
        this.type = type;
        this.name = name;
        this.otherSettings = otherSettings;
    }
}
