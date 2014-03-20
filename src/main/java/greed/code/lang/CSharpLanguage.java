package greed.code.lang;

import greed.model.Argument;
import greed.model.Primitive;
import greed.model.Type;

/**
 * Greed is good! Cheers!
 */
public class CSharpLanguage extends CStyleLanguage {
    public static final CSharpLanguage instance = new CSharpLanguage();

    protected CSharpLanguage() {
        super();
    }

    @Override
    public String renderPrimitive(Primitive primitive) {
        switch (primitive) {
            case STRING:
                return "string";
            case DOUBLE:
                return "double";
            case INT:
                return "int";
            case BOOL:
                return "bool";
            case LONG:
                return "long";
        }
        return "";
    }

    @Override
    public String renderType(Type type) {
        String typeName = renderPrimitive(type.getPrimitive());
        if (type.isArray()) typeName += "[]";
        return typeName;
    }

    @Override
    protected String renderParamValuePrimitive(Argument arg) {
        // TODO use equals (implement it first)
        if (arg.getType().getPrimitive() == Primitive.LONG)
            return arg.getValue() + "L";
        return super.renderParamValuePrimitive(arg);
    }

    @Override
    public String renderZeroValue(Type type) {
        if (type.isArray()) return "new " + renderPrimitive(type.getPrimitive()) + "[] { }";
        switch (type.getPrimitive()) {
            case BOOL:
                return "false";
            case STRING:
                return "\"\"";
            case INT:
            case LONG:
                return "0";
            case DOUBLE:
                return "0.0";
        }
        return "";
    }
}
