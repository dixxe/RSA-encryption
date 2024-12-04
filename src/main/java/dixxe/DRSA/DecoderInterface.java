package dixxe.DRSA;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DecoderInterface 
{
    // Old code zone. Some terminal debug info.
    static List<BigInteger> encodeText(String plainText, BigInteger[] keys)
        {
            List<BigInteger> encrypted = new ArrayList<BigInteger>();
            
            for (byte b : plainText.getBytes())
            {
                encrypted.add(RSA.encrypt(BigInteger.valueOf(b), keys[0], keys[1]));
                encrypted.add(RSA.encrypt(BigInteger.valueOf("@".getBytes()[0]), keys[0], keys[1]));
            }

            System.out.println("Message encrypted. Here coded text. Each word seperated by space.\n--------------");

            for (BigInteger code : encrypted)
            {
                System.out.print(code + " ");
            }

            System.out.println("\n---------------\n");
            return encrypted;
        }
    // New code zone. Shortcuts for boilerplate.
    public static String DecodeText(String encodedMessage, BigInteger[] keys) {
        List<BigInteger> decodedMessage = new ArrayList<>();
        BigInteger separatorValue = DecoderInterface.encodeText("@", keys).get(0);
        String[] seperatedMessagesString = encodedMessage.split(separatorValue.toString());
        BigInteger[] separateMessagesBigIntegers = getBigIntegersFromString(seperatedMessagesString);
        for (BigInteger code : separateMessagesBigIntegers) {
            decodedMessage.add(RSA.decrypt(code, keys[2], keys[1]));
        }
        String resultString = "";
        // Decoding message byte by byte and appending it to string.
        for (BigInteger wrongByte : decodedMessage) {
            resultString = resultString.concat(new String(wrongByte.toByteArray(), StandardCharsets.UTF_8));
        }
        return resultString;
    }

    // Converter method.
    private static BigInteger[] getBigIntegersFromString(String[] readValue) {
        List<BigInteger> buffer = new ArrayList<>();
        for(String key : readValue) {
            buffer.add(new BigInteger(key));
        }
        return buffer.toArray(new BigInteger[0]);
    }
}
