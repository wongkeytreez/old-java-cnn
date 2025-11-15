import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class file {
public static void writeArrayListToBinaryFile2d(String fileName, ArrayList<String> data) {
    try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
        dos.writeInt(data.size()); // Write the number of strings in the list
        for (String str : data) {
            dos.writeUTF(str); // Write each string using UTF encoding
        }
    } catch (IOException e) {
        System.err.println("Error writing binary file: " + e.getMessage());
    }
}
public static ArrayList<String> readArrayListFromBinaryFile2d(String fileName) {
    ArrayList<String> result = new ArrayList<>();

    try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
        int size = dis.readInt(); // Read the number of strings in the list
        for (int i = 0; i < size; i++) {
            result.add(dis.readUTF()); // Read each string and add it to the list
        }
    } catch (IOException e) {
        System.err.println("Error reading binary file: " + e.getMessage());
    }

    return result;
}

    public static void writeArrayListToBinaryFile(String fileName, ArrayList<ArrayList<double[][]>> data) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            dos.writeInt(data.size()); // Write the number of outer lists
            for (ArrayList<double[][]> outerList : data) {
                dos.writeInt(outerList.size()); // Write the number of arrays in the inner list
                for (double[][] array : outerList) {
                    dos.writeInt(array.length); // Number of rows
                    dos.writeInt(array[0].length); // Number of columns
                    for (double[] row : array) {
                        for (double value : row) {
                            dos.writeDouble(value); // Write the double values
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing binary file: " + e.getMessage());
        }
    }
    public static ArrayList<ArrayList<double[][]>> readArrayListFromBinaryFile(String fileName) {
        ArrayList<ArrayList<double[][]>> result = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            int outerSize = dis.readInt(); // Number of outer lists
            for (int i = 0; i < outerSize; i++) {
                ArrayList<double[][]> outerList = new ArrayList<>();
                int innerSize = dis.readInt(); // Number of arrays in the inner list
                for (int j = 0; j < innerSize; j++) {
                    int rows = dis.readInt(); // Number of rows
                    int cols = dis.readInt(); // Number of columns
                    double[][] array = new double[rows][cols];
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            array[r][c] = dis.readDouble(); // Read the double value
                        }
                    }
                    outerList.add(array); // Add the array to the inner list
                }
                result.add(outerList); // Add the inner list to the outer list
            }
        } catch (IOException e) {
            System.err.println("Error reading binary file: " + e.getMessage());
        }

        return result;
    }
    public static void write2DArrayToFile(String fileName, double[][] array) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (double[] row : array) {
                for (int i = 0; i < row.length; i++) {
                    bw.write(String.valueOf(row[i])); // Write each double value
                    if (i < row.length - 1) {
                        bw.write(" "); // Add space between values
                    }
                }
                bw.newLine(); // Move to the next line for the next row
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    public static double[][] read2DArrayFromFile(String fileName) {
        ArrayList<double[]> rows = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+"); // Split by spaces
                double[] row = new double[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Double.parseDouble(tokens[i]); // Parse as double
                }
                rows.add(row); // Add the row to the list
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // Convert ArrayList to 2D double array
        double[][] array = new double[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            array[i] = rows.get(i);
        }
        return array;
    }
     public static void saveToTxt(double[][] array, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (double[] row : array) {
                for (double value : row) {
                    writer.write(value + " "); // Separate values with a space
                }
                writer.write("\n"); // Newline for each row
            }
            System.out.println("File saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    public static BufferedImage drawBoundingBoxes(BufferedImage image, double[][] boundingBoxes) {
       // Create a copy of the original image to avoid modifying the original one
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = outputImage.createGraphics();

        // Draw the original image onto the new image
        g2d.drawImage(image, 0, 0, null);

        // Iterate through the bounding boxes and their corresponding confidence values
        for (int i = 0; i < boundingBoxes.length; i++) {

            for(int j=4;j<boundingBoxes[i].length;j++)
            if(boundingBoxes[i][j] > 0.5)

            // Check if the confidence is greater than 0.5
            {
                // Set the color and stroke for drawing the bounding box
                g2d.setColor(Color.RED);  // Set color to red for the bounding box
                g2d.setStroke(new BasicStroke(2));  // Set stroke width to 2
System.out.println(Math.sqrt(boundingBoxes.length));
                // Draw the bounding box on the image
                g2d.drawRect(
                    (int)  ((boundingBoxes[i][0]+(int)(i/Math.sqrt(boundingBoxes.length)))*(int)(100/Math.sqrt(boundingBoxes.length))), 
                    (int)   ((boundingBoxes[i][1]+(int)(i%Math.sqrt(boundingBoxes.length)))*(int)(100/Math.sqrt(boundingBoxes.length))),
                    (int) (boundingBoxes[i][2]*100),
                    (int) (boundingBoxes[i][3]*100));

                // Optionally, draw the confidence score on the bounding box
               
            }
        }

        // Dispose of the graphics object
        g2d.dispose();

        // Return the image with bounding boxes
        return outputImage;
    }
    public static String[] readFolderFolders(String folderPath) {
        // Specify the folder path
        
        
        // Create a File object
        File folder = new File(folderPath);
        ArrayList<String> returnlist= new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
      
            File[] files = folder.listFiles();
            
      
            if (files != null) {
                for (File file : files) 
                    if (file.isDirectory()) {
                      returnlist.add(file.getName()); 
                    }
            } else {
                System.out.println("The folder is empty.");
            }
        } else {
            System.out.println("The specified path is not a folder.");
        }
        return returnlist.toArray(new String[0] );
    }
public static BufferedImage createImageFromDoubleArray(double[][] data) {
    int width = data.length;
    int height = data[0].length;

    // Create a BufferedImage
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    // Set the pixel values directly
    for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
            int gray = (int) data[x][y]; // Assumes values are in the range [0, 255]
            gray = Math.max(0, Math.min(255, gray)); // Clamp values to ensure valid grayscale

            // Use gray value for R, G, and B to create grayscale color
            Color color = new Color(gray, gray, gray); 
            image.setRGB(x, y, color.getRGB());
        }
    }

    return image;
}


    public static BufferedImage makeSquare(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();

    // Determine the new dimensions for the square image
    int size = Math.max(width, height);

    // Create a new square image with a black background
    BufferedImage squareImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = squareImage.createGraphics();

    // Fill the square image with black
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, size, size);

    // Calculate the position to center the original image
    int xOffset = (size - width) / 2;
    int yOffset = (size - height) / 2;

    // Draw the original image onto the center of the square
    g2d.drawImage(image, xOffset, yOffset, null);
    g2d.dispose();

    return squareImage;
}
    // public static BufferedImage poolImage(BufferedImage image, int size) {
    
    // return newImage;
    // }
