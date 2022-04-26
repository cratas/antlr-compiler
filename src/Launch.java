

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

import static org.antlr.v4.runtime.CharStreams.fromFileName;


public class Launch {
    public static void main(String[] args) {
        try {
            String source = "input1.txt";
            System.out.println("Parsing:" + source);
            CharStream cs = (CharStream) fromFileName(source);
            gLexer lexer = new gLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            gParser parser = new gParser(tokens);

            parser.addErrorListener(new VerboseErrorListener());

            ParseTree tree = parser.program();


            if(parser.getNumberOfSyntaxErrors() == 0) {
                var result = new gBaseVisitor<>().visit(tree);
                System.out.println(result);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}