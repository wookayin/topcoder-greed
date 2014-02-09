package greed.template;

import greed.code.LanguageManager;
import greed.code.LanguageTrait;
import greed.model.Language;
import greed.model.Method;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Problem;
import greed.model.Testcase;
import greed.model.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * A common fixtures or stubbing models for testing templates.
 *
 * @author Jongwook Choi
 * @author Shiva Wu
 *
 */
class TestModelFixtures {

    private TestModelFixtures() { }

    static Map<String, Object> buildStubbingModel(Language language) {
        return buildStubbingModel(
                LanguageManager.getInstance().getTrait(language)
                );
    }

    /**
     * @param language the language. This is necessary since models such as {@link ParamValue}
     *  are language-specific (it contains the rendered value) as of now. (TODO separate)
     */
    static Map<String, Object> buildStubbingModel(LanguageTrait language) {

        Param paramInt = new Param("arg1", Type.INT_TYPE, 0);
        Param paramLongArray = new Param("arg2", Type.LONG_ARRAY_TYPE, 1);
        Param paramStringArray = new Param("arg3", Type.STRING_ARRAY_TYPE, 2);

        Type retType = Type.STRING_ARRAY_TYPE;
        Param output = new Param("return", retType, 0);

        ParamValue valueInt = language.parseValue("15", paramInt);
        ParamValue valueLongArray = language.parseValue(
                "{919, 111, 234, 567, 2147483647987987}", paramLongArray);
        ParamValue valueStringArray = language.parseValue(
                "{\"NNYYNN\", \"NNNNNN\", \"YNYNYN\", \"NYNYNY\"}", paramStringArray);
        ParamValue valueOutput = language.parseValue(
                "{\"abcd\", \"efg\", \"123\", \"456\"}", output);

        Method method = new Method("TestMethod", retType, new Param[]{paramInt, paramLongArray, paramStringArray});

        Testcase case0 = new Testcase(0, new ParamValue[]{
                valueInt,
                valueLongArray,
                valueStringArray
        }, valueOutput);

        Problem problem = new Problem("Test", 250, "TestClass", 2000, 256, false, method, new Testcase[]{case0}, null);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("Problem", problem);
        model.put("ClassName", "Test");
        model.put("Method", problem.getMethod());
        model.put("Examples", problem.getTestcases());
        model.put("NumOfExamples", problem.getTestcases().length);
        model.put("HasArray", true);
        model.put("ReturnsArray", true);
        model.put("RecordRuntime", true);
        model.put("RecordScore", true);
        model.put("CreateTime", System.currentTimeMillis() / 1000);

        return model;
    }
}
