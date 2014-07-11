package greed.model;

/**
 * The model containing the actual argument value.
 * (especially binded with parameters)
 * <p>
 * Note: The value <strong>SHOULD BE</strong> of a canonical form. For example,
 * no quotation for string type arguments,
 * no LL-suffixes for long type arguments,
 * and etc.
 *
 * @author Jongwook Choi (wook)
 * @since 2.0
 *
 * @see ParamValue
 *
 */
public class Argument {

    private final Type type;
    private final String value;

    public Argument(Type type, String value) {
        if(type == null) throw new NullPointerException("type");
        if(value == null) throw new NullPointerException("value");

        this.type = type;
        this.value = value;
    }

    public static Argument ofInt(int v) {
        return new Argument(Type.INT_TYPE, Integer.toString(v));
    }
    public static Argument ofLong(long v) {
        return new Argument(Type.LONG_TYPE, Long.toString(v));
    }
    public static Argument ofDouble(double v) {
        return new Argument(Type.DOUBLE_TYPE, Double.toString(v));
    }
    public static Argument ofBoolean(boolean v) {
        return new Argument(Type.BOOL_TYPE, v ? "true" : "false");
    }
    public static Argument ofString(String v) {
        return new Argument(Type.STRING_TYPE, v);
    }

    public Type getType() {
        return type;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if(Type.STRING_TYPE.equals(type)) {
            // TODO I don't think this is correct. But the value is quoted once,
            // because by doing so all the previous templates that depend on
            // ${pv.valueList} or ${pv.value}, although deprecated.
            // By introducing breaking changes on templates, we can remove it.
            return '"' + getValue() + '"';
        }
        else {
            return getValue();
        }
    }

    @Override
	public boolean equals(Object o) {
        if(!(o instanceof Argument)) return false;

        Argument rhs = (Argument) o;
        if(rhs == this) return true;

        return this.type == rhs.type
               && this.value.equals(rhs.value);
    }

    @Override
    public int hashCode() {
        int h = type.hashCode();
        h = h * 31 + value.hashCode();
        return h;
    }

}
