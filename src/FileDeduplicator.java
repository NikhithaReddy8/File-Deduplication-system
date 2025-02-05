import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class FileDeduplicator {
    private static final Map<String, List<Path>> hashToFileMap = new HashMap<>();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter directory path: ");
        String inputPath = scanner.nextLine();
        
        Path dirPath = Paths.get(inputPath);
        if (!Files.isDirectory(dirPath)) {
            System.out.println("Invalid directory path.");
            return;
        }

        scanFiles(dirPath);
        processDuplicates();
    }

    private static void scanFiles(Path dir) throws IOException, NoSuchAlgorithmException {
        Files.walk(dir)
             .filter(Files::isRegularFile)
             .forEach(file -> {
                 try {
                     String hash = getFileHash(file);
                     hashToFileMap.computeIfAbsent(hash, k -> new ArrayList<>()).add(file);
                 } catch (Exception e) {
                     System.err.println("Error processing file: " + file + " - " + e.getMessage());
                 }
             });
    }

    private static String getFileHash(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = Files.newInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, digest)) {
            while (dis.read() != -1) {}
        }
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    private static void processDuplicates() {
        for (Map.Entry<String, List<Path>> entry : hashToFileMap.entrySet()) {
            List<Path> duplicates = entry.getValue();
            if (duplicates.size() > 1) {
                System.out.println("Duplicate files detected:");
                for (int i = 0; i < duplicates.size(); i++) {
                    System.out.println("  " + (i == 0 ? "(Original) " : "(Duplicate) ") + duplicates.get(i));
                }
                handleDuplicateFiles(duplicates);
            }
        }
    }

    private static void handleDuplicateFiles(List<Path> duplicates) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an action: (1) Delete duplicates, (2) Move to backup, (3) Skip");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            for (int i = 1; i < duplicates.size(); i++) {
                try {
                    Files.delete(duplicates.get(i));
                    System.out.println("Deleted: " + duplicates.get(i));
                } catch (IOException e) {
                    System.err.println("Failed to delete: " + duplicates.get(i));
                }
            }
        } else if (choice == 2) {
            Path backupDir = duplicates.get(0).getParent().getParent().resolve("backup");
            try {
                Files.createDirectories(backupDir);
                for (int i = 1; i < duplicates.size(); i++) {
                    Path backupFile = backupDir.resolve(duplicates.get(i).getFileName());
                    Files.move(duplicates.get(i), backupFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved to backup: " + backupFile);
                }
            } catch (IOException e) {
                System.err.println("Failed to move duplicates to backup");
            }
        } else {
            System.out.println("Skipping duplicates.");
        }
    }
}
