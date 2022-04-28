import javax.management.monitor.MonitorSettingException;
import java.util.HashMap;
import java.util.Map;

public class MyVisitor extends gBaseVisitor<MyVisitor.MyObject> {
    int labelCounter = 0;
    static final String INTEGER = "I";
    static final String STRING = "S";
    static final String FLOAT = "F";
    static final String BOOLEAN = "B";
    Map<String, String> vars = new HashMap<String, String>();
    public static StringBuilder generatedOutput = new StringBuilder();

    int expCounter = 0;

    public class MyObject {
        public String type;
        public String value;
    }
    @Override
    public MyObject visitProgram(gParser.ProgramContext ctx) {
        MyObject mo = new MyObject();

        for (var l : ctx.line()) {
            if(l.assigment() != null) {
                visitAssigment(l.assigment());
//                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
//                MyVisitor.generatedOutput.append("pop");

            } else
                visit(l);
        }
        return mo;
    }
    @Override
    public MyObject visitDeclaration(gParser.DeclarationContext ctx) {
        MyObject mo = new MyObject();

        // if there are no childs, return
        if (ctx.children.get(0) == null)
            return mo;

        var dataType = ctx.datatype().children.get(0).toString();

        for (var i : ctx.ID()) {
            switch (dataType) {
                case "int":
                    mo.type = INTEGER;
                    mo.value = "0";
                    break;
                case "string":
                    mo.type = STRING;
                    mo.value = "\"\"";
                    ;
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
            MyVisitor.generatedOutput.append("save " + i.toString());

            vars.put(i.toString(), mo.type);
        }

        return mo;
    }
    @Override
    public MyObject visitAssigment(gParser.AssigmentContext ctx) {
        MyObject mo = new MyObject();
        boolean isUminus = false;
        boolean isItof = false;

        if (ctx.exp() != null) {
            return visit(ctx.exp());
        }

        var value = visitAssigment(ctx.assigment());

        if (ctx.getChild(0) != null) {
            if (vars.containsKey(ctx.getChild(0).toString())) {

                String searchValue = null;

                if (ctx.getChild(2).getChild(0).getChild(0) != null) {
                    searchValue = ctx.getChild(2).getChild(0).getChild(0).getText();
                }
                var searchType = vars.get(ctx.getChild(0).toString());

                if((searchType.equals("I")) && (searchValue != null)) {
                    boolean isNegative;
                    int intValue;
                    try {
                        intValue = Integer.parseInt(searchValue);
                    } catch (Exception e) {
                        intValue = 1;
                    }
                    isNegative = intValue < 0;
                    if(isNegative) {
                        intValue = intValue*-1;
                        searchValue = Integer.toString(intValue);
                        isUminus = true;
                    }
                }

                if((searchType.equals("F")) && (searchValue != null)) {
                    boolean isNegative;
                    float floatValue;
                    try {
                        floatValue = Float.parseFloat(searchValue);
                    } catch (Exception e) {
                        floatValue = 1;
                    }
                    isNegative = floatValue < 0;
                    if(isNegative) {
                        floatValue = floatValue*-1;
                        searchValue = Float.toString(floatValue);
                        isUminus = true;
                    }
                }

                if (value.type != null && value.type.equals("I") && searchType == "F") {
                    searchType = "I";
                    isItof = true;
                }

                mo.type = searchType;
                mo.value = searchValue;

                if (mo.value != null && value.type != null) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("push " + searchType + " " + searchValue);
                }

                if (isItof) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("itof");
                }

                if (isUminus) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("uminus");
                }

    //            vars.put(vars.get(ctx.getChild(0).toString()), searchValue);

                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("save " + ctx.getChild(0).toString());
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("load " + ctx.getChild(0).toString());

                if(!(ctx.getParent().getClass().toString().equals(ctx.assigment().getClass().toString()))) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("pop");
                }

        } else {
            System.out.println("Undefined variable");
        }
    }

        return mo;
    }
    @Override public MyObject visitPrint(gParser.PrintContext ctx) {
        MyObject mo = new MyObject();

        expCounter = 0;

        for(var o : ctx.output()) {
            expCounter++;
            visitOutput(o);
        }
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("print " + expCounter);

        return mo;
    }

    @Override public MyObject visitOutput(gParser.OutputContext ctx) {
        MyObject mo = new MyObject();

        var s = ctx.STRING();

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + s);

        if(ctx.exp(0) != null) {
            mo = visit(ctx.exp(0));

            if(mo.type != null) {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("push "+ mo.type + " " + mo.value);
            }

            expCounter++;
        }

        return mo;
    }
    @Override public MyObject visitRead(gParser.ReadContext ctx) {
        MyObject mo = new MyObject();
        for(var i : ctx.ID()) {

            if(vars.containsKey(i.toString())) {

                String type = vars.get(i.toString());
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("read " + type);
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("save " + i);

            } else {
                // there should be called method from VerboseErrorListener
                System.out.println("Variable does not exists!");
            }
        }

        return mo;
    }
    @Override public MyObject visitCondition(gParser.ConditionContext ctx) {
        MyObject mo = new MyObject();

        var condition = visit(ctx.exp());
        if(condition.type != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + condition.type + " " + condition.value);
        }

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("fjmp " + labelCounter);

        boolean isFirstCondition = true;

        visitBlock(ctx.block());

        if(ctx.elseBlock() != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("jmp " + (labelCounter+1));
        }

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("label " + labelCounter);

        if(ctx.elseBlock() != null) {
            for(var c : ctx.elseBlock()) {
                if(!isFirstCondition) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("label " + ++labelCounter);
                }
                else
                    isFirstCondition = false;
                visitElseBlock(c);
            }
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("label " + ++labelCounter);
        }
        labelCounter++;

        return mo;
    }

    @Override public MyObject visitElseBlock(gParser.ElseBlockContext ctx) {
        MyObject mo = new MyObject();

        if(ctx.block() != null) {
            return visitBlock(ctx.block());
        }

        return mo;
    }
    @Override public MyObject visitLoop(gParser.LoopContext ctx) {
        MyObject mo = new MyObject();

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("label " + labelCounter);

        visit(ctx.exp());

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("fjmp " + (labelCounter+1));

        visitBlock(ctx.block());

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("jmp " + labelCounter);
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("label " + ++labelCounter);

        labelCounter++;
        return mo;
    }
    @Override public MyObject visitIdentifier(gParser.IdentifierContext ctx) {
        MyObject mo = new MyObject();

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("load " + ctx.ID().getText() + " ");

        return mo;
    }
    @Override public MyObject visitA(gParser.AContext ctx) {
        MyObject mo = new MyObject();

        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        if(leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }

        if(rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }

        if(ctx.op.getText().equals("+")) {
            mo.value = leftSide.toString() + rightSide.toString() + "ADD\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("add");
        } else if(ctx.op.getText().equals(("-"))) {
            mo.value = leftSide.toString() + rightSide.toString() + "SUB\n";
            MyVisitor.generatedOutput.append("sub");
        }

//        if(leftSide.type == FLOAT || rightSide.type == FLOAT) {
//            mo.type = FLOAT;
//        } else {
//            mo.type = INTEGER;
//        }

        return mo;
    }
    @Override public MyObject visitComp(gParser.CompContext ctx) {
        MyObject mo = new MyObject();

        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        if (leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());

            if (leftSide.type == "I" && rightSide.type == "F") {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("itof");
            }
        }

        if (rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());

            if (leftSide.type == "F" && rightSide.type == "I") {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("itof");
            }
        }

        if (ctx.comp.getText().equals("<")) {
            mo.value = leftSide.toString() + rightSide.toString() + "LT\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("lt");
        } else if (ctx.comp.getText().equals(">")) {
            mo.value = leftSide.toString() + rightSide.toString() + "GT\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("gt");
        } else if (ctx.comp.getText().equals("==")) {
            mo.value = leftSide.toString() + rightSide.toString() + "EQ\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("eq");
        } else {
            mo.value = leftSide.toString() + rightSide.toString() + "EQ NOT\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("eq");
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("not");
        }


        return mo;
    }
    @Override public MyObject visitOr(gParser.OrContext ctx) {
        MyObject mo = new MyObject();

        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        if(leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }
        if(rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("or");

        return mo;
    }
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
        MyObject mo = new MyObject();

        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        if(leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }

        if(rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }

        if(leftSide.type != null && rightSide.type != null) {
            if((leftSide.type.equals("I") && rightSide.equals("F") ) || (leftSide.type.equals("F") && rightSide.type.equals("I"))) {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("itof");
            }
        }

        if(ctx.op.getText().equals("*")) {
            mo.value = leftSide.toString() + rightSide.toString() + "MUL\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("mul");
        } else if(ctx.op.getText().equals("%")) {
            mo.value = leftSide.toString() + rightSide.toString() + "MOD\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("mod");
        } else if((ctx.op.getText().equals("/"))) {
            mo.value = leftSide.toString() + rightSide.toString() + "DIV\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("div");
        }

        return mo;
    }
    @Override public MyObject visitConcat(gParser.ConcatContext ctx) {
        MyObject mo = new MyObject();

        String firstString = ctx.getChild(0).toString();
        String secondString = ctx.getChild(2).toString();

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + firstString);
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + secondString);
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("concat");

        return mo;
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
        MyObject mo = new MyObject();

        visit(ctx.exp());
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("not");

        return mo;
    }
    @Override public MyObject visitAnd(gParser.AndContext ctx) {
        MyObject mo = new MyObject();
        var leftSide = visit(ctx.exp().get(0));
        var rightSide = visit(ctx.exp().get(1));

        if(leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }
        if(rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("and");

        return mo;
    }
}
