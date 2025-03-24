public class DoubleStack {
    private double[] pilhaArray;
    private int topo;

    public DoubleStack(int tamanho) {
        pilhaArray = new double[tamanho];
        topo = -1;
    }

    public void push(double valor) {
        pilhaArray[++topo] = valor;
    }

    public double pop() {
        return pilhaArray[topo--];
    }

    public boolean isEmpty() {
        return topo == -1;
    }
}
