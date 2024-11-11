// Aluno: Mihael Rommel Barbosa Xavier
//RA: 10239617

public class SimpleAssemblyList {
    private Node head;

    public Node getHead() {
        return head;
    }

    public SimpleAssemblyList() {
        this.head = null;
    }

    public void clear() {
        head = null;
    }    

    // Insere uma nova linha ou atualiza uma linha existente
    public void insertLine(int lineNumber, String instruction) {
        if (lineNumber < 0) {
            System.out.println("Erro: linha " + lineNumber + " inválida.");
            return;
        }
        insertLine(lineNumber, instruction, true);
    }

    public void insertLine(int lineNumber, String instruction, boolean showMessage) {
        Node newNode = new Node(lineNumber, instruction);

        // Insere no início se a lista estiver vazia ou se a linha é menor que o head
        if (head == null || lineNumber < head.lineNumber) {
            newNode.next = head;
            head = newNode;
            if (showMessage) System.out.println("Linha inserida:\n" + newNode);
            return;
        }

        Node current = head;
        while (current.next != null && current.next.lineNumber < lineNumber) {
            current = current.next;
        }

        // Atualiza a linha se o número já existir
        if (current.next != null && current.next.lineNumber == lineNumber) {
            String oldInstruction = current.next.instruction;
            current.next.instruction = instruction;
            if (showMessage) {
                System.out.println("Linha:\n" + oldInstruction);
                System.out.println("Atualizada para:\n" + current.next);
            }
        } else { 
            // Insere a nova linha
            newNode.next = current.next;
            current.next = newNode;
            if (showMessage) System.out.println("Linha inserida:\n" + newNode);
        }
    }

    // Deleta uma linha ou intervalo de linhas 
    public void deleteLine(int startLine, int endLine) {
        // Verifica se o intervalo é válido
        if (startLine > endLine) {
            System.out.println("Erro: intervalo inválido de linhas.");
            return;
        }
    
        boolean found = false; // Variável para rastrear se alguma linha foi removida
    
        // Remove do início da lista enquanto as linhas estiverem no intervalo
        while (head != null && head.lineNumber >= startLine && head.lineNumber <= endLine) {
            if (!found) {
                System.out.println("Linhas removidas:");
                found = true;
            }
            System.out.println(head);
            head = head.next;
        }
    
        // Percorre o restante da lista para remover nós dentro do intervalo
        Node current = head;
        while (current != null && current.next != null) {
            if (current.next.lineNumber >= startLine && current.next.lineNumber <= endLine) {
                if (!found) {
                    System.out.println("Linhas removidas:");
                    found = true;
                }
                System.out.println(current.next);
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
    
        // Mensagem de erro se nenhuma linha foi encontrada no intervalo
        if (!found) {
            if (startLine == endLine) {
                System.out.println("Erro: linha " + startLine + " inexistente.");
            } else {
                System.out.println("Erro: nenhuma linha encontrada no intervalo " + startLine + " a " + endLine + ".");
            }
        }
    }
    
    public void deleteLine(int lineNumber) {
        deleteLine(lineNumber, lineNumber);
    }    
        
    
    // Exibe todas as linhas, aguardando a cada 20 linhas para não causar erros
    public void displayLines() {
        if (head == null) {
            System.out.println("Código vazio.");
            return;
        }

        Node current = head;
        int lineCount = 0;
        while (current != null) {
            System.out.println(current);
            current = current.next;
            lineCount++;
            if (lineCount % 20 == 0) {
                System.out.println("Pressione Enter para continuar...");
                new java.util.Scanner(System.in).nextLine(); 
            }
        }
    }

    // Busca uma linha pelo número
    public Node findLine(int lineNumber) {
        Node current = head;
        while (current != null) {
            if (current.lineNumber == lineNumber) {
                return current;
            }
            current = current.next;
        }
        return null;
    }
}
