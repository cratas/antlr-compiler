import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VirtualMachine {
    List<String> instructions;
    Map<String, MyObject> vars = new HashMap<>();
    Map<Integer, Integer> labels = new HashMap<>();
    Stack<MyObject> stack;

    int currentBlock = 0;

    VirtualMachine(String instructionsBuffer) {
        this.stack = new Stack<MyObject>();

        //convert incoming string into list, split by space and remove empty elements
        instructions = Arrays.asList(instructionsBuffer.split("\\r?\\n"));
        instructions = instructions
                .stream()
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toList());
    }
    public void run() {
        // iterate over all lables and mark them
        for(; currentBlock < instructions.size(); currentBlock++) {
            String instruction = instructions.get(currentBlock);
            if (instruction.startsWith("label")) {
                label(instruction.split(" ")[1]);
            }
        }

        currentBlock = 0;

        // iterate over all lines in code
        for(; currentBlock < instructions.size(); currentBlock++ ) {

            String i = instructions.get(currentBlock);
            String[] instructionLine;

            // if instruction is push type, split into 3 elements, otherwise just two
            if(i.contains("push")) {
                instructionLine = i.split(" ", 3);
                instructionLine[2] = instructionLine[2].replace("\"", "");
            } else {
                instructionLine = i.split(" ");
            }

            // switch for choosing instruction type
            switch (instructionLine[0]) {
                case "print":
                    print(instructionLine[1]);
                    break;
                case "push":
                    push(instructionLine[1], instructionLine[2]);
                    break;
                case "load":
                    load(instructionLine[1]);
                    break;
                case "uminus":
                    uminus();
                    break;
                case "add":
                    calculation("+");
                    break;
                case "sub":
                    calculation("-");
                    break;
                case "mul":
                    calculation("*");
                    break;
                case "div":
                    calculation("/");
                    break;
                case "mod":
                    calculation("%");
                    break;
                case "itof":
                    itof();
                    break;
                case "concat":
                    concat();
                    break;
                case "read":
                    read(instructionLine[1]);
                    break;
                case "and":
                    logicalOperation("&&");
                    break;
                case "or":
                    logicalOperation("||");
                    break;
                case "not":
                    not();
                    break;
                case "gt":
                    compare(">");
                    break;
                case "eq":
                    compare("==");
                    break;
                case "lt":
                    compare("<");
                    break;
                case "save":
                    save(instructionLine[1]);
                    break;
                case "label":
                    label(instructionLine[1]);
                    break;
                case "fjmp":
                    fjmp(instructionLine[1]);
                    break;
                case "jmp":
                    jmp(instructionLine[1]);
                    break;
                default:
                    break;
//                    System.out.println("Unknown instruction");
            }
        }
    }

    // conditional jmp to another code label
    private void fjmp(String paramId) {
        int id = Integer.parseInt(paramId);

        var value = stack.pop();
        if(value.type.equals("B") && value.value.equals("false")) {
            if(labels.containsKey(id)) {
                currentBlock = labels.get(id);
            }
        }
    }

    // jump to another code label given by parameter
    private void jmp(String paramId) {
        int id = Integer.parseInt(paramId);

        if(labels.containsKey(id))
            currentBlock = labels.get(id);
        else
            throw new RuntimeException("Label not found.");
    }

    // method for adding new code label into labels structure
    private void label(String paramId) {
        int id = Integer.parseInt(paramId);

        labels.put(id, currentBlock);
    }
    private void compare(String operator) {
        MyObject mo = new MyObject();
        var rightSide = stack.pop();
        var leftSide = stack.pop();

        if(operator.equals("<")) {
            if((leftSide.type.equals("I") || leftSide.type.equals("F") && rightSide.type.equals("I") || rightSide.type.equals("F"))) {
                if(Float.parseFloat(leftSide.value) < Float.parseFloat(rightSide.value)) {
                    mo.value = Boolean.toString(true);
                } else {
                    mo.value = Boolean.toString(false);
                }
            }
        } else if(operator.equals(">")) {
            if((leftSide.type.equals("I") || leftSide.type.equals("F") && rightSide.type.equals("I") || rightSide.type.equals("F"))) {
                if(Float.parseFloat(leftSide.value) > Float.parseFloat(rightSide.value)) {
                    mo.value = Boolean.toString(true);
                } else {
                    mo.value = Boolean.toString(false);
                }
            }
        } else if(operator.equals("==")) {
            if(leftSide.value.equals(rightSide.value)) {
                mo.value = Boolean.toString(true);
            } else {
                mo.value = Boolean.toString(false);
            }
        }

        mo.type = "B";
        stack.push(mo);
    }

    private void logicalOperation(String operation) {
        MyObject mo = new MyObject();
        var rightSide = stack.pop();
        var leftSide = stack.pop();

        if(!rightSide.type.equals("B") || !leftSide.type.equals("B")) return;

        if(operation.equals("&&")) {
            boolean tmp = Boolean.parseBoolean(leftSide.value) && Boolean.parseBoolean(rightSide.value);
            mo.value = Boolean.toString(tmp);
        } else if(operation.equals("||")) {
            boolean tmp = Boolean.parseBoolean(leftSide.value) || Boolean.parseBoolean(rightSide.value);
            mo.value = Boolean.toString(tmp);
        }

        mo.type = "B";
        stack.push(mo);
    }
    private void print(String countOfPrint) {
        int count = Integer.parseInt(countOfPrint);

        for(int i = 0; i < count; i++) {
            var o = stack.pop();
            System.out.println(o.value);
        }
    }
    private void not() {
        MyObject mo = stack.pop();
        if (mo.type.equals("B")) {
            boolean tmp = Boolean.parseBoolean(mo.type);
            tmp = !tmp;
            mo.value = Boolean.toString(tmp);
            stack.push(mo);
        }
    }
    private void push(String dataType, String value) {
        MyObject mo = new MyObject(dataType, value);
        stack.push(mo);
    }
    private void save(String var) {
        var o = stack.pop();
        vars.put(var, o);
    }
    private void load(String var) {
        if(vars.containsKey(var)) {
            stack.push(vars.get(var));
        } else {
            throw new RuntimeException("Variable not found");
        }
    }

    private void read(String dataType) {
        MyObject mo = new MyObject();

        mo.type = dataType;

        System.out.println("Scanning " + dataType + " variable: ");
        Scanner in = new Scanner(System.in);
        mo.value = in.nextLine();
        stack.push(mo);
    }
    private void uminus() {
        var o = stack.pop();

        if(o.type.equals("I")) {
            int value = Integer.parseInt(o.value);
            value*=-1;
            stack.push(new MyObject(o.type, Integer.toString(value)));
        } else if(o.type.equals("F")) {
            float value = Float.parseFloat(o.value);
            value*=-1;
            stack.push(new MyObject(o.type, Float.toString(value)));
        }
    }
    private void calculation(String operator) {
        MyObject mo = new MyObject();
        var rightObject = stack.pop();
        var leftObject = stack.pop();
        float result = 0.00f;

        switch (operator) {
            case "+" -> result = Float.parseFloat(leftObject.value) + Float.parseFloat(rightObject.value);
            case "-" -> result = Float.parseFloat(leftObject.value) - Float.parseFloat(rightObject.value);
            case "*" -> result = Float.parseFloat(leftObject.value) * Float.parseFloat(rightObject.value);
            case "/" -> result = Float.parseFloat(leftObject.value) / Float.parseFloat(rightObject.value);
        }

        if(rightObject.type.equals("I") && leftObject.type.equals("I")) {
            if (operator.equals("%"))
                result = Integer.parseInt(leftObject.value) % Integer.parseInt(rightObject.value);
            mo.type = "I";
            mo.value = Integer.toString((int)result);
        } else {
            mo.type = "F";
            mo.value = Float.toString((float)result);
        }
        stack.push(mo);

    }
    private void itof() {
        var o = stack.pop();

        if(o.type.equals("I")) {
            float value = Float.parseFloat(o.value);
            o.value = Float.toString(value);
            stack.push(o);
        }
    }
    private void concat() {
        var second = stack.pop();
        var first = stack.pop();

        String concatedString = first.value + second.value;
        stack.push(new MyObject(first.type, concatedString));
    }
}
