package greed.template;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import greed.model.Argument;
import greed.model.Language;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Greed is good! Cheers!
 *
 * @see StringUtilRenderer
 *
 * @author Shiva Wu
 * @author Jongwook Choi
 */
public class StringUtilRendererTest {

    private Map<String, Object> createModel(String key, Object value) {
        return Collections.singletonMap(key, value);
    }

    private TemplateEngine engine;

    @Before
    public void setup() {
        engine = TemplateEngine.newLanguageEngine(Language.CPP);
    }

    @Test
    public void testContestName() {
        Map<String, Object> model = createModel("ContestName", "SRM    245");
        String result = engine.render("${ContestName;string(lower,removespace)}", model);
        Assert.assertEquals(result, "srm245");
    }

    @Test
    public void testAbbr() {
        Map<String, Object> model;

        model = createModel("ContestName", "Single Round Match    245");
        assertEquals("SRM245", engine.render("${ContestName;string(abbr)}", model));

        model = createModel("ContestName", "TCHS 21 Div 1");
        assertEquals("TCHS21D1", engine.render("${ContestName;string(abbr)}", model));

        model = createModel("ContestName", "TCO11");
        assertEquals("TCO11", engine.render("${ContestName;string(abbr)}", model));
    }

    @Test
    public void testUpcaseFirst() {
        Map<String, Object> model = createModel("Var", "topcoder");
        assertEquals("Topcoder", engine.render("${Var;string(upcasefirst)}", model));
    }

    @Test
    public void testRemovespace() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("V1", "A B C");
        model.put("V2", "   Topcoder");
        model.put("V3", "   Single Round Match     ");

        assertEquals("ABC", engine.render("${V1;string(removespace)}", model));
        assertEquals("Topcoder", engine.render("${V2;string(removespace)}", model));
        assertEquals("SingleRoundMatch", engine.render("${V3;string(removespace)}", model));
    }

    @Test
    public void testUnquote() {
        Map<String, Object> model;

        model = createModel("Var", "\"\"");
        assertEquals("", engine.render("${Var;string(unquote)}", model));

        model = createModel("Var", "\"####\"");
        assertEquals("####", engine.render("${Var;string(unquote)}", model));
    }

    @Test
    public void testFilterChains() {
        Map<String, Object> model ;

        //model = createModel("Var", "Topcoder Single Round Match 200");
        //Assert.assertEquals("Srm 200-224",
        //        engine.render("${Var;string(contestcategory, lower, upFirst, unquote)}", model));

        model = createModel("Var", "Topcoder Single Round Match 200");
        assertEquals("Topcoder Single Round Match 200",
                engine.render("${Var;string()}", model));
    }

    @Test
    public void testOnArgument() {
        // does it handle correctly instances of 'Argument'?

        Map<String, Object> model = new LinkedHashMap<String, Object>();
        model.put("LongVar", Argument.ofLong(9876543210L));
        model.put("StringVar", Argument.ofString("YNYN"));

        assertThat(engine.render("${LongVar}", model), equalTo("9876543210LL")); // oops. the language is C++
        assertThat(engine.render("${LongVar;string}", model), equalTo("9876543210"));
        assertThat(engine.render("${LongVar;string(\"unquote\")}", model), equalTo("9876543210"));

        // TODO decide the behavior on string arguments -- with quote? without quote?
        // as of now, just as in '.Value', it is quoted by default.
        assertThat(engine.render("${StringVar}", model), equalTo("\"YNYN\""));
        assertThat(engine.render("${StringVar;string}", model), equalTo("\"YNYN\""));
        assertThat(engine.render("${StringVar;string(\"unquote\")}", model), equalTo("YNYN"));
    }

}