public static double[][] readImage(BufferedImage image) {
    double[][] newImage = new double[image.getWidth()][image.getHeight()];
    for (int x = 0; x < newImage.length; x++) {
        for (int y = 0; y < newImage[x].length; y++) {
            int rgb = image.getRGB(x, y);

            // Extract RGB components using bit shifts
            int r = (rgb >> 16) & 0xFF;  // Red component
            int g = (rgb >> 8) & 0xFF;   // Green component
            int b = rgb & 0xFF;          // Blue component

            // Convert to grayscale using luminance formula
            newImage[x][y] = r * 0.2126 + g * 0.7152 + b * 0.0722;
        }
    }
    return newImage;
}

public static double[][] poolImage(double[][] image, int size) {
    double[][] newImage = new double[size][size];
    
    if(image.length>size){
    int newWidth = size;
    int newHeight = size;
    
    
    double scaleX = (double) image.length / newWidth;
    double scaleY = (double) image[0].length / newHeight;

    for (int x = 0; x < newWidth; x++) {
        for (int y = 0; y < newHeight; y++) {
            int startX = Math.min((int) (x * scaleX), image.length - 1);
            int startY = Math.min((int) (y * scaleY), image[0].length - 1);
            int endX = Math.min((int) Math.ceil((x + 1) * scaleX), image.length);
            int endY = Math.min((int) Math.ceil((y + 1) * scaleY), image[0].length);
            
            double sum = 0;
            int count = 0;

            for (int i = startX; i < endX; i++) {
                for (int j = startY; j < endY; j++) {
                    sum += image[i][j];
                    count++;
                }
            }

            // Handle cases where the region might be empty
            newImage[x][y] = (count > 0) ? (sum / count) : image[startX][startY];
        }
    }}
    else {
    int originalWidth = image.length;
    int originalHeight = image[0].length;
  

    double scaleX = (double) originalWidth / size;
    double scaleY = (double) originalHeight / size;

    for (int x = 0; x < size; x++) {
        for (int y = 0; y < size; y++) {
            // Get the position of the pixel in the original image
            double srcX = x * scaleX;
            double srcY = y * scaleY;

            int x1 = (int) Math.floor(srcX);
            int y1 = (int) Math.floor(srcY);
            int x2 = Math.min(x1 + 1, originalWidth - 1);
            int y2 = Math.min(y1 + 1, originalHeight - 1);

            // Bilinear interpolation
            double dx = srcX - x1;
            double dy = srcY - y1;
            double top = (1 - dx) * image[x1][y1] + dx * image[x2][y1];
            double bottom = (1 - dx) * image[x1][y2] + dx * image[x2][y2];
            newImage[x][y] = (1 - dy) * top + dy * bottom;
        }
    }
    }
    return newImage;
}


 public static void writeArrayToFile(String filename, double[][][][] array)   {
       try {
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(array);
        }} catch (Exception e) {
        // TODO: handle exception
       }
    }

public static void writeArrayToFile(String filename, double[][][] array)   {
       try {
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(array);
        }} catch (Exception e) {
        // TODO: handle exception
       }
    }
    // Function to read the array from a file
    public static double[][][][] readArrayFromFile4d(String filename){
        try {
         try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (double[][][][]) ois.readObject();
        }} catch (Exception e) {
            System.out.println(e);
            return new double[0][][][];
        // TODO: handle exception
       }
    }
    public static double[][][] readArrayFromFile3d(String filename){
        try {
         try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (double[][][]) ois.readObject();
        }} catch (Exception e) {
            System.out.println(e);
            return new double[0][][];
        // TODO: handle exception
       }
    }
}
