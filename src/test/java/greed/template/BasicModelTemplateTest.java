package greed.template;

import static org.junit.Assert.assertEquals;
import greed.code.LanguageManager;
import greed.code.LanguageTrait;
import greed.model.Language;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.Collections;

import org.junit.Test;

public class BasicModelTemplateTest {

    TemplateEngine engine;
    LanguageTrait trait;

    private void switchLanguage(Language lang) {
        this.engine = TemplateEngine.newLanguageEngine(lang);
        this.trait = LanguageManager.getInstance().getTrait(lang);
    }

    @Test
    public void paramValueWithoutTcRenderer() {
        switchLanguage(Language.CPP);

        Param param = new Param("arg0", Type.LONG_ARRAY_TYPE, 0);
        ParamValue paramValue = trait.parseValue("{0, 0}", param);

        String template = "${foreach in.ValueList v ,}${v}${end}";
        String rendered = engine.render(template, Collections.singletonMap("in", (Object)paramValue));
        assertEquals("0LL, 0LL", rendered);
    }

}
