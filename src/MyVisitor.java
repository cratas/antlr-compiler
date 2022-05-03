import java.util.HashMap;
import java.util.Map;

public class MyVisitor extends gBaseVisitor<MyObject> {
    int labelCounter = 0;
    static final String INTEGER = "I";
    static final String STRING = "S";
    static final String FLOAT = "F";
    static final String BOOLEAN = "B";
    static final String ERROR_TYPE = "-";
    Map<String, String> vars = new HashMap<>();
    public static StringBuilder generatedOutput = new StringBuilder();
    int expCounter = 0;

    VerboseErrorListener errorListener = new VerboseErrorListener();
    @Override
    public MyObject visitProgram(gParser.ProgramContext ctx) {
        MyObject mo = new MyObject();

        // iterating over all lines in code
        for (var l : ctx.line()) {
                visit(l);
        }
        return mo;
    }
    @Override
    public MyObject visitDeclaration(gParser.DeclarationContext ctx) {
        MyObject mo = new MyObject();

        // return if node has no childs
        if (ctx.datatype() == null)
            return mo;

        // getting datatype of declaration
        var dataType = ctx.datatype().children.get(0).toString();

        // iterate over all new variables and set proper datatype
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
//                    errorListener.unknownDataType(dataType);
//                    System.exit(0);
            }

