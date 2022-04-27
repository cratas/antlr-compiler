import java.util.HashMap;
import java.util.Map;

public class MyVisitor extends gBaseVisitor<MyVisitor.MyObject>{
    static final String INTEGER = "I";
    static final String STRING = "S";
    static final String FLOAT = "F";
    static final String BOOLEAN = "B";
    Map<String, String> vars = new HashMap<String, String>();
    public static StringBuilder generatedOutput = new StringBuilder();

    int expCounter = 0;

    public class MyObject
    {
        public String type;
        public String value;
    };

    @Override public MyObject visitProgram(gParser.ProgramContext ctx) {

        for(var l : ctx.line()) {
            visit(l);
        }

        return null;
    }
    @Override public MyObject visitDeclaration(gParser.DeclarationContext ctx) {
        // if there are no childs, return
        if(ctx.children.get(0) == null)
            return null;

        var dataType = ctx.datatype().children.get(0).toString();
        MyObject mo = new MyObject();

        for(var i : ctx.ID()) {
            switch(dataType) {
                case "int":
                    mo.type = INTEGER;
                    mo.value = "0";
                    break;
                case "string":
                    mo.type = STRING;
                    mo.value = "\"\"";;
                    break;
                case "float":
                    mo.type = FLOAT;
                    mo.value = "0.0";
                    break;
                case "bool":
                    mo.type = BOOLEAN;
                    mo.value = "true";
                    break;
                default:
                    // there should be warning method from VerboseErrorListener class
                    System.out.println("UNKNOWN datatype");
            }

            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + mo.type + " " + mo.value);
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("save " +i.toString());
            vars.put(i.toString(), mo.type);
        }

        return null;
    }

    @Override public MyObject visitAssigment(gParser.AssigmentContext ctx) {
        MyObject mo = new MyObject();

        if(ctx.exp() != null) {
            return visit(ctx.exp());
        } else {
            if(vars.containsKey(ctx.ID().getText())) {
                mo = visitAssigment(ctx.assigment());
                boolean isNegative = false;

                if((mo.type.equals("I")) && (mo.value != null)) {
                    int intValue = Integer.parseInt(mo.value);
                    isNegative = intValue < 0;
                    if(isNegative) {
                        intValue = intValue*-1;
                        mo.value = Integer.toString(intValue);
                    }
                }

                if(ctx.getChild(2).getChild(0).getChild(0) != null) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("push " + mo.type + " " + mo.value);
                }

                if(isNegative) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("uminus");
                }

                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("save " + ctx.ID().getText());
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("load " + ctx.ID().getText());
            } else {
                // there should be error from VerboseErrorListener class
                System.out.println("Undefined variable");
            }

        }

        if(!(ctx.getParent().getClass() == ctx.assigment().getClass())) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("pop");
        }

        return mo;
    }

    @Override public MyObject visitPrint(gParser.PrintContext ctx) {
        expCounter = 0;

        for(var o : ctx.output()) {
            expCounter++;
            visitOutput(o);
        }
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("print " + expCounter);

        return null;
    }

    @Override public MyObject visitOutput(gParser.OutputContext ctx) {
        var s = ctx.STRING();

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + s);

        if(ctx.exp(0) != null) {
            var mo = visit(ctx.exp(0));

            if(mo != null) {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("push "+ mo.type + " " + mo.value);
            }
            expCounter++;
        }

        return null;
    }

    @Override public MyObject visitRead(gParser.ReadContext ctx) {
        for(var i : ctx.ID()) {
            if(vars.containsKey(i.toString())) {

                String type = vars.get(i.toString());
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("read " + type);
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("save " + i.toString());

            } else {
                // there should be called method from VerboseErrorListener
                System.out.println("Variable does not exists!");
            }
        }

//        return visitChildren(ctx);
        return null;
    }

    @Override public MyObject visitCondition(gParser.ConditionContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitElse(gParser.ElseContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitLoop(gParser.LoopContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitBlock(gParser.BlockContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitPar(gParser.ParContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitIdentifier(gParser.IdentifierContext ctx) {

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("load " + ctx.ID().getText() + " ");

        return visitChildren(ctx);
    }
    @Override public MyObject visitA(gParser.AContext ctx) { return visitChildren(ctx); }
    @Override public MyObject visitComp(gParser.CompContext ctx) { return visitChildren(ctx); }
    @Override public MyObject visitOr(gParser.OrContext ctx) { return visitChildren(ctx); }
    @Override public MyObject visitBool(gParser.BoolContext ctx) {
        MyObject mo = new MyObject();
        mo.type = BOOLEAN;
        mo.value = ctx.BOOL().toString();

        return mo;
    }
    @Override public MyObject visitString(gParser.StringContext ctx) {
        MyObject mo = new MyObject();
        mo.type = STRING;
        mo.value = ctx.STRING().toString();

        return mo;
    }
    @Override public MyObject visitMul(gParser.MulContext ctx) {



        return visitChildren(ctx);
    }
    @Override public MyObject visitConcat(gParser.ConcatContext ctx) {
        String firstString = ctx.getChild(0).toString();
        String secondString = ctx.getChild(2).toString();

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + firstString);
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + secondString);
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("concat");

        return visitChildren(ctx);
    }
    @Override public MyObject visitFloat(gParser.FloatContext ctx) {
        MyObject mo = new MyObject();
        mo.type = FLOAT;
        mo.value = ctx.FLOAT().getText();

        return mo;
    }
    @Override public MyObject visitInt(gParser.IntContext ctx) {
        MyObject mo = new MyObject();
        mo.type = INTEGER;
        mo.value = ctx.INT().getText();

        return mo;
    }
    @Override public MyObject visitNot(gParser.NotContext ctx) {

        return visitChildren(ctx);
    }
    @Override public MyObject visitAnd(gParser.AndContext ctx) {
        var leftSide = visit(ctx.exp().get(0));
        var rightSide = visit(ctx.exp().get(1));

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push");

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("and");

        return visitChildren(ctx);
    }
    @Override public MyObject visitDatatype(gParser.DatatypeContext ctx) { return visitChildren(ctx); }
}
