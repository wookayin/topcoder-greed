package greed.template;

import greed.code.LanguageManager;
import greed.model.Argument;
import greed.model.Language;
import greed.model.Method;
import greed.model.ParamValue;
import greed.model.Type;
import greed.util.StringUtil;

import java.util.Locale;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;


/**
 * Greed is good! Cheers!
 *
 * @author vexorian
 * @author Jongwook Choi
 */
public class HTMLRenderer implements NamedRenderer {

    private String stripHTML(String v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length(); i++) {
            switch (v.charAt(i)) {
            case '"':
                sb.append("&quot;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            case '\'':
                sb.append("&apos;");
                break;
            default:
                sb.append(v.charAt(i));
            }
        }
        return sb.toString();
    }

    private String renderParamValue(ParamValue pv, String param) {
        Type t = pv.getParam().getType();

        if (t.isString()) {
            if (t.isArray()) {
                Argument[] x = pv.getArgumentList();
                boolean useGrid = isGridMode(param, x);
                return doRenderStringArray(x, useGrid);
            } else {
                return stripHTML(pv.getArgument().toString());
            }
        }
        return pv.getValue();
    }

    private boolean isGridMode(String param, Argument[] x) {
        boolean grid = ((x.length > 1) && "grid".equals(param));
        if (grid) {
            int s = x[0].getValue().length();
            for (Argument y : x) {
                if(!y.getType().isString()) return false;
                grid = ( grid && (s == y.getValue().length()) );
            }
        }
        return grid;
    }

    private String doRenderStringArray(Argument[] x, boolean grid) {
        if(x.length == 0) return "{ }";

        StringBuilder sb = new StringBuilder();
        String[] xQuoted = new String[x.length];

        for(int i = 0; i < x.length; ++ i) {
            xQuoted[i] = stripHTML(x[i].toString());
        }

        sb.append("{");
        if(grid) {
            sb.append(StringUtil.join(xQuoted, ",<br />&nbsp;"));
        } else {
            sb.append(" ");
            sb.append(StringUtil.join(xQuoted, ", "));
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String render(Object o, String param, Locale locale) {
        if (o instanceof ParamValue) {
            return renderParamValue( (ParamValue)o, param);
        }
        if (o instanceof Argument) {
            Argument arg = (Argument) o;
            return stripHTML(arg.getValue());
        }
        else if (o instanceof String) {
            return stripHTML( (String)o );
        }
        else if (o instanceof Type) {
            return stripHTML(
                    LanguageManager.getInstance().getRenderer(Language.fromString(param)).renderType((Type) o)
            );
        }
        else if (o instanceof Method) {
            return stripHTML(
                    LanguageManager.getInstance().getRenderer(Language.fromString(param)).renderMethod((Method) o)
            );
        }
        return "(No HTML renderer support for type: " + o.getClass().getName() + ")";
    }

    @Override
    public String getName() {
        return "html";
    }

    @Override
    public RenderFormatInfo getFormatInfo() {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[]{ ParamValue.class, Argument.class, String.class, Type.class, Method.class };
    }
}
