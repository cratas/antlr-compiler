public class VirtualMachine {
    String instructions;

    VirtualMachine(String instructionsBuffer) {
        this.instructions = instructionsBuffer;
    }

    public void run() {
        System.out.println(this.instructions);
    }
}
