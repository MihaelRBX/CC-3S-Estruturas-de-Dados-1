public class Node {
    public int lineNumber;
    public String instruction;
    public Node next;

    public Node(int lineNumber, String instruction) {
        this.lineNumber = lineNumber;
        this.instruction = instruction;
        this.next = null;
    }

    @Override
    public String toString() {
        return lineNumber + " " + instruction;
    }
}
