/* Alunos:
Mihael Rommel B. Xavier RA: 10239617
Lucas Akio RA: 10425346
Kleber Gadelha Ponte Souza Filho RA: 10321335
Caio Guilherme dos Santos Silva RA: 10420097
*/

import java.util.Scanner;

public class AvaliadorDeExpressao {
    private static final int MAX_VARS = 26;

    private static char[] variaveis = new char[MAX_VARS];
    private static double[] valores = new double[MAX_VARS];
    private static boolean[] estaoDefinidas = new boolean[MAX_VARS];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String entrada;

        while (true) {
            System.out.print("> ");
            entrada = scanner.nextLine().trim();

            try {
                if (entrada.equalsIgnoreCase("EXIT")) {
                    break;
                } else if (entrada.matches("[A-Za-z]\\s*=\\s*[-]?[0-9]+(\\.[0-9]+)?")) {
                    atribuirVariavel(entrada);
                } else if (entrada.equalsIgnoreCase("VARS")) {
                    mostrarVariaveis();
                } else if (entrada.equalsIgnoreCase("RESET")) {
                    resetarVariaveis();
                } else {
                    avaliarExpressao(entrada);
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void atribuirVariavel(String entrada) {
        char variavel = entrada.charAt(0);
        double valor;
        
        try {
            valor = Double.parseDouble(entrada.split("=")[1].trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Valor numérico inválido para a variável.");
        }

        int index = Character.toUpperCase(variavel) - 'A';
        variaveis[index] = Character.toUpperCase(variavel);
        valores[index] = valor;
        estaoDefinidas[index] = true;

        System.out.println(Character.toUpperCase(variavel) + " = " + valor);
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
    }

    private static void resetarVariaveis() {
        for (int i = 0; i < MAX_VARS; i++) {
            estaoDefinidas[i] = false;
        }
        System.out.println("Variáveis reiniciadas.");
    }

    private static void avaliarExpressao(String expressao) {
        try {
            if (expressao.isEmpty()) {
                throw new RuntimeException("Expressão vazia.");
            }

            if (!validarExpressaoInfixa(expressao)) {
                throw new RuntimeException("Expressão infixa inválida.");
            }

            String postfix = infixParaPosfixa(expressao);
            System.out.println("Expressão pós-fixa: " + postfix);
            double resultado = avaliarPosfixa(postfix);
            System.out.println(resultado);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static boolean validarExpressaoInfixa(String expressao) {
        if (!expressao.matches("[A-Za-z0-9+\\-*/^(). ]+")) {
            throw new RuntimeException("Caracteres inválidos na expressão.");
        }

        int balance = 0;
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance < 0) {
                    throw new RuntimeException("Parênteses desbalanceados.");
                }
            }
        }
        if (balance != 0) {
            throw new RuntimeException("Parênteses desbalanceados.");
        }

        if (expressao.matches(".*[+\\-*/^]$")) {
            throw new RuntimeException("Expressão não pode terminar com operador.");
        }

        return true;
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
                postfix.append(Character.toUpperCase(c));
            }
            else if (c == '(') {
                pilha.push(c);
            }
            else if (c == ')') {
                while (!pilha.isEmpty() && pilha.peek() != '(') {
                    postfix.append(pilha.pop());
                }
                if (pilha.isEmpty()) {
                    throw new RuntimeException("Parênteses desbalanceados.");
                }
                pilha.pop();
            }
            else if (isOperator(c)) {
                while (!pilha.isEmpty() && precedencia(pilha.peek()) >= precedencia(c)) {
                    postfix.append(pilha.pop());
                }
                pilha.push(c);
            } else if (Character.isDigit(c)) {
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    postfix.append(infix.charAt(i++));
                }
                i--;
                postfix.append(' ');
            }
        }

        while (!pilha.isEmpty()) {
            if (pilha.peek() == '(') {
                throw new RuntimeException("Parênteses desbalanceados.");
            }
            postfix.append(pilha.pop());
        }

        return postfix.toString().trim();
    }

    private static double avaliarPosfixa(String postfix) {
        DoubleStack pilha = new DoubleStack(MAX_VARS);
        StringBuilder numero = new StringBuilder();

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            if (Character.isLetter(c)) {
                int index = Character.toUpperCase(c) - 'A';
                if (estaoDefinidas[index]) {
                    pilha.push(valores[index]);
                } else {
                    throw new RuntimeException("Variável " + Character.toUpperCase(c) + " não definida.");
                }
            }
            else if (Character.isDigit(c) || c == '.') {
                numero.append(c);
            }
            else if (c == ' ' && numero.length() > 0) {
                pilha.push(Double.parseDouble(numero.toString()));
                numero.setLength(0);
            }
            else if (isOperator(c)) {
                if (pilha.isEmpty()) {
                    throw new RuntimeException("Operador '" + c + "' sem operandos suficientes.");
                }
                double b = pilha.pop();
                
                if (pilha.isEmpty()) {
                    throw new RuntimeException("Operador '" + c + "' sem operandos suficientes.");
                }
                double a = pilha.pop();
                
                try {
                    pilha.push(aplicarOperacao(a, b, c));
                } catch (ArithmeticException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

        if (numero.length() > 0) {
            pilha.push(Double.parseDouble(numero.toString()));
        }

        if (pilha.isEmpty()) {
            throw new RuntimeException("Expressão vazia.");
        }

        double resultado = pilha.pop();
        
        if (!pilha.isEmpty()) {
            throw new RuntimeException("Expressão malformada: operandos não utilizados.");
        }

        return resultado;
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

    private static double aplicarOperacao(double a, double b, char operador) {
        switch (operador) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Divisão por zero.");
                return a / b;
            case '^':
                return Math.pow(a, b);
            default:
                throw new RuntimeException("Operador inválido: " + operador);
        }
    }
}
