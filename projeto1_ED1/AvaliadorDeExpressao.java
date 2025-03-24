//Aluno: Mihael Rommel B. Xavier
//RA: 10239617


import java.util.Scanner;

public class AvaliadorDeExpressao {
    private static final int MAX_VARS = 26;
    private static final int MAX_COMMANDS = 10;

    private static char[] variaveis = new char[MAX_VARS];
    private static double[] valores = new double[MAX_VARS];
    private static boolean[] estaoDefinidas = new boolean[MAX_VARS];
    private static String[] filaDeComandos = new String[MAX_COMMANDS];
    private static int contagemDeComandos = 0;
    private static boolean gravando = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String entrada;

        while (true) {
            System.out.print("> ");
            entrada = scanner.nextLine().trim();

            if (entrada.equalsIgnoreCase("SAIR")) {
                break;
            } else if (entrada.matches("[A-Z]\\s*=\\s*[-]?[0-9]+(\\.[0-9]+)?")) {
                atribuirVariavel(entrada);
            } else if (entrada.equalsIgnoreCase("VARS")) {
                mostrarVariaveis();
            } else if (entrada.equalsIgnoreCase("RESET")) {
                resetarVariaveis();
            } else if (entrada.equalsIgnoreCase("REC")) {
                iniciarGravacao();
            } else if (entrada.equalsIgnoreCase("STOP")) {
                pararGravacao();
            } else if (entrada.equalsIgnoreCase("PLAY")) {
                reproduzirComandos();
            } else if (entrada.equalsIgnoreCase("ERASE")) {
                apagarComandos();
            } else {
                avaliarExpressao(entrada);
            }
        }

        scanner.close();
    }

    private static void atribuirVariavel(String entrada) {
        char variavel = entrada.charAt(0);
        double valor = Double.parseDouble(entrada.split("=")[1].trim());

        int index = variavel - 'A';
        variaveis[index] = variavel;
        valores[index] = valor;
        estaoDefinidas[index] = true;

        System.out.println(variavel + " = " + valor);
        if (gravando) {
            adicionarComando(entrada);
        }
    }

    private static void mostrarVariaveis() {
        boolean temVars = false;
        for (int i = 0; i < MAX_VARS; i++) {
            if (estaoDefinidas[i]) {
                System.out.println(variaveis[i] + " = " + valores[i]);
                temVars = true;
            }
        }
        if (!temVars) {
            System.out.println("Nenhuma variável definida.");
        }
        if (gravando) {
            adicionarComando("VARS");
        }
    }

    private static void resetarVariaveis() {
        for (int i = 0; i < MAX_VARS; i++) {
            estaoDefinidas[i] = false;
        }
        System.out.println("Variáveis reiniciadas.");
        if (gravando) {
            adicionarComando("RESET");
        }
    }

    private static void iniciarGravacao() {
        gravando = true;
        System.out.println("Iniciando gravação...");
        contagemDeComandos = 0;
    }

    private static void pararGravacao() {
        gravando = false;
        System.out.println("Encerrando gravação...");
    }

    private static void reproduzirComandos() {
        if (contagemDeComandos == 0) {
            System.out.println("Nenhum comando para reproduzir.");
        } else {
            System.out.println("Reproduzindo gravação...");
            for (int i = 0; i < contagemDeComandos; i++) {
                System.out.println("> " + filaDeComandos[i]);
                if (filaDeComandos[i].matches("[A-Z] = [-]?[0-9]+(\\.[0-9]+)?")) {
                    atribuirVariavel(filaDeComandos[i]);
                } else if (filaDeComandos[i].equalsIgnoreCase("VARS")) {
                    mostrarVariaveis();
                } else if (filaDeComandos[i].equalsIgnoreCase("RESET")) {
                    resetarVariaveis();
                } else {
                    avaliarExpressao(filaDeComandos[i]);
                }
            }
        }
    }

    private static void apagarComandos() {
        contagemDeComandos = 0;
        System.out.println("Gravação apagada.");
    }

    private static void adicionarComando(String comando) {
        if (contagemDeComandos < MAX_COMMANDS) {
            filaDeComandos[contagemDeComandos++] = comando;
        } else {
            System.out.println("Limite de comandos atingido.");
            pararGravacao();
        }
    }

    private static void avaliarExpressao(String expressao) {
        try {
            if (!expressao.matches("[A-Z0-9+\\-*/^(). ]+")) {
                throw new RuntimeException("Erro: caracteres inválidos na expressão.");
            }

            String postfix = infixParaPosfixa(expressao);
            System.out.println("Expressão pós-fixa: " + postfix); 
            double resultado = avaliarPosfixa(postfix);
            System.out.println(resultado);
            if (gravando) {
                adicionarComando(expressao);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static String infixParaPosfixa(String infix) {
        StringBuilder postfix = new StringBuilder();
        CharStack pilha = new CharStack(MAX_VARS);

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (c == ' ') {
                continue;
            }

            if (Character.isLetter(c)) {
                postfix.append(c);
            }
            else if (c == '(') {
                pilha.push(c);
            }

            else if (c == ')') {
                while (!pilha.isEmpty() && pilha.peek() != '(') {
                    postfix.append(pilha.pop());
                }
                pilha.pop(); 
            }
            else if (isOperator(c)) {
                while (!pilha.isEmpty() && precedencia(pilha.peek()) >= precedencia(c)) {
                    postfix.append(pilha.pop());
                }
                pilha.push(c);
            } else {
                throw new RuntimeException("Erro: caractere inválido na expressão.");
            }
        }

        while (!pilha.isEmpty()) {
            postfix.append(pilha.pop());
        }

        return postfix.toString();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedencia(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    private static double avaliarPosfixa(String postfix) {
        DoubleStack pilha = new DoubleStack(MAX_VARS);

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            if (Character.isLetter(c)) {
                int index = c - 'A';
                if (estaoDefinidas[index]) {
                    pilha.push(valores[index]);
                } else {
                    throw new RuntimeException("Erro: variável " + c + " não definida.");
                }
            }
            else if (isOperator(c)) {
                double b = pilha.pop();
                double a = pilha.pop();
                pilha.push(aplicarOperacao(a, b, c));
            } else {
                throw new RuntimeException("Erro: caractere inválido na expressão pós-fixa.");
            }
        }


        return pilha.pop();
    }

    
    private static double aplicarOperacao(double a, double b, char operador) {
        switch (operador) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            case '^':
                return Math.pow(a, b);
            default:
                throw new RuntimeException("Operador inválido: " + operador);
        }
    }
}
