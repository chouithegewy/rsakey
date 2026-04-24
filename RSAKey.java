package rsa;

import java.math.BigInteger;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

class RSAKey {
    public static void main(String[] args) {
        BigInteger[] parsedArgs = { BigInteger.ZERO };
        try {
            // cli
            parsedArgs = parseArgs(args);
            BigInteger p = parsedArgs[0];
            BigInteger q = parsedArgs[1];
            BigInteger kOrE = parsedArgs[2];
            run(p, q, kOrE);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    static void run(BigInteger p, BigInteger q, BigInteger kOrE) {
        BigInteger n = null;
        BigInteger e = null;
        BigInteger d = null;
        if (p == null && q == null) {
            BigInteger[] nTotientOfN = nTotientOfN(kOrE.intValue(), null, null);
            p = null;
            q = null;
            n = nTotientOfN[0];
            BigInteger phi = nTotientOfN[1];
            e = selectIntegerE(phi);
            while (e.equals(BigInteger.ZERO)) {
                nTotientOfN = nTotientOfN(kOrE.intValue(), null, null);
                n = nTotientOfN[0];
                phi = nTotientOfN[1];
                e = selectIntegerE(phi);
            }
            d = calculateD(e, n);
        } else {
            e = kOrE;
            BigInteger[] nTotientOfN = nTotientOfN(-1, p, q);
            p = null;
            q = null;
            n = nTotientOfN[0];
            BigInteger totientOfN = nTotientOfN[1];
            d = calculateD(e, totientOfN);
        }
        try (FileWriter f = new FileWriter("pub_key.txt", false)) {
            PrintWriter out = new PrintWriter(new BufferedWriter(f));
            out.println("e = " + e);
            out.println("n = " + n);
            out.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
        try (FileWriter f = new FileWriter("pri_key.txt")) {
            PrintWriter out = new PrintWriter(new BufferedWriter(f));
            out.println("d = " + d);
            out.println("n = " + n);
            out.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    // n = p x q, ø(n)=(p-1)x(q-1)
    // negative kBits defaults to 12 bitLength
    // p and q may be null, in which case randomly generated using the specified or
    // default bitLength
    static BigInteger[] nTotientOfN(int kBits, BigInteger p, BigInteger q) {
        if (kBits < 0) {
            kBits = 1 << 11;
        }
        if (p == null) {
            p = BigInteger.probablePrime(kBits, new Random());
        }
        if (q == null) {
            q = BigInteger.probablePrime(kBits, new Random());
        }
        BigInteger n = p.multiply(q);
        BigInteger totientOfN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger[] n_totient_of_n = { n, totientOfN };
        return n_totient_of_n;
    }

    // gcd(ø(n),e)=1; 1<e<ø(n)
    // return 0 in oft chance 2^16 + 1 (Fermat's Fourth Prime) is not coprime to e
    // propogate error upward, regenerate n, try again
    static BigInteger selectIntegerE(BigInteger totient) {
        BigInteger e = BigInteger.valueOf((1 << 15) + 1);
        boolean isCoprime = BigInteger.ONE.equals(totient.gcd(e));
        if (isCoprime) {
            return e;
        } else {
            return BigInteger.ZERO;
        }
    }

    // d ≡ e^-1 (mod ø(n))
    static BigInteger calculateD(BigInteger e, BigInteger totient) {
        return e.modInverse(totient);
    }

    // parse command line args
    // if one given, then k-bits
    // if three given, then p q e
    static BigInteger[] parseArgs(String[] args) throws Exception {
        BigInteger kOrE;
        BigInteger p;
        BigInteger q;
        if (args.length == 1) {
            // k given
            kOrE = new BigInteger(args[0]);
            p = null;
            q = null;
        } else if (args.length == 3) {
            // p q e given
            p = new BigInteger(args[0]);
            q = new BigInteger(args[1]);
            kOrE = new BigInteger(args[2]);
        } else {
            throw new Exception(
                    "Invalid amount of args: " + args.length + "\nCan be 1 if k-bits given or three if p q and e");
        }

        BigInteger[] userInput = { p, q, kOrE };
        return userInput;
    }
}
