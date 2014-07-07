package greed.template;

import greed.model.Argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

/**
 * Renderer used in templates such as <code>${v;string("unquote")}</code>.
 * <p>
 * Available filters:
 * <ul>
 *  <li>lower</li>
 *  <li>upcasefirst</li>
 *  <li>removespace</li>
 *  <li>unquote</li>
 *  <li>abbr</li>
 * </ul>
 *
 * @author Shiva wu
 * @author vexorian
 * @author Jongwook Choi
 */
public class StringUtilRenderer implements NamedRenderer {

    private List<String> parseParams(String param) {
        if(param == null) return Collections.emptyList();
        List<String> params = new ArrayList<String>();

        for(String func : param.split(",")) {
            func = func.trim();
            if(func.isEmpty()) continue;
            params.add(func.toLowerCase());
        }
        return params;
    }

    @Override
    public String render(Object o, String param, Locale locale) {
        if (o instanceof String) {
            return renderString((String)o, param);
        }
        else if (o instanceof Argument) {
            // just for compatbility prior to 2.0-RC.
            return renderArgument((Argument)o, param);
        }
        else {
            return "";
        }
    }

    private String renderString(String result, String param) {
        for (String func: parseParams(param)) {
            if ("lower".equals(func)) {
                result = applyLower(result);
            }
            else if ("upcasefirst".equals(func)) {
                result = applyUpcaseFirst(result);
            }
            else if ("removespace".equals(func)) {
                result = applyRemoveSpace(result);
            }
            else if ("unquote".equals(func)) {
                result = applyUnquote(result);
            }
            else if ("abbr".equals(func)) {
                result = applyAbbr(result);
            }
        }
        return result;
    }

    private String renderArgument(Argument arg, String param) {
        return renderString(arg.toString(), param);
    }



    private String applyLower(String s) {
        return s.toLowerCase();
    }

    private String applyUpcaseFirst(String s) {
        if(s.length() > 0) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
        return s;
    }

    private String applyRemoveSpace(String s) {
        return s.replaceAll("\\s",  "");
    }

    private String applyUnquote(String s) {
        int n = s.length();
        if (n >= 2 && s.charAt(0) == '"' && s.charAt(n - 1) == '"')
            s = s.substring(1, n - 1);
        return s;
    }

    private String applyAbbr(String s) {
        String[] tokens = s.split("\\s+");
        StringBuilder abbr = new StringBuilder();
        for (String tok : tokens) {
            if (allDigits(tok) || allUppercaseOrDigits(tok))
                abbr.append(tok);
            else
                abbr.append(tok.substring(0, 1).toUpperCase());
        }
        return abbr.toString();
    }

    @Override
    public String getName() {
        return "string";
    }

    private boolean allDigits(String s) {
        for (int i = 0; i < s.length(); ++i)
            if (!Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }

    private boolean allUppercaseOrDigits(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isUpperCase(s.charAt(i)) && !Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
    }

    @Override
    public RenderFormatInfo getFormatInfo() {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[] { String.class, Argument.class };
    }
}
