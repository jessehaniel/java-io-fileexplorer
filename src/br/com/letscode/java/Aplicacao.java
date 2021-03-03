package br.com.letscode.java;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Aplicacao {

    private FileSystemExplorer fileSystemExplorer;

    public static void main(String[] args) {
        Aplicacao aplicacao = new Aplicacao();
        aplicacao.init();
    }

    private void init() {
        this.imprimirCabecalho();
        final var path = this.lerCaminhoParaExplorar();
        this.fileSystemExplorer = new FileSystemExplorer(path);
//        this.apresentarResumo();
//        this.buscarArquivosPorExtensao();
//        this.listFilesSortedByModificationDate();
        this.listFilesSortedBySize();
    }

    private void imprimirCabecalho() {
        System.out.println("##############################################");
        System.out.println("###### Navegação em Sistema de Arquivos ######");
    }

    private Path lerCaminhoParaExplorar() {
        System.out.println("Qual caminho você deseja explorar?");
        Scanner scanner = new Scanner(System.in);
        final var caminho = scanner.nextLine();
        return Paths.get(caminho);
    }

    private void apresentarResumo() {
        System.out.println("Possui a seguinte estrutura...");
        fileSystemExplorer.printDirectoryContent();
    }

    private void buscarArquivosPorExtensao() {
        System.out.println("Informe uma extensão para filtrar o conteúdo deste diretório. (Ex: pdf)");
        Scanner scanner = new Scanner(System.in);
        final var extension = scanner.nextLine();
        fileSystemExplorer.filterFilesWithExtension(extension);
    }

    private void listFilesSortedByModificationDate() {
        System.out.println("Conteúdo do diretório ordenado por Data de Modificação");
        fileSystemExplorer.listFilesSortedByModificationDate();
    }

    private void listFilesSortedBySize() {
        System.out.println("Conteúdo do diretório ordenado por Tamanho (MB)");
        try {
            fileSystemExplorer.listFilesSortedBySize();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
