package painter;

/**
 * Created by airat on 28.11.15.
 */
public class Properties {
    private Property[] properties;

    public Properties(Property... properties) {
        this.properties = properties;
    }

    public Property[] getProperties() {
        return properties;
    }

    public void setPropertyValue(String name, Object value) {
        for (Property property : properties) {
            if (property.name.equals(name)) {
                property.value = value;
            }
        }

    }
}
