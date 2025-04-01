/* Alunos:
Mihael Rommel B. Xavier RA: 10239617
Lucas Akio RA: 10425346
Kleber Gadelha Ponte Souza Filho RA: 10321335
Caio Guilherme dos Santos Silva RA: 10420097
*/
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
