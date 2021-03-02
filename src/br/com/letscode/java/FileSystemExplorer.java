package br.com.letscode.java;

import static java.nio.file.Files.newDirectoryStream;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemExplorer {

    private final Path path;

    public FileSystemExplorer(Path path) {
        this.path = path;
        this.init();
    }

    private void init() {
        System.out.println("Analisando o caminho: " + path.toAbsolutePath().toString());
        System.out.println("Contido em: " + path.getParent().toAbsolutePath().toString());
    }

    public void printDirectoryContent() {
        try (var stream = newDirectoryStream(this.path)) {
            for (Path file : stream) {
                System.out.println(file.getFileName());
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void filterFilesWithExtension(String extension) {
        this.filterFilesWithExtension(this.path, extension);
    }

    private void filterFilesWithExtension(Path path, String extension) {
        File file = path.toFile();
        File[] matchingFiles = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File f = Paths.get(dir.getPath() + File.separator + name).toFile();
                if (f.isDirectory()) {
                    filterFilesWithExtension(Paths.get(f.getPath()), extension);
                }
                return name.endsWith(extension);
            }
        });
        if (matchingFiles == null) {
            System.out.println("Nenhum resultado encontrado");
            return;
        }
        for (File f : matchingFiles) {
            System.out.println(f.getParent()+": "+f.getName());
        }
    }
}
