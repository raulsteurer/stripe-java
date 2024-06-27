package com.stripe;

import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

public class BranchCoverageUtil {
  private final static int BRANCHES_COUNT = 20;

  private final static Logger logger = Logger.getLogger(BranchCoverageUtil.class.getName());

  public static void writeDefault() {
    String filePath = "branchCoverage.txt";


    File file = new File(filePath);
    try {
      file.createNewFile();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to create to file: " + filePath, e);
    }


    try (FileOutputStream fos = new FileOutputStream(filePath)) {
      System.out.println("Writing to file: " + filePath);
      // Writing zeros to the file
      for (int i = 0; i < BRANCHES_COUNT; i++) {
        fos.write('0');
      }
      fos.flush();
      System.out.println("Successfully wrote to file: " + filePath);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to write to file: " + filePath, e);
    }
  }

  // This function is to be called in main code
  // For each branch you are supposed to call it with a different uid assigned to you
  public static void insertXAtIndex(int index) {
    String filePath = "branchCoverage.txt";

    try {
      Path path = Paths.get(filePath);
      if (Files.notExists(path)) {
        System.out.println("File does not exist.");
        return;
      }

      List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      StringBuilder content = new StringBuilder();
      for (String line : lines) {
        content.append(line);
      }

      if (index < 0 || index > content.length()) {
        //System.out.println("Index out of bounds.");
        return;
      }
      content.setCharAt(index, 'X');
      try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING)) {
        writer.write(content.toString());
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to read from or write to file: " + filePath, e);
    }
  }
}
