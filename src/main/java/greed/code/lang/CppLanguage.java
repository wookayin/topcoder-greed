package greed.code.lang;

import greed.code.LanguageRenderer;
import greed.model.Argument;
import greed.model.Language;
import greed.model.Primitive;
import greed.model.Type;
import greed.util.Utils;

/**
 * Greed is good! Cheers!
 */
public class CppLanguage extends CStyleLanguage implements LanguageRenderer {
    public static final CppLanguage instance = new CppLanguage();

    protected CppLanguage() {
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
                // TODO: Bad practice, need decoupling
                return Utils.getGreedConfig().getLanguage().get(Language.getName(Language.CPP)).getLongIntTypeName();
        }
        return "";
    }

    @Override
    public String renderType(Type type) {
        String typeName = renderPrimitive(type.getPrimitive());
        if (type.isArray())
            typeName = "vector<" + typeName + ">";
        return typeName;
    }

    @Override
    protected String renderParamValuePrimitive(Argument arg) {
        // TODO use equals (implement it first)
        if (arg.getType().getPrimitive() == Primitive.LONG)
            return arg.getValue() + "LL";
        return super.renderParamValuePrimitive(arg);
    }

}
