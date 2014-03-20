package greed.code.lang;

import greed.code.LanguageRenderer;
import greed.code.LanguageTrait;
import greed.model.Argument;
import greed.model.Method;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;
import greed.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

/**
 * An base implementation of {@link LanguageRenderer} and {@link LanguageTrait}.
 * <p>
 * This implementation expects to support traits that are common to almost all
 * languages, so that each language's trait implementation can be benefited by
 * extending this class.
 *
 * @since 2.0
 *
 * @author Jongwook Choi
 * @author Shiva Wu
 */
public abstract class AbstractLanguage implements LanguageTrait, LanguageRenderer {

    @Override
    public ParamValue parseValue(String value, Param param) {
        Type type = param.getType();
        value = value.trim();

        // TODO throw exception if malformed or validation fails
        if (!type.isArray()) {
            // do some required canonization
            if(type.getPrimitive() == Primitive.STRING) {
                if(value.length() >= 2 && value.charAt(0) == '"' &&
                        value.charAt(value.length() - 1) == '"') {
                    // drops quotation
                    value = value.substring(1, value.length() - 1);
                }
                return new ParamValue(param, value);
            }
            else {
                return new ParamValue(param, value);
            }
        }

        // assuming the parameter is an array, surrounded with '{' and '}',
        // parse the input string into an array of elements.
        value = value.substring(1, value.length() - 1); // drops '{', '}'
        value = value.replaceAll("\n", "");
        value = value.trim(); //need a second trim in case it is an empty list {  }

        if (type.getPrimitive() == Primitive.STRING) {
            boolean inString = false;
            ArrayList<String> valueList = new ArrayList<String>();
            StringBuilder buf = new StringBuilder();
            // TODO: escape \" in string
            for (int i = 0; i < value.length(); ++i) {
                char c = value.charAt(i);
                if (c == '"') {
                    if (inString) {
                        valueList.add(buf.toString());
                    } else {
                        buf.setLength(0);
                    }
                    inString = !inString;
                } else if (inString) {
                    buf.append(c);
                }
            }

            return new ParamValue(param, valueList.toArray(new String[0]));
        } else if (value.length() == 0) {
            // Empty array
            return new ParamValue( param, new String[]{} );
        } else {
            String[] valueList = value.split(",");
            for (int i = 0; i < valueList.length; i++) {
                valueList[i] = valueList[i].trim();
            }
            return new ParamValue(param, valueList);
        }
    }

    @Override
    public String renderMethod(Method method) {
        return renderType(method.getReturnType()) + " " + method.getName() + "(" + renderParamList(method.getParams()) + ")";
    }

    @Override
    public String renderParamList(Param[] params) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) buf.append(", ");
            buf.append(renderParam(params[i]));
        }
        return buf.toString();
    }

    @Override
    public String renderParamValue(ParamValue paramValue) {
        Type paramType = paramValue.getParam().getType();

        if (paramType.isArray()) {
            // TODO extract the list itself from Argument
            return renderParamValueArray(paramValue.getArgumentList());
        }
        else {
            return renderParamValuePrimitive(paramValue.getArgument());
        }
    }

    /**
     * A base implementation for rendering an param value of array type
     * into the string that can be properly interpreted by the language.
     * <p>
     * The default implementation is joining all the elements, rendered with
     * {@link #renderParamValuePrimitive(Type, String)}.
     *
     * TODO refactor by exploiting a polymorphism of ParamValue.
     */
    protected String renderParamValueArray(Argument[] args) {
        String[] renderedElements = new String[args.length];
        for(int i = 0; i < args.length; i++)
            renderedElements[i] = renderParamValuePrimitive(args[i]);

        return "{" + StringUtil.join(renderedElements, ", ") + "}";
    }

    /**
     * A template method for rendering a primitive param value into the string
     * that can be properly interpreted by the language. Expected to be overriden.
     * For instance, long-type integers in C++ should be followed by the 'LL' suffix.
     * <p>
     * The default implementation is returning the value with the common quotation
     * for strings, or the canonical form as-is for else.
     * (i.e. {@code value})
     */
    protected String renderParamValuePrimitive(Argument arg) {
        if(arg.getType().isString())
            return '"' + arg.getValue() + '"';
        else
            return arg.getValue();
    }

    @Override
    public String renderArgument(Argument arg) {
        return renderParamValuePrimitive(arg);
    }

    @Override
    public List<NamedRenderer> getOtherRenderers() {
        ArrayList<NamedRenderer> namedRenderers = new ArrayList<NamedRenderer>();
        namedRenderers.add(new NamedRenderer() {
            @Override
            public String render(Object o, String s, Locale locale) {
                if (o instanceof Type)
                    return renderZeroValue((Type) o);
                return "";
            }

            @Override
            public String getName() {
                return "zeroval";
            }

            @Override
            public RenderFormatInfo getFormatInfo() {
                return null;
            }

            @Override
            public Class<?>[] getSupportedClasses() {
                return new Class<?>[]{Type.class};
            }
        });
        return namedRenderers;
    }

}
