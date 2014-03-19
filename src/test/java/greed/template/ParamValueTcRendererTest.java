package greed.template;

import greed.code.LanguageManager;
import greed.model.Language;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @see ParamValueTcRenderer
 *
 * @author Jongwook Choi
 */
public class ParamValueTcRendererTest {

    private TemplateEngine engine;

    @Before
    public void setup() {
        engine = TemplateEngine.newLanguageEngine(Language.CPP);
    }

    private static ParamValue getParamValue(String raw, Param param) {
        return LanguageManager.getInstance().getTrait(Language.CPP).parseValue(raw, param);
    }


    private static String rendersTo(String string) {
        return string;
    }

    private void assertParamValue(String options, Type paramType, String rawTcString, String expected) {
        Param param = new Param("arg0", paramType, 0);
        ParamValue value = getParamValue(rawTcString, param);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("Param", param);
        model.put("ParamValue", value);

        String templateExpr = "${ParamValue;tc}";
        if(options != null) templateExpr = "${ParamValue;" + options + "}";

        String rendered = engine.render(templateExpr, model);
        System.out.println(rendered);
        Assert.assertEquals(expected, rendered);
    }
    private void assertParamValue(Type paramType, String rawTcString, String expected) {
        assertParamValue(null, paramType, rawTcString, expected);
    }

    @Test
    public void paramValuePrimitives() {
        assertParamValue(Type.INT_TYPE, "1", rendersTo("1"));
        assertParamValue(Type.STRING_TYPE, "\"YES\"", rendersTo("\"YES\""));
        assertParamValue(Type.LONG_TYPE, "98765432123456789", rendersTo("98765432123456789"));
        assertParamValue(Type.DOUBLE_TYPE, "3.14159265358979", rendersTo("3.14159265358979"));
    }

    @Test
    public void paramValueArrays() {
        assertParamValue(Type.INT_ARRAY_TYPE, "{}", rendersTo("{  }"));
        assertParamValue(Type.INT_ARRAY_TYPE, "{ 3 }", rendersTo("{ 3 }"));
        assertParamValue(Type.INT_ARRAY_TYPE, "{ 1, 2, 3 }", rendersTo("{ 1, 2, 3 }"));

        assertParamValue(Type.LONG_ARRAY_TYPE, "{ 123456789012345 }", rendersTo("{ 123456789012345 }")); // no LL
        assertParamValue(Type.LONG_ARRAY_TYPE, "{ 0 }", rendersTo("{ 0 }")); // no LL
        assertParamValue(Type.LONG_ARRAY_TYPE, "{ 1, 2, 3 }", rendersTo("{ 1, 2, 3 }"));

        assertParamValue(Type.STRING_ARRAY_TYPE, "{ }", rendersTo("{  }"));
    }

    // TODO grid
    @Test
    public void paramValueArraysInGridStyle() {
        assertParamValue(Type.STRING_ARRAY_TYPE, "{}", rendersTo("{  }"));

        // TODO is this right? with quotation? or without?
        assertParamValue(Type.STRING_ARRAY_TYPE, "{ \"YYY\", \"NNN\", \"YNY\" }",
                rendersTo("{ YYY, NNN, YNY }"));
    }
}
