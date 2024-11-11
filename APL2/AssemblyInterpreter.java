// Aluno: Mihael Rommel Barbosa Xavier
//RA: 10239617

public class AssemblyInterpreter {
    private SimpleAssemblyList codeList; // Lista de instruções de código
    private int[] registers; // registradores
    private boolean[] initialized; 

    public AssemblyInterpreter(SimpleAssemblyList codeList) {
        this.codeList = codeList;
        this.registers = new int[26]; // Registradores de A a Z
        this.initialized = new boolean[26]; // Rastrea quais registradores foram inicializados
    }

    // Método principal, executa as instrucões
    public void run() {
        Node current = codeList.getHead();
        while (current != null) {
            String[] parts = current.instruction.split(" ");
            current = executeInstruction(parts, current);
        }
    }

    private Node executeInstruction(String[] parts, Node node) {
        switch (parts[0].toLowerCase()) { // Identifica a instrução pelo primeiro termo
            case "mov":
                mov(parts); // Copia um valor para um registrador
                break;
            case "inc":
                inc(parts); // Incrementa o valor de um registrador
                break;
            case "dec":
                dec(parts); // Decrementa o valor de um registrador
                break;
            case "add":
                add(parts); // Adiciona um valor a um registrador
                break;
            case "sub":
                sub(parts); // Subtrai um valor de um registrador
                break;
            case "mul":
                mul(parts); // Multiplica o valor de um registrador por outro valor
                break;
            case "div":
                div(parts); // Divide o valor de um registrador por outro valor
                break;
            case "jnz":
                return jnz(parts, node); // Salto condicional para uma linha
            case "out":
                out(parts); // Exibe o valor de um registrador
                break;
            default:
                System.out.println("Erro: Instrução desconhecida - " + node.instruction);
        }
        return node.next; // Retorna o próximo nó para continuar a execução
    }


     // Copia o valor de uma fonte para um registrador
     private void mov(String[] parts) {
        int destIndex = parts[1].charAt(0) - 'a'; // Índice do registrador destino
        int value = parseOperand(parts[2]); // Valor a ser movido
        registers[destIndex] = value;
        initialized[destIndex] = true; // Marca o registrador como inicializado
    }

    // Incrementa o valor de um registrador
    private void inc(String[] parts) {
        int index = parts[1].charAt(0) - 'a';
        if (!initialized[index]) {
            System.out.println("Erro: registrador " + parts[1] + " não inicializado.");
            return;
        }
        registers[index]++;
    }

    // Decrementa o valor de um registrador
    private void dec(String[] parts) {
        int index = parts[1].charAt(0) - 'a';
        if (!initialized[index]) {
            System.out.println("Erro: registrador " + parts[1] + " não inicializado.");
            return;
        }
        registers[index]--;
    }

    // Adiciona um valor ao registrador
    private void add(String[] parts) {
        int index = parts[1].charAt(0) - 'a';
        int value = parseOperand(parts[2]);
        if (!initialized[index]) {
            System.out.println("Erro: registrador " + parts[1] + " não inicializado.");
            return;
        }
        registers[index] += value;
    }

    // Subtrai um valor do registrador
    private void sub(String[] parts) {
        int index = parts[1].charAt(0) - 'a';
        int value = parseOperand(parts[2]);
        if (!initialized[index]) {
            System.out.println("Erro: registrador " + parts[1] + " não inicializado.");
            return;
        }
        registers[index] -= value;
    }

    // Multiplica o valor de um registrador por outro valor
    private void mul(String[] parts) {
        int index = parts[1].charAt(0) - 'a';
        int value = parseOperand(parts[2]);
        if (!initialized[index]) {
            System.out.println("Erro: registrador " + parts[1] + " não inicializado.");
            return;
        }
        registers[index] *= value;
    }

    // Divide o valor de um registrador por outro valor
    private void div(String[] parts) {
        int index = parts[1].charAt(0) - 'a';
        int value = parseOperand(parts[2]);
        if (!initialized[index]) {
            System.out.println("Erro: registrador " + parts[1] + " não inicializado.");
            return;
        }
        if (value == 0) {
            System.out.println("Erro: divisão por zero no registrador " + parts[1] + ".");
            return;
        }
        registers[index] /= value;
    }

    // Salta para uma linha específica se o valor do registrador não for zero
    private Node jnz(String[] parts, Node node) {
        int regIndex = parts[1].charAt(0) - 'a';
        
        // Verifica se o registrador está inicializado e se não é zero
        if (!initialized[regIndex] || registers[regIndex] == 0) {
            return node.next;
        }
        
        int targetLine;
        
        // Tenta interpretar o segundo operando como um número ou como um registrador
        if (Character.isLetter(parts[2].charAt(0))) {
            int regTargetIndex = parts[2].charAt(0) - 'a';
            if (!initialized[regTargetIndex]) {
                System.out.println("Erro: registrador " + parts[2] + " não inicializado.");
                return node.next; // Se o registrador não estiver inicializado, continue
            }
            targetLine = registers[regTargetIndex];
        } else {
            try {
                targetLine = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Erro: instrução inválida em 'jnz " + parts[1] + " " + parts[2] + "'. O destino deve ser um número ou registrador.");
                return node.next;
            }
        }
        
        // Busca a linha alvo na lista de instruções
        Node current = codeList.getHead();
        while (current != null && current.lineNumber != targetLine) {
            current = current.next;
        }
        
        return current != null ? current : node.next; // Se a linha alvo não for encontrada, continua 
    }

    // Exibe o valor de um registrador
    private void out(String[] parts) {
        int index = parts[1].charAt(0) - 'a';
        if (!initialized[index]) {
            System.out.println("Erro: registrador " + parts[1] + " não inicializado.");
            return;
        }
        System.out.println(registers[index]);
    }

    // Verifica se o operando é uma letra (registrador) ou um valor inteiro
    private int parseOperand(String operand) {
        if (Character.isLetter(operand.charAt(0))) {
            int regIndex = operand.charAt(0) - 'a';
            if (!initialized[regIndex]) {
                System.out.println("Erro: registrador " + operand + " não inicializado.");
                return 0;
            }
            return registers[regIndex];
        } else {
            return Integer.parseInt(operand);
        }
    }
}