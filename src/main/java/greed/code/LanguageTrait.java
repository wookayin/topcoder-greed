package greed.code;

import greed.model.Param;
import greed.model.ParamValue;

/**
 * Greed is good! Cheers!
 */
public interface LanguageTrait {

    /**
     * @return the comment prefix for this language
     */
    public String getCommentPrefix();

    /**
     * Parse from the input value string for the parameter {@code param}
     * to create a {@link ParamValue}.
     *
     * @param value value string for the parameter
     * @param param the parameter or element type
     * @return parsed {@link ParamValue}
     */
    public ParamValue parseValue(String value, Param param);
}
