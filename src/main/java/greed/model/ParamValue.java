package greed.model;

/**
 * The model containing the parameter value, as well as the parameter.
 *
 * <p>
 * The parameter values contained, represented as the properties
 * <code>value</code> or <code>valueList</code> should be in the canonical form.
 * That is, they are independent of language: no suffixes for long type
 * integers, or no quotations for string. Therefore, the renderers used in
 * the <i>source code</i> templates should respect it and emit the correct
 * tokens that can be interpreted by the language (e.g. do string quotation
 * or append long-type suffixes)
 *
 * <p>
 * Note: Prior to version 2.0, they were bounded with the current language.
 * (e.g. "0LL" in C++, "0L" in Java / all strings were with quotation mark)
 *
 * @author Shiva Wu
 * @author vexorian
 * @author Jongwook Choi (wook)
 *
 */
public class ParamValue {
    private Param param;
    private String value;
    private String[] valueList;

    /**
     * construct
     * @param param
     * @param value the value of parameter, in the canonical representation.
     */
    public ParamValue(Param param, String value) {
        this.param = param;
        this.value = value;
        this.valueList = new String[]{value};
    }

    public ParamValue(Param param, String[] valueList) {
        this.param = param;
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        String sep = "";
        for (String s : valueList) {
            buf.append(sep);
            sep = ", ";
            buf.append(s);
        }
        buf.append(" }");
        this.value = buf.toString();
        this.valueList = valueList;
    }

    public Param getParam() {
        return param;
    }

    public String getValue() {
        return value;
    }

    public String[] getValueList() {
        return valueList;
    }

    public int getValueListLength() {
        return valueList.length;
    }

    public boolean isMultiLine() {
        return valueList.length > 1;
    }
}
