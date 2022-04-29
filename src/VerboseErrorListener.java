import org.antlr.v4.runtime.*;

import java.util.Collections;
import java.util.List;

public class VerboseErrorListener extends BaseErrorListener {

    public void SyntaxError(Recognizer recognizer, Token offendingSymbol, int line, int charPositionLine, String msg, RecognitionException e) {
        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);

        System.out.println("rule stack: " + String.join(", ", stack));
        System.out.println("line " + line + ":" + charPositionLine + " at "+ offendingSymbol + ": " + msg);
    }

    public void unknownDataType(String datatype)
    {
        System.out.println("Uknown datatype: " + datatype);
    }

    public void undefinedVariable(String variable)
    {
        System.out.println("Undefined variable: " + variable);
    }
}
