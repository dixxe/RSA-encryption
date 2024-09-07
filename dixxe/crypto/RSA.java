package dixxe.crypto;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class RSA {

    public static BigInteger[] genKeys(int PrimeLimit)
    {
        Random rand = new Random();
        List<BigInteger> primeNums = Prime.result(PrimeLimit);
        
        BigInteger q = primeNums.get(rand.nextInt(primeNums.size()));
        BigInteger p = primeNums.get(rand.nextInt(primeNums.size()));

        BigInteger n = q.multiply(p);
        
        BigInteger fi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = fi.add(BigInteger.ONE);

        while (e.compareTo(fi) > 0) {
            e = primeNums.get(rand.nextInt(primeNums.size()));
        }
        BigInteger d = e.modInverse(fi);

        // пара (e,n) - открытых ключей
        // пара (d,n) - закрытые ключи

        BigInteger[] result = {e, n, d};
        return result;
    }

    public static BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n); // m^e mod n
    }

    public static BigInteger decrypt(BigInteger encryptedMessage, BigInteger d, BigInteger n) {
        return encryptedMessage.modPow(d, n); // c^d mod n
    }

}
