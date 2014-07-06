package greed.model;

/**
 * The model for parameter. (also used for return value)
 */
public class Param {
    private final String name;
    private final Type type;
    private final int index;

    public Param(String name, Type type, int index) {
        if(name == null) throw new NullPointerException("name");
        if(type == null) throw new NullPointerException("type");

        this.name = name;
        this.type = type;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
