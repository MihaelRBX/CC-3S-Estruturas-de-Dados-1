// Aluno: Mihael Rommel Barbosa Xavier
//RA: 10239617

/* Referências:
 * Material de aula (Mais especificamente sobre Nós e Linked Lists) dos professores Leonardo Takuno e André Kishimoto (disponível no moodle da matéria)
 * Documentacão Java: https://docs.oracle.com/en/java/
 * Entendendo Parse: https://youtu.be/pf-ZKwsCN9U?si=w0xzyNXCBHUwjqjX
 * Entendendo conceitos de Exception Handling: https://youtu.be/1XAfapkBQjk?si=riyPlE6sIMTe8ujQ
 * Java.io :https://youtu.be/ScUJx4aWRi0?si=Ke8sqn3SAxwKUjQj
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
    private static String currentFileName = ""; // Armazena o nome do arquivo atual
    private static boolean hasUnsavedChanges = false; // Indica se há alterações não salvas

    // Lógica principal da Main (interpretando qual método tratar)
    public static void main(String[] args) {
        SimpleAssemblyList assemblyList = new SimpleAssemblyList();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.print(">> ");
            String input = scanner.nextLine().trim();

            if (input.startsWith("LOAD")) {
                handleLoad(input.substring(5).trim(), assemblyList);
                continue;
            }

            String[] commandParts = input.split(" ", 3);
            String command = commandParts[0].toUpperCase();

            switch (command) {
                case "LIST":
                    assemblyList.displayLines();
                    break;
                case "RUN":
                    AssemblyInterpreter interpreter = new AssemblyInterpreter(assemblyList);
                    interpreter.run();
                    break;
                case "INS":
                    handleInsert(commandParts, assemblyList);
                    break;
                case "DEL":
                    handleDelete(commandParts, assemblyList);
                    break;
                case "SAVE":
                    handleSave(assemblyList);
                    break;
                case "EXIT":
                    exit = confirmExit(scanner, assemblyList);
                    break;
                default:
                    System.out.println("Comando não reconhecido.");
                    break;
            }
        }
        
        scanner.close();
        System.out.println("Programa encerrado.");
    }

    // Manuseio do método LOAD
    private static void handleLoad(String fileName, SimpleAssemblyList assemblyList) {
        fileName = fileName.replace("\"", "");
        File file = new File(fileName);

        try (Scanner fileScanner = new Scanner(file)) {
            assemblyList.clear(); // Limpa a lista antes de carregar o novo arquivo
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                String[] lineParts = line.split(" ", 2);
                int lineNumber = Integer.parseInt(lineParts[0]);
                String instruction = lineParts[1];
                assemblyList.insertLine(lineNumber, instruction, false);
            }
            currentFileName = fileName;
            hasUnsavedChanges = false;
            System.out.println("Arquivo '" + fileName + "' carregado com sucesso.");
        } catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo não encontrado - " + fileName);
        } catch (Exception e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }

    // Manuseio do método INS
    private static void handleInsert(String[] commandParts, SimpleAssemblyList assemblyList) {
        if (commandParts.length < 3) {
            System.out.println("Erro: Formato do comando incorreto. Use: INS <LINHA> <INSTRUÇÃO>");
            return;
        }
        try {
            int lineNumber = Integer.parseInt(commandParts[1]);
            String instruction = commandParts[2];
            assemblyList.insertLine(lineNumber, instruction);
            hasUnsavedChanges = true;
        } catch (NumberFormatException e) {
            System.out.println("Erro: O número da linha deve ser um inteiro.");
        }
    }

    // Manuseio do metodo DEL
    private static void handleDelete(String[] commandParts, SimpleAssemblyList assemblyList) {
        if (commandParts.length < 2) {
            System.out.println("Erro: Especifique a linha ou o intervalo de linhas para deletar.");
            return;
        }
        
        try {
            int startLine = Integer.parseInt(commandParts[1]);
            
            if (commandParts.length == 2) {
                // Se houver apenas um número, delete a linha especificada 
                assemblyList.deleteLine(startLine);
            } else if (commandParts.length == 3) {
                // Se houver dois números, delete o intervalo
                int endLine = Integer.parseInt(commandParts[2]);
                assemblyList.deleteLine(startLine, endLine);
            } else {
                System.out.println("Erro: Formato inválido para o comando DEL.");
            }
            hasUnsavedChanges = true;
        } catch (NumberFormatException e) {
            System.out.println("Erro: Números de linha inválidos.");
        }
    }    

    // Manuseio do metodo SAVE
    private static void handleSave(SimpleAssemblyList assemblyList) {
        if (currentFileName.isEmpty()) {
            System.out.println("Erro: Nenhum arquivo carregado para salvar.");
            return;
        }
        try (PrintWriter writer = new PrintWriter(currentFileName)) {
            System.out.println("Salvando o código em: " + currentFileName);
            Node current = assemblyList.getHead();
            while (current != null) {
                writer.println(current.lineNumber + " " + current.instruction);
                current = current.next;
            }
            hasUnsavedChanges = false;
            System.out.println("Arquivo salvo com sucesso.");
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    // Verifica se a mudancas não salvas
    private static boolean confirmExit(Scanner scanner, SimpleAssemblyList assemblyList) {
        if (hasUnsavedChanges) {
            System.out.println("EXIT");
            System.out.println("Arquivo atual ('" + currentFileName + "') contém alterações não salvas.");
            System.out.print("Deseja salvar? (S/N): ");
            String response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("S")) {
                handleSave(assemblyList);
            }
        }
        return true;
    }
}
