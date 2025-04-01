/* Alunos:
Mihael Rommel B. Xavier RA: 10239617
Lucas Akio RA: 10425346
Kleber Gadelha Ponte Souza Filho RA: 10321335
Caio Guilherme dos Santos Silva RA: 10420097
*/
public class CharStack {
    private char[] pilhaArray;
    private int topo;

    public CharStack(int tamanho) {
        pilhaArray = new char[tamanho];
        topo = -1;
    }

    public void push(char valor) {
        pilhaArray[++topo] = valor;
    }

    public char pop() {
        return pilhaArray[topo--];
    }

    public char peek() {
        return pilhaArray[topo];
    }

    public boolean isEmpty() {
        return topo == -1;
    }
}
