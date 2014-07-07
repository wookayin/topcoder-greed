package greed.model;

import java.util.Arrays;
import java.util.Iterator;

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
 * @see Param
 * @see Argument
 *
 * @author Shiva Wu
 * @author vexorian
 * @author Jongwook Choi (wook)
 *
 */
public class ParamValue implements Iterable<Argument> {
    private Param param;
    private Argument value;

    /** @deprecated going to be replaced with {@link value} */
    @Deprecated
    private Argument[] valueList;

    /**
     * @param param
     * @param value the value of parameter, in the canonical representation.
     */
    public ParamValue(Param param, String value) {
        this.param = param;
        this.value = new Argument(param.getType(), value);
        this.valueList = new Argument[]{ this.value };
    }

    public ParamValue(Param param, String[] valueList) {
        this.param = param;

        // TODO how to handle this? (consider plymorphism)
        this.valueList = new Argument[valueList.length];

        Type elemType = Type.primitiveType(param.getType().getPrimitive());
        for(int i = 0; i < valueList.length; ++ i)
            this.valueList[i] = new Argument(elemType, valueList[i]);

        this.value = new Argument(param.getType(),
                Arrays.asList(this.valueList).toString());
    }

    public Param getParam() {
        return param;
    }

    public Argument getArgument() {
        return value;
    }

    // TODO in beta: can be removed
    public Argument[] getArgumentList() {
        return this.valueList;
    }

    @Override
    public Iterator<Argument> iterator() {
        // make ParamValue itself iterable,
        // so that it can be used with 'foreach' statement in the template
        return Arrays.asList(this.valueList).iterator();
    }

    // TODO in beta: can be removed
    public String[] getValueStringList() {
        int n = this.valueList.length;
        String[] ret = new String[n];
        for(int i = 0; i < n; ++ i)
            ret[i] = this.valueList[i].toString();
        return ret;
    }

    /** @deprecated in favor of {@link #getArgument()} */
    @Deprecated
    public String getValue() {
        return value.toString();
    }

    @Deprecated
    public Argument[] getValueList() {
        return this.valueList;
    }

    public int getValueListLength() {
        return valueList.length;
    }

    public boolean isMultiLine() {
        return valueList.length > 1;
    }


    @Override
    public String toString() {
        return "{" + param + " : " + value.toString() + "}";
    }

}
