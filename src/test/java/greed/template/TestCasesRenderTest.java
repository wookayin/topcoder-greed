package greed.template;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import greed.conf.ConfigException;
import greed.model.Language;
import greed.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestCasesRenderTest {

    private InputStream template;

    Map<String, Object> model = TestModelFixtures.buildStubbingModel(Language.CPP);
    TemplateEngine engine;

    @BeforeClass
    public static void initializeGreed() throws ConfigException {
        // TODO : Why at all do we need this?
        Utils.initialize();
    }

    @Before
    public void setupTemplates() throws IOException {
        this.template = getClass().getResourceAsStream("/templates/testcase/testcases.tmpl");
        assertThat(this.template, notNullValue());

        engine = TemplateEngine.newLanguageEngine(Language.CPP);
    }

    @Test
    public void renderTemplate() {
        String code = engine.render(template, model);
        System.out.println(code);

        assertThat(code, containsString("15\n5\n919\n111\n234\n567\n2147483647987987"));
        assertThat(code, containsString("4\nNNYYNN\nNNNNNN\nYNYNYN\nNYNYNY\n\n4"));
    }

}
