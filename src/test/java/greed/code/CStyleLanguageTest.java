package greed.code;

import static org.junit.Assert.assertEquals;
import greed.code.lang.AbstractLanguage;
import greed.code.lang.CStyleLanguage;
import greed.code.lang.CppLanguage;
import greed.model.Argument;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Greed is good! Cheers!
 *
 * @see CStyleLanguage
 * @see AbstractLanguage
 *
 * @author Shiva Wu
 * @author Jongwook Choi (wook)
 */
public class CStyleLanguageTest {

    CppLanguage trait;

    @Before
    public void setup() {
        trait = CppLanguage.instance;
    }

    @Test
    public void parsePrimitivesTest() {
        ParamValue pv;

        pv = trait.parseValue("1", new Param("arg", Type.INT_TYPE, 0));
        assertEquals(Argument.ofInt(1), pv.getArgument());
        assertEquals("1", pv.getValue());

        pv = trait.parseValue("1", new Param("arg", Type.LONG_TYPE, 0));
        assertEquals(Argument.ofLong(1L), pv.getArgument());
        assertEquals("1", pv.getValue());   // not 1LL

        pv = trait.parseValue("1.0", new Param("arg", Type.DOUBLE_TYPE, 0));
        assertEquals(Argument.ofDouble(1.0), pv.getArgument());
        assertEquals("1.0", pv.getValue());

        pv = trait.parseValue("true", new Param("arg", Type.BOOL_TYPE, 0));
        assertEquals(Argument.ofBoolean(true), pv.getArgument());
        assertEquals("true", pv.getValue());

        pv = trait.parseValue("\"\"", new Param("arg", Type.STRING_TYPE, 0));
        assertEquals(Argument.ofString(""), pv.getArgument()); // wtf?
        assertEquals("\"\"", pv.getValue());   // TODO: [""](X) [](O)

        pv = trait.parseValue("\"Alice\"", new Param("arg", Type.STRING_TYPE, 0));
        assertEquals(Argument.ofString("Alice"), pv.getArgument());
        assertEquals("\"Alice\"", pv.getValue()); // TODO: "Alice"(X) Alice(O)
    }

    @Test
    public void parseStringArrayTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("{    \n");
        sb.append("\"Abcde\"\n, \"12345\", \n\n\n");
        sb.append("\"Hello\"\n");
        sb.append(", \"world\", \"\"   }");

        ParamValue pv = trait.parseValue(sb.toString(),
                new Param("arg", Type.STRING_ARRAY_TYPE, 0));
        Argument[] parsedValueList = pv.getArgumentList();

        for (Argument v : parsedValueList)
            System.out.println(v);

        // in argument form
        Assert.assertThat( Arrays.asList(parsedValueList), Matchers.equalTo(Arrays.asList(
                        Argument.ofString("Abcde"),
                        Argument.ofString("12345"),
                        Argument.ofString("Hello"),
                        Argument.ofString("world"),
                        Argument.ofString("")
                    ))
        );

        // in string form
        Assert.assertThat( pv.getValueStringList(), Matchers.equalTo(new String[] {
                "\"Abcde\"", "\"12345\"", "\"Hello\"", "\"world\"", "\"\""
        }));
    }

    @Test
    public void parseLongArrayTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("{12345678987654321,    ");
        sb.append("\n123\n,125,999,\n\n12\n,123\n    } \n");

        ParamValue pv = trait.parseValue(sb.toString(),
                new Param("arg", Type.LONG_ARRAY_TYPE, 0));
        Argument[] parsedValueList = pv.getArgumentList();

        for (Argument v : parsedValueList)
            System.out.println(v);

        // in argument form
        Assert.assertThat( Arrays.asList(parsedValueList), Matchers.equalTo(Arrays.asList(
                        Argument.ofLong(12345678987654321L),
                        Argument.ofLong(123),
                        Argument.ofLong(125),
                        Argument.ofLong(999),
                        Argument.ofLong(12),
                        Argument.ofLong(123)
                    ))
        );

        // in string form
        // NOTE: The 'LL' suffix is removed during the refactoring on ParamValue.
        // see also the test on how templates are acutally rendered.
        Assert.assertThat( pv.getValueStringList(), Matchers.equalTo(new String[] {
                "12345678987654321", "123", "125", "999", "12", "123"
        }));

    }
}
