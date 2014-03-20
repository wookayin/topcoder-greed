package greed.code;

import greed.model.Argument;
import greed.model.Method;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

import java.util.List;

import com.floreysoft.jmte.NamedRenderer;

/**
 * Greed is good! Cheers!
 */
public interface LanguageRenderer {
    public String renderPrimitive(Primitive primitive);

    public String renderType(Type type);

    public String renderParam(Param param);

    public String renderParamValue(ParamValue paramValue);

    public String renderParamList(Param[] params);

    public String renderArgument(Argument argument);

    public String renderZeroValue(Type type);

    public String renderMethod(Method method);

    public List<NamedRenderer> getOtherRenderers();
}
