# 3Frames Assignment - Efficient File Deduplication System


## Overview    
The Efficient File Deduplication System is a Java-based application designed to scan a specified directory for duplicate files based on their content.
Instead of relying on file names, the system computes SHA-256 hashes to detect duplicate files. 
Users can choose to delete or back up the duplicates while preserving the original file. This ensures optimal storage usage and efficient file management

## Features  
- **Content-Based Deduplication:** Uses SHA-256 hashing to identify duplicates based on file content.
- **Efficient Scanning:** Utilizes Java's NIO package for fast file traversal and processing.
- **Safe Handling of Duplicates:** Provides options to delete or move duplicates to a backup directory.
- **User-Friendly Console Interaction:** Accepts user input to perform actions on detected duplicates.
- **Supports Large Files:** Works efficiently with large datasets and directories.

## Technologies Used   
- Java (Core language for implementation)
- Java NIO (New I/O) (For efficient file handling and traversal)
- SHA-256 Hashing (For secure and reliable duplicate detection)
