package greed.template;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import greed.code.LanguageManager;
import greed.code.LanguageTrait;
import greed.model.Language;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class BasicModelTemplateTest {

    TemplateEngine engine;
    LanguageTrait trait;

    private void switchLanguage(Language lang) {
        this.engine = TemplateEngine.newLanguageEngine(lang);
        this.trait = LanguageManager.getInstance().getTrait(lang);
    }

    @Before
    public void setup() {
        switchLanguage(Language.CPP);
    }

    @Test
    public void paramValueWithoutTcRenderer() {
        Param param = new Param("arg0", Type.LONG_ARRAY_TYPE, 0);
        ParamValue paramValue = trait.parseValue("{0, 0}", param);

        String template = "${foreach in.ValueList v ,}${v}${end}";
        Map<String, Object> model = Collections.singletonMap("in", (Object) paramValue);
        String rendered = engine.render(template, model);

        // TODO is this right?
        assertThat(rendered, is("0LL,0LL"));
    }

    @Test
    public void paramValueAsForeachIterable() {
        Param param = new Param("arg0", Type.LONG_ARRAY_TYPE, 0);
        ParamValue paramValue = trait.parseValue("{0, 1}", param);

        String template = "${foreach in v ,}${v}${end}";
        Map<String, Object> model = Collections.singletonMap("in", (Object) paramValue);
        String rendered = engine.render(template, model);
        System.out.println(rendered);

        assertThat(rendered, equalTo("0LL,1LL"));
    }

}
