package dixxe.crypto;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DecoderInterface {
    public static void Main(String[] args)
    {
        // тестовой код, удали потом. Закоментируй настоящий.
        BigInteger[] keys = RSA.genKeys(100000);
        String m = "Dixxe loves cryptography";
        for (byte b : m.getBytes())
        {
            BigInteger s = RSA.decrypt(BigInteger.valueOf(b), keys[2], keys[1]);
            System.out.print(s + " ");
        }
        
        System.out.println(String.format("\nОткрытые ключи RSA: e %1$s; n %2$s", keys[0], keys[2]));
    }

    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);

        System.out.println("Hello! Please choose mode (d)ecrypt (e)ncrypt");

        try {
            String input = scan.nextLine();

            switch (input) {
                case "e":
                {
                    String plainText;
                    String choice;
                    BigInteger[] keys = new BigInteger[2];
                    String[] buffer;

                    System.out.println("Okay. Now choose:\nif you have *own* RSA keys type OWN\nelse type CREATE");
                    choice = scan.nextLine();
                    switch (choice)
                    {
                        case "OWN":
                            {
                                System.out.println("Nice, paste keys in this order, seperated by whitespace: e; n; ");
                                buffer = scan.nextLine().split(" ");
                                for (int i = 0; i < keys.length; i++)
                                {
                                    keys[i] = new BigInteger(buffer[i]);
                                }
                                System.out.println("Keys recieved.");
                                break;
                            }
                        case "CREATE":
                            {
                                System.out.println("Nice, generating keys...");
                                keys = RSA.genKeys(1000000);
                                System.out.println("Keys created.");
                                System.out.println(String.format("Your keys! Be aware, that you shouldn't show D key, because it used in decryption\nE: %1$s, N: %2$s D: %3$s", keys[0], keys[1], keys[2]));
                                break;

                            }
                        default:
                            break;
                    }

                    System.out.println("Now type what you want to encrypt: ");
                    plainText = scan.nextLine();
                    System.out.println("Message readed. Encrypting...");
                    encodeText(plainText, keys);

                    break;

                }
                    
                case "d":
                {
                    String[] codedText;
                    byte[] decodedBytes;

                    BigInteger[] keys;
                    String[] buffer;

                    System.out.println("Okay. Now paste an encoded text where each word seperated by whitespace: \n");
                    codedText = scan.nextLine().split(" ");

                    System.out.println("Okay. Now paste your key (D) FIRST and public key (N) SECOND seperated by whitespace: \n");
                    buffer = scan.nextLine().split(" ");

                    keys = new BigInteger[buffer.length];
                    for (int i = 0; i < keys.length; i++)
                    {
                        keys[i] = new BigInteger(buffer[i]);
                    }
                    System.out.println("Keys recieved. Starting decoding...");

                    decodedBytes = new byte[codedText.length];

                    for (int i = 0; i < codedText.length; i++)
                    {
                        decodedBytes[i] = RSA.decrypt(new BigInteger(codedText[i]), keys[0], keys[1]).byteValue();
                        System.out.println(new String(decodedBytes, Charset.forName("UTF-8")));
                    }
                }
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong. Try again.");
            System.out.print(e);
        }
            
    }

    static List<BigInteger> encodeText(String plainText, BigInteger[] keys)
        {
            List<BigInteger> encrypted = new ArrayList<BigInteger>();
            
            for (byte b : plainText.getBytes())
            {
                encrypted.add(RSA.encrypt(BigInteger.valueOf(b), keys[0], keys[1]));
            }

            System.out.println("Message encrypted. Here coded text. Each word seperated by space.\n--------------");

            for (BigInteger code : encrypted)
            {
                System.out.print(code + " ");
            }

            System.out.println(String.format("\n---------------\n"));
            return encrypted;
        }
}