            // output of instructions
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + mo.type + " " + mo.value);
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("save " + i.toString());

            // sending variable into stack
            vars.put(i.toString(), mo.type);
        }

        return mo;
    }

    @Override public MyObject visitAssigment(gParser.AssigmentContext ctx) {
        MyObject mo = new MyObject();

        // checking if node as exp
        if(ctx.exp() != null) {
            mo = visit(ctx.exp());
            boolean isNegative = false;

            // if type is null, set it to error type
            if(mo.type == null) {
                mo.type = ERROR_TYPE;
            }

            // checking if variable is INTEGER
            if((mo.type.equals("I")) && (mo.value != null)) {
                int intValue;

                try {
                    intValue = Integer.parseInt(mo.value);
                } catch (Exception e) {
                    intValue = 1;
                }

                isNegative = intValue < 0;
                if(isNegative) {
                    intValue = intValue*-1;
                    mo.value = Integer.toString(intValue);
                }
            }

            // checking if variable is FLOAT
            if((mo.type.equals("F")) && (mo.value != null)) {
                float floatValue;
                try {
                    floatValue = Float.parseFloat(mo.value);
                } catch (Exception e) {
                    floatValue = 1;
                }

                isNegative = floatValue < 0;
                if(isNegative) {
                    floatValue = floatValue*-1;
                    mo.value = Float.toString(floatValue);
                }
            }

            // if variable has proper datatype print push instruction
            if(mo.type != ERROR_TYPE) {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("push " + mo.type + " " + mo.value);
            }

            // if variable is negative print uminus instraction
            if(isNegative) {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("uminus");
            }

            return mo;
        } else {
            // checking if variable is defined
            if(vars.containsKey(ctx.ID().getText())) {

                // recursively get values from next variable
                mo = visitAssigment(ctx.assigment());
                boolean isItof = false;

                // getting datatype from variables dict
                var searchType = vars.get(ctx.getChild(0).toString());
                if (mo.type != null && mo.type.equals("I") && searchType == "F") {
                    mo.type = "I";
                    isItof = true;
                }

                // if variable was converted, print itof instruction
                if (isItof) {
                    MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                    MyVisitor.generatedOutput.append("itof");
                }

                // print output with instructions
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("save " + ctx.ID().getText());
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("load " + ctx.ID().getText());
            } else {
                errorListener.undefinedVariable(ctx.ID().getText());
                System.exit(0);
            }
        }

        // if variables is last of assigment, print pop instruction
        if(!(ctx.getParent().getClass() == ctx.assigment().getClass())) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("pop");
        }

        return mo;
    }

    @Override public MyObject visitPrint(gParser.PrintContext ctx) {
        MyObject mo = new MyObject();

        expCounter = 0;

        // iterate over all outputs in print
        for(var o : ctx.output()) {
            expCounter++;
            visitOutput(o);
        }

        // print instructions output
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("print " + expCounter);

        return mo;
    }

    @Override public MyObject visitOutput(gParser.OutputContext ctx) {
        MyObject mo = new MyObject();

        // getting string from output
        var s = ctx.STRING();

        // printing push instruction
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + s);

        // checking if output contains some exps
        if(ctx.exp(0) != null) {
            // visit exp node nad get data
            mo = visit(ctx.exp(0));

            // if type of node is not null print push instruction
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

        // iterate over all variables in read
        for(var i : ctx.ID()) {
            // checking if variable is defined
            if(vars.containsKey(i.toString())) {
                String type = vars.get(i.toString());
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("read " + type);
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("save " + i);
            } else {
                errorListener.undefinedVariable(i.getText());
                System.exit(0);
            }
        }

        return mo;
    }
    @Override public MyObject visitCondition(gParser.ConditionContext ctx) {
        MyObject mo = new MyObject();

        // visit exp node
        var condition = visit(ctx.exp());
        // printing result of condition node
        if(condition.type != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + condition.type + " " + condition.value);
        }

        // printing jump instruction
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("fjmp " + labelCounter);

        boolean isFirstCondition = true;

        // visiting block in condition
        visitBlock(ctx.block());

        // checking if condition contains else block
        if(ctx.elseBlock() != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("jmp " + (labelCounter+1));
        }

        // printing label info
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("label " + labelCounter);

        // checking if there is another else block
        if(ctx.elseBlock() != null) {
            // iterate over all else blocks
            for(var c : ctx.elseBlock()) {
                // checking if its first condition iteration
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

        // if block is not null, visit
        if(ctx.block() != null) {
            return visitBlock(ctx.block());
        }

        return mo;
    }
    @Override public MyObject visitLoop(gParser.LoopContext ctx) {
        MyObject mo = new MyObject();

        // print label info
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("label " + labelCounter);

        // visit condition
        visit(ctx.exp());

        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("fjmp " + (labelCounter+1));

        // visit loop block
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

        // printing load instruction with variable name
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("load " + ctx.ID().getText() + " ");

        return mo;
    }
    @Override public MyObject visitComp(gParser.CompContext ctx) {
        MyObject mo = new MyObject();

        // getting values to compare
        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        // if left value is not null print push instruction with info
        if (leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());

            // checking convertion between int and float
            if (leftSide.type == "I" && rightSide.type == "F") {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("itof");
            }
        }

        // if right value is not null print push instruction with info
        if (rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());

            // checking convertion between int and float
            if (leftSide.type == "F" && rightSide.type == "I") {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("itof");
            }
        }

        // checking type of compare
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

        // getting left and right side of logical or
        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        // if left value is not null print push instruction with info
        if (leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }
        // if right value is not null print push instruction with info
        if (rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }

        // printing or instraction
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("or");

        return mo;
    }

    // method for visit bool value
    @Override public MyObject visitBool(gParser.BoolContext ctx) {
        MyObject mo = new MyObject();
        mo.type = BOOLEAN;
        mo.value = ctx.BOOL().toString();

        return mo;
    }

    // method for visit string value
    @Override public MyObject visitString(gParser.StringContext ctx) {
        MyObject mo = new MyObject();
        mo.type = STRING;
        mo.value = ctx.STRING().toString();

        return mo;
    }

    @Override public MyObject visitMul(gParser.MulContext ctx) {
        MyObject mo = new MyObject();

        // getting left and right side of operation
        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        // checking if left side is not null, otherwise print push instruction with info
        if (leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }
        // checking if right side is not null, otherwise print push instruction with info
        if (rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }
        // checking convertion between values
        if (leftSide.type != null && rightSide.type != null) {
            if ((leftSide.type.equals("I") && rightSide.equals("F") ) || (leftSide.type.equals("F") && rightSide.type.equals("I"))) {
                MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
                MyVisitor.generatedOutput.append("itof");
            }
        }

        // checking type of operation
        if (ctx.op.getText().equals("*")) {
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

        // getting left and right string to concat
        String firstString = ctx.getChild(0).toString();
        String secondString = ctx.getChild(2).toString();

        // printing instructions with info
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + firstString);
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("push S " + secondString);
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("concat");

        return mo;
    }

    // method for visit float value
    @Override public MyObject visitFloat(gParser.FloatContext ctx) {
        MyObject mo = new MyObject();
        mo.type = FLOAT;
        mo.value = ctx.FLOAT().getText();

        return mo;
    }

    // method for visit integer value
    @Override public MyObject visitInt(gParser.IntContext ctx) {
        MyObject mo = new MyObject();
        mo.type = INTEGER;
        mo.value = ctx.INT().getText();

        return mo;
    }
    @Override public MyObject visitNot(gParser.NotContext ctx) {
        MyObject mo = new MyObject();

        // visiting expretion to negate and print not instruction
        visit(ctx.exp());
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("not");

        return mo;
    }
    @Override public MyObject visitAnd(gParser.AndContext ctx) {
        MyObject mo = new MyObject();

        // getting left and right side of logical and operation
        var leftSide = visit(ctx.exp().get(0));
        var rightSide = visit(ctx.exp().get(1));

        // checking if left side is not null, otherwise print push instruction with info
        if (leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }
        // checking if right side is not null, otherwise print push instruction with info
        if (rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }

        // printing instruction and
        MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
        MyVisitor.generatedOutput.append("and");

        return mo;
    }
    @Override public MyObject visitA(gParser.AContext ctx) {
        MyObject mo = new MyObject();

        // getting left and right side of operation
        var leftSide = visit(ctx.exp(0));
        var rightSide = visit(ctx.exp(1));

        // checking if left side is not null, otherwise print push instruction with info
        if (leftSide.type != null && leftSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + leftSide.type + " " + leftSide.value.toString());
        }
        // checking if right side is not null, otherwise print push instruction with info
        if (rightSide.type != null && rightSide.value != null) {
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("push " + rightSide.type + " " + rightSide.value.toString());
        }

        // checking type of operation and print add or sub instruction
        if (ctx.op.getText().equals("+")) {
            mo.value = leftSide.toString() + rightSide.toString() + "ADD\n";
            MyVisitor.generatedOutput.append(System.getProperty("line.separator"));
            MyVisitor.generatedOutput.append("add");
        } else if(ctx.op.getText().equals(("-"))) {
            mo.value = leftSide.toString() + rightSide.toString() + "SUB\n";
            MyVisitor.generatedOutput.append("sub");
        }

        return mo;
    }
}
