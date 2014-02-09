package greed.template;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import greed.conf.ConfigException;
import greed.model.Language;
import greed.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CppTemplateTest {

    private InputStream codeTemplate;
    private InputStream testTemplate;

    Map<String, Object> model = TestModelFixtures.buildStubbingModel(Language.CPP);
    TemplateEngine engine;

    @BeforeClass
    public static void initializeGreed() throws ConfigException {
        // TODO : Why at all do we need this?
        Utils.initialize();
    }

    @Before
    public void setupTemplates() throws IOException {
        this.codeTemplate = getClass().getResourceAsStream("/templates/source/cpp.tmpl");
        assertThat(this.codeTemplate, notNullValue());

        this.testTemplate = getClass().getResourceAsStream("/templates/test/cpp.tmpl");
        assertThat(this.testTemplate, notNullValue());

        engine = TemplateEngine.newLanguageEngine(Language.CPP);
    }

    @Test
    public void renderCppCode() {
        String test = engine.render(testTemplate, model);
        model.put("TestCode", test);
        String code = engine.render(codeTemplate, model);
        System.out.println(code);

        // TODO verify to make test fail on malfunctioning

        // 1. verify that each long/string array is rendered well
        assertTrue(code.contains("919LL,"));
        assertTrue(code.contains("2147483647987987LL"));
        assertTrue(code.contains("\"NNYYNN\","));
        assertTrue(code.contains("\"NYNYNY\""));
    }

    @Test
    public void renderCppCode_cxx11() {
        Map<String,String> mp = new HashMap<String,String>();
        mp.put("cpp11", "true");
        model.put("Options", mp);
        String test = engine.render(testTemplate, model);
        model.put("TestCode", test);
        String code = engine.render(codeTemplate, model);
        System.out.println(code);

        // TODO verify to make test fail on malfunctioning

        // 1. verify to use initializer list (depends on the template)
        assertTrue(code.contains("vector<long long> arg2 = {"));
        assertTrue(code.contains("vector<string> arg3 = {"));
    }

}
