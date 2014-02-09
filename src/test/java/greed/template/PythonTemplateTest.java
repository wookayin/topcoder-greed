package greed.template;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import greed.model.Language;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;



/**
 * @see greed.code.lang.PythonLanguage
 *
 * @since 1.5
 * @author Jongwook Choi (wook)
 *
 */
public class PythonTemplateTest {

    InputStream codeTemplate;
    InputStream testTemplate;

    Map<String, Object> model = TestModelFixtures.buildStubbingModel(Language.PYTHON);
    TemplateEngine engine;

    @Before
    public void setupTemplates() throws IOException {
        this.codeTemplate = getClass().getResourceAsStream("/templates/source/py.tmpl");
        assertThat(this.codeTemplate, notNullValue());

        this.testTemplate = getClass().getResourceAsStream("/templates/test/py.tmpl");
        assertThat(this.testTemplate, notNullValue());

        engine = TemplateEngine.newLanguageEngine(Language.PYTHON);
    }

    @Test
    public void renderPythonTestCode() {
        String testCode = engine.render(testTemplate, model);
        System.out.println(testCode);

        // naive and minimal assertions by string matching
        assertThat(testCode, containsString("do_test(arg1, arg2, arg3, __expected"));
        assertThat(testCode, containsString("arg1 = 15"));
        assertThat(testCode, containsString("arg2 = (\n" +
            "            919,\n" +
            "            111,\n" +
            "            234,\n" +
            "            567,\n" +
            "            2147483647987987\n" +
            "        )"
        ));
        assertThat(testCode, containsString("arg3 = (\n" +
            "            \"NNYYNN\",\n" +
            "            \"NNNNNN\",\n" +
            "            \"YNYNYN\",\n" +
            "            \"NYNYNY\"\n" +
            "        )"
        ));
        assertThat(testCode, containsString("return do_test(arg1, arg2, arg3, __expected,"));
    }

    @Test
    public void renderPythonMainCode() {
        String code = engine.render(codeTemplate, model);
        System.out.println(code);

        // naive assertions by string matching
        assertThat(code, containsString("class Test:"));
        assertThat(code, containsString("def TestMethod(self, arg1, arg2, arg3):"));
    }
}
