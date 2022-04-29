import java.util.*;
import java.util.logging.Handler;

public class VirtualMachine {
    List<String> instructions;
    Map<String, String> vars = new HashMap<>();
    Stack<MyObject> stack;

    VirtualMachine(String instructionsBuffer) {
        this.stack = new Stack<MyObject>();
        instructions = Arrays.asList(instructionsBuffer.split("\\r?\\n"));
    }

    public void run() {
        for(var i : instructions) {

            String[] instructionLine;
            if(i.contains("push")) {
                instructionLine = i.split(" ", 3);
            } else {
                instructionLine = i.split(" ");
            }


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
                case "pop":
                    pop();
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
                default:
//                    System.out.println("Unknown instruction");
            }
        }
    }

    private void print(String countOfPrint) {
        int count = Integer.parseInt(countOfPrint);

        for(int i = 0; i < count; i++) {
            var o = stack.pop();
            System.out.println(o.value);
        }
    }

    private void push(String dataType, String value) {
        MyObject mo = new MyObject(dataType, value);
        stack.push(mo);
    }

    private void save(String var) {
        var o = stack.pop();
        vars.put(o.value, o.type);
    }

    private void load(String var) {
        if(vars.containsKey(var)) {
            stack.push(new MyObject(vars.get(var), var));
        }
    }

    private void pop() {
//        stack.pop();
    }

    private void uminus() {
        var o = stack.pop();

        if(o.type == "I") {
            int value = Integer.parseInt(o.value);
            value*=-1;
            stack.push(new MyObject(o.type, Integer.toString(value)));
        } else if(o.type == "F") {
            float value = Float.parseFloat(o.value);
            value*=-1;
            stack.push(new MyObject(o.type, Float.toString(value)));
        }
    }

    private void calculation(String operator) {

    }

    private void itof() {
        var o = stack.pop();

        if(o.type.equals("I")) {
            float value = Float.parseFloat(o.value);
            o.value = Float.toString(value);
            o.type = "F";
            stack.push(o);
        }
    }
}
