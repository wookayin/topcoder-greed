package greed.code;

import static org.junit.Assert.assertEquals;
import greed.code.lang.AbstractLanguage;
import greed.code.lang.CStyleLanguage;
import greed.code.lang.CppLanguage;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.Arrays;

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
        assertEquals("1", pv.getValue());

        pv = trait.parseValue("1", new Param("arg", Type.LONG_TYPE, 0));
        assertEquals("1", pv.getValue());   // not 1LL

        pv = trait.parseValue("1.0", new Param("arg", Type.DOUBLE_TYPE, 0));
        assertEquals("1.0", pv.getValue());

        pv = trait.parseValue("true", new Param("arg", Type.BOOL_TYPE, 0));
        assertEquals("true", pv.getValue());

        pv = trait.parseValue("\"\"", new Param("arg", Type.STRING_TYPE, 0));
        assertEquals("\"\"", pv.getValue());

        pv = trait.parseValue("\"Alice\"", new Param("arg", Type.STRING_TYPE, 0));
        assertEquals("\"Alice\"", pv.getValue()); // TODO: "Alice"(X) Alice(O)
    }

    @Test
    public void parseStringArrayTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("{    \n");
        sb.append("\"Abcde\"\n, \"12345\", \n\n\n");
        sb.append("\"Hello\"\n");
        sb.append(", \"world\", \"\"   }");

        String[] parsedValueList = trait.parseValue(sb.toString(),
                new Param("arg", Type.STRING_ARRAY_TYPE, 0)).getValueList();

        for (String pv : parsedValueList)
            System.out.println(pv);

        Assert.assertArrayEquals("Parsed value is " + Arrays.toString(parsedValueList),
                new String[]{"\"Abcde\"", "\"12345\"", "\"Hello\"", "\"world\"", "\"\""},
                parsedValueList
        );
    }

    @Test
    public void parseLongArrayTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("{12345678987654321,    ");
        sb.append("\n123\n,125,999,\n\n12\n,123\n    } \n");

        String[] parsedValueList = trait.parseValue(sb.toString(),
                new Param("arg", Type.LONG_ARRAY_TYPE, 0)).getValueList();

        for (String pv : parsedValueList)
            System.out.println(pv);

        Assert.assertArrayEquals("Parsed value is " + Arrays.toString(parsedValueList),
                new String[]{"12345678987654321LL", "123LL", "125LL", "999LL", "12LL", "123LL"},
                parsedValueList
                );
    }
}
