package greed.template;

import greed.model.Param;
import greed.model.ParamValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

/**
 * A <i>named</i> renderer for {@link ParamValue}, addressed by the name 'tc'.
 * <p>
 * This class is primarily designed for rendering parameter values in the
 * TopCoder-style, for the test cases sample file to provide much more readability.
 * For example, the template
 * <pre>
 * // Assume (example.Input instanceof ParamValue)
 * ${example.Input;tc}
 * ${example.Output;tc(grid)}
 * </pre>
 * may be rendered into the following form:
 * <pre>
 * { 1, 2, 3 }
 * {"YYN",
 *  "NNN",
 *  "NYN"}
 * </pre>
 * The list of available parameters (options or filters) are:
 * <ul>
 * <li> sep : the separator. By default, a single whitespace is used.
 * <li> grid : Renders an array of string, in the grid-style, if possible.
 *      (see also the grid part of {@link HTMLRenderer})
 * </ul>
 *
 * Refer to its unit test for the more detailed usage and the result.
 *
 * <p>
 * Note that {@link ParamValue} is by default detected by its type (class)
 * and is rendered in the way as the language trait specifies. Therefore
 * one has to use this renderer by addressing the specified name, 'tc'.
 * Moreover, also note that when the parameter value is processed by this,
 * it does not respect the language traits. For example, there is no 'LL'
 * suffixe for long integers, and the parenthesis '{', '}' is used for arrays.
 *
 * @author Jongwook Choi
 * @since 2.0
 *
 */
public class ParamValueTcRenderer implements NamedRenderer {

    @Override
    public RenderFormatInfo getFormatInfo() {
         return null;
    }

    @Override
    public String getName() {
        return "tc";
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[] { ParamValue.class };
    }

    @Override
    public String render(Object o, String param, Locale locale) {
        if (!(o instanceof ParamValue))
            return "";

        ParamValue paramValue = (ParamValue) o;
        List<String> params = parseParams(param);

        boolean isGrid = params.contains("grid");
        return doRender(paramValue, isGrid);
    }

    private String doRender(ParamValue paramValue, boolean isGrid) {
        Param param = paramValue.getParam();
        if(! param.getType().isArray())
            return paramValue.getValue();

        String separator = isGrid ? ",\n " : ", ";

        // TODO duplication with ParamValue.Value
        String[] valueList = paramValue.getValueList();
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        String sep = "";
        for (int i = 0; i < valueList.length; ++ i) {
            String s = valueList[i];
            if(i == valueList.length) sep = "";
            buf.append(sep);
            sep = separator;
            buf.append(s);
        }
        buf.append(" }");
        return buf.toString();
    }


    // Helpers

    private List<String> parseParams(String param) {
        // TODO Refactor out (see StringUtilRenderer)
        if(param == null) return Collections.emptyList();
        List<String> params = new ArrayList<String>();

        for(String func : param.split(",")) {
            func = func.trim();
            if(func.isEmpty()) continue;
            params.add(func.toLowerCase());
        }
        return params;
    }
}
