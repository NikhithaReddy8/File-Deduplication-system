import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class FileDeduplicator {
    // Stores file hashes and their corresponding file paths
    private static final Map<String, List<Path>> hashToFileMap = new HashMap<>();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter directory path: ");
        String inputPath = s.nextLine();
        
        Path dirPath = Paths.get(inputPath);
        if (!Files.isDirectory(dirPath)) {
            System.out.println("Invalid directory path. Please enter a valid directory.");
            return;
        }

        // Scan all files in the folder and find duplicates
        scanFiles(dirPath);

        // Show duplicate files and ask the user what to do
        processDuplicates();
    }

    // Goes through all files in the folder and generates a unique hash for each file
    private static void scanFiles(Path dir) throws IOException, NoSuchAlgorithmException {
        Files.walk(dir)
             .filter(Files::isRegularFile) // Only consider files, ignore folders
             .forEach(file -> {
                 try {
                     String hash = getFileHash(file); // Get file content hash
                     hashToFileMap.computeIfAbsent(hash, k -> new ArrayList<>()).add(file);
                 } catch (Exception e) {
                     System.err.println("Error processing file: " + file + " - " + e.getMessage());
                 }
             });
    }

    // Reads a file and creates a unique SHA-256 hash based on its content
    private static String getFileHash(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Read the file and update the hash value
        try (InputStream fis = Files.newInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, digest)) {
            while (dis.read() != -1) {} // Read file completely
        }

        // Convert hash bytes into a readable format (hexadecimal)
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Finds duplicate files and asks the user how to handle them
    private static void processDuplicates() {
        for (Map.Entry<String, List<Path>> entry : hashToFileMap.entrySet()) {
            List<Path> duplicates = entry.getValue();
            if (duplicates.size() > 1) { // More than one file with the same content
                System.out.println("Duplicate files found:");
                for (int i = 0; i < duplicates.size(); i++) {
                    System.out.println("  " + (i == 0 ? "(Original) " : "(Duplicate) ") + duplicates.get(i));
                }

                // Ask the user what they want to do with duplicate files
                handleDuplicateFiles(duplicates);
            }
        }
    }

    // Allows the user to delete, move to backup, or skip duplicate files
    private static void handleDuplicateFiles(List<Path> duplicates) {
        Scanner s = new Scanner(System.in);
        System.out.println("Choose an action: (1) Delete duplicates, (2) Move to backup, (3) Skip");
        int choice = s.nextInt();
        s.nextLine();

        if (choice == 1) {
            // Delete all duplicate files, keeping only the first one
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
                Files.createDirectories(backupDir); // Create backup folder if it doesn’t exist
                for (int i = 1; i < duplicates.size(); i++) {
                    Path backupFile = backupDir.resolve(duplicates.get(i).getFileName());
                    Files.move(duplicates.get(i), backupFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved to backup: " + backupFile);
                }
            } catch (IOException e) {
                System.err.println("Failed to move duplicates to backup: " + e.getMessage());
            }
        } else {
            System.out.println("Skipping duplicates.");
        }
    }
}
