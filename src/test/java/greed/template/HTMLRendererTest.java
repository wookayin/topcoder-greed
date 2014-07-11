package greed.template;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import greed.code.lang.CppLanguage;
import greed.model.Language;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * @see HTMLRenderer
 *
 * @author Jongwook Choi
 *
 */
public class HTMLRendererTest {

    private TemplateEngine engine;

    @Before
    public void setup() {
        engine = TemplateEngine.newLanguageEngine(Language.CPP);
    }

    static final Param GRID_LIKE_STRING_ARRAY_PARAM =
            new Param("gridParam", Type.STRING_ARRAY_TYPE, 0);

    static final ParamValue GRID_LIKE_STRING_ARRAY_PARAMVALUE = CppLanguage.instance.parseValue(
            "{\"####\", \"#..#\", \"#..#\", \"####\"}",
            GRID_LIKE_STRING_ARRAY_PARAM);


    private static Map<String, Object> createModel(String paramName, Type type, String valueToParse) {
        Map<String, Object> model = new HashMap<String, Object>();
        Param param = new Param(paramName, type, 0);
        ParamValue paramValue = CppLanguage.instance.parseValue(valueToParse, param);
        model.put(paramName, paramValue);
        return model;
    }

    ///////////////////////////////////////////////////////

    @Test
    public void arrayParametersInHtml() {
        Map<String, Object> model = createModel("param", Type.LONG_ARRAY_TYPE, "{-1, 0, 1, 9876543210}");
        String result = engine.render("${param;html}", model);
        System.out.println(result);
        assertThat(result, equalTo("{ -1, 0, 1, 9876543210 }"));
    }

    @Test
    public void emptyArrayParametersInHtml() {
        Map<String, Object> model; String result;

        // String Type
        model = createModel("emptyParam", Type.STRING_ARRAY_TYPE, "{ }");
        result = engine.render("${emptyParam;html}", model);
        System.out.println(result);
        assertThat(result, equalTo("{ }"));

        // Other Type
        model = createModel("emptyParam", Type.DOUBLE_ARRAY_TYPE, "{ }");
        result = engine.render("${emptyParam;html}", model);
        System.out.println(result);
        assertThat(result, equalTo("{ }"));
    }

    @Test
    public void stringParametersInHtml() {
        Map<String, Object> model; String result;

        // normal string param
        model = createModel("strParam", Type.STRING_TYPE, "\"topcoder\"");
        result = engine.render("${strParam;html}", model);
        System.out.println(result);
        assertThat(result, equalTo("&quot;topcoder&quot;")); // needs quote!!

        // empty string param
        model = createModel("emptyStrParam", Type.STRING_TYPE, "\"\"");
        result = engine.render("${emptyStrParam;html}", model);
        System.out.println(result);
        assertThat(result, equalTo("&quot;&quot;")); // needs quote!!
    }

    @Test
    public void testRenderHtmlGridFilter() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("gridParam", GRID_LIKE_STRING_ARRAY_PARAMVALUE);
        String result = engine.render("${gridParam;html(grid)}", model);

        System.out.println(result);
        assertThat(result, equalTo("{&quot;####&quot;,<br />&nbsp;&quot;#..#&quot;,<br />&nbsp;&quot;#..#&quot;,<br />&nbsp;&quot;####&quot;}"));
    }

    @Test
    public void testRenderHtmlNonGridFilter() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("gridParam", GRID_LIKE_STRING_ARRAY_PARAMVALUE);
        String result = engine.render("${gridParam;html}", model);

        System.out.println(result);
        assertThat(result, equalTo("{ &quot;####&quot;, &quot;#..#&quot;, &quot;#..#&quot;, &quot;####&quot; }"));
    }

    @Test
    public void testRenderHtmlStringQuote() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("stringParam", new ParamValue(
                    new Param("stringParam", Type.STRING_TYPE, 0),
                    "\"quoted\"")  // value should be in a canonical form
                );

        String result = engine.render("${stringParam;html}", model);
        System.out.println(result);
        assertThat(result, equalTo("&quot;&quot;quoted&quot;&quot;"));

        model.put("gridParam", new ParamValue(
                    new Param("gridParam", Type.STRING_ARRAY_TYPE, 1),
                    new String[] {"\"\"", ".."} // 2x2
                    // each value should be in a canonical form (i.e. no enclosing quotes)
                    )
                );
        String gridResult = engine.render("${gridParam;html(grid)}", model);
        System.out.println(gridResult);
        assertThat(gridResult, equalTo("{&quot;&quot;&quot;&quot;,<br />&nbsp;&quot;..&quot;}"));
    }

}
