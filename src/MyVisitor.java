public class MyVisitor extends gBaseVisitor<MyVisitor.MyObject>{
    static final String INTEGER = "I";
    static final String STRING = "S";
    static final String FLOAT = "F";
    static final String BOOLEAN = "B";

    StringBuffer generatedOutput = new StringBuffer();

    public class MyObject
    {
        public String type;
        public String value;
    };

    @Override public MyObject visitProgram(gParser.ProgramContext ctx) {

        for(var l : ctx.line()) {
            visit(l);
        }

        return visitChildren(ctx);
    }

    @Override public MyObject visitDeclaration(gParser.DeclarationContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitAssigment(gParser.AssigmentContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitPrint(gParser.PrintContext ctx) {
        for(var o : ctx.output()) {
            visitOutput(o);
        }

        return visitChildren(ctx);
    }

    @Override public MyObject visitOutput(gParser.OutputContext ctx) {
        var s = ctx.STRING();
        generatedOutput.append("push S " + s);

        if(ctx.exp(0) != null) {
            var mo = visit(ctx.exp(0));

            if(mo != null) {
                generatedOutput.append("push "+ mo.type + " " + mo.value);
//                System.out.println("push "+ mo.type + " " + mo.value);
            }
        }

        return visitChildren(ctx);
    }

    @Override public MyObject visitRead(gParser.ReadContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitCondition(gParser.ConditionContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitElse(gParser.ElseContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitLoop(gParser.LoopContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitBlock(gParser.BlockContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitPar(gParser.ParContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitIdentifier(gParser.IdentifierContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitA(gParser.AContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitComp(gParser.CompContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitOr(gParser.OrContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitBool(gParser.BoolContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitString(gParser.StringContext ctx) {
        MyObject mo = new MyObject();
        mo.type = STRING;
        mo.value = ctx.STRING().toString();
        System.out.println(ctx.STRING());

        return mo;
    }

    @Override public MyObject visitMul(gParser.MulContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitConcat(gParser.ConcatContext ctx) { return visitChildren(ctx); }

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

    @Override public MyObject visitNot(gParser.NotContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitAnd(gParser.AndContext ctx) { return visitChildren(ctx); }

    @Override public MyObject visitDatatype(gParser.DatatypeContext ctx) { return visitChildren(ctx); }
}
