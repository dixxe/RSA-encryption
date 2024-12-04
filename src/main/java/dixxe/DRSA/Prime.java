package dixxe.DRSA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Prime
{
    static private int limit;
    static private int sqr_limit;
    static private boolean[] isPrime;
    static private int x2, y2, i, j, n;

    static private void pass()
    {
        sqr_limit = (int)Math.sqrt((double)limit);
        for (i = 0; i <= limit; i++)
        {
            isPrime[i] = false;
        }

        isPrime[2] = true;
        isPrime[3] = true;

        x2 = 0;
        for (i = 1; i <= sqr_limit; i++)
        {
            x2 += 2 * i - 1;
            y2 = 0;
            for (j = 1; j <= sqr_limit; j++)
            {
                y2 += 2 * j - 1;
                n = 4 * x2 + y2;
                if ((n <= limit) && (n % 12 == 1 || n % 12 ==5))
                {
                    isPrime[n] = !isPrime[n];
                }

                n -= x2;
                if ((n <= limit) && (n % 12 == 7))
                {
                    isPrime[n] = !isPrime[n];
                }

                n -= 2 * y2;
                if ((i > j) && (n <= limit) && (n % 12 == 11))
                {
                    isPrime[n] = !isPrime[n];
                }
            }
        }
    }

    static private void filter()
    {
        for (i = 5; i <= sqr_limit; ++i) {
            if (isPrime[i]) {
                n = i * i;
                for (j = n; j <= limit; j += n)
                    isPrime[j] = false;
            }
        }
    }

    static public List<BigInteger> result(int limit)
    {
        Prime.limit = limit;
        Prime.isPrime = new boolean[limit + 1];
        pass();
        filter();

        List<BigInteger> primeNums = new ArrayList<>();
        for (i = 6; i <= limit; ++i) {  // добавлена проверка делимости на 3 и 5. В оригинальной версии алгоритма потребности в ней нет.
            if ((isPrime[i]) && (i % 3 != 0) && (i % 5 !=  0))
                primeNums.add(BigInteger.valueOf(i));
        }
        return primeNums;
    }

}
