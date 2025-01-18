import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Cal_Enc{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Ask the user for the image file path
            System.out.print("Enter the full path of the image file: ");
            String image_path = sc.nextLine(); // Get image path input from the user

            // Load the image from the specified file path
            File input_file = new File(image_path);
            if (!input_file.exists()) {
                System.out.println("Error: File does not exist at the specified path.");
                return;
            }

            BufferedImage image = ImageIO.read(input_file);
            if (image == null) {
                System.out.println("Error: Unsupported or corrupted image file.");
                return;
            }

            // Get encryption key from the user
            System.out.print("Enter encryption key (integer): ");
            int key = sc.nextInt();
            int k = key; // Store the original key

            // Perform encryption
            BufferedImage encrypt_image = encrypt_image(image, key, k);

            // Ask the user for the output file path
            System.out.print("Enter the output path for the encrypted image (including the file name): ");
            sc.nextLine(); // Consume the newline left from the previous input
            String output_path = sc.nextLine();

            // Save the encrypted image
            File output_file = new File(output_path);
            ImageIO.write(encrypt_image, "jpg", output_file);

            System.out.println("Image encryption completed successfully!");
            System.out.println("Encrypted image saved at: " + output_path);

        } catch (IOException e) {
            System.out.println("Error loading or saving the image: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    
    private static BufferedImage encrypt_image(BufferedImage image, int key, int k) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage encrypted_image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        int px=128; //mid point in range 0-255;
        int temp=0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y) & 0xFF;
                int n=pixel%10;
                int encrypted_pixel = ((pixel*key)^(px*key+149)) % 256;
                int a=(pixel-px);
                px=pixel;
                if (key == 0) 
                key = k;
                key = (temp*a+17)-(key*n-(x+y))%256;
                temp=key;
                encrypted_image.setRGB(x, y, encrypted_pixel);
            }
        }
        return encrypted_image;
    }
}
