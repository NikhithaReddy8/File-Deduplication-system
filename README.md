# 3Frames Assignment - Efficient File Deduplication System


## Overview

The Efficient File Deduplication System is a Java-based application designed to scan a specified directory for duplicate files based on their content. Instead of relying on file names, the system computes SHA-256 hashes to detect duplicate files. Users can choose to delete or back up the duplicates while preserving the original file. This ensures optimal storage usage and efficient file management.



## Features

  - **Content-Based Deduplication**: Uses SHA-256 hashing to identify duplicates based on file content.
  
  - **Efficient Scanning**: Utilizes Java's NIO package for fast file traversal and processing.
  
  - **Safe Handling of Duplicates**: Provides options to delete or move duplicates to a backup directory.
  
  - **User-Friendly Console Interaction**: Accepts user input to perform actions on detected duplicates.
  
  - **Supports Large Files**: Works efficiently with large datasets and directories.

## Technologies Used

  - Java (Core language for implementation)

  - Java NIO (New I/O) (For efficient file handling and traversal)

  - SHA-256 Hashing (For secure and reliable duplicate detection)

## Prerequisites

   Before running the application, ensure that you have:

  - Java Development Kit (JDK) installed (JDK 8 or later recommended)
  
  - Basic knowledge of command-line usage
  
  - A folder with files to scan for duplicates

## Installation & Setup

  1. Install Java

  - If Java is not already installed, download and install the latest JDK from Oracle or OpenJDK.

  2. Clone or Download the Project
    
           git clone <repository_link>
           cd EfficientFileDeduplicationSystem

  3. Compile the Java Program

          javac FileDeduplicator.java

  4. Run the Program

          java FileDeduplicator

## Usage

  - When prompted, enter the directory path to scan for duplicate files.

  - The system will compute SHA-256 hashes and detect duplicates.

  - If duplicates are found, the user can choose one of the following actions:

      - Delete duplicates (Retains the original, removes copies)
      
      - Move duplicates to a backup folder
      
      - Skip the duplicates

The program will process the user’s selection and display the results.

## Limitations & Future Enhancements

  - **Current Limitation**: Only supports local file storage (no cloud integration).

  - **Future Enhancements**:

     - Add support for cloud storage (Google Drive, AWS S3)
      
      - Develop a GUI for a better user experience
      
      - Improve performance for extremely large directories

## Conclusion

  This Efficient File Deduplication System provides a robust solution for identifying and managing duplicate files on a local system.
  With its hashing-based detection, safe handling options, and user-friendly interface, it ensures effective storage management. 
  Future improvements can further enhance its usability and scalability.


  










