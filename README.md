# RSA implementation

RSA Key Pair Generation:

1. Choose two large distinct primes, p and q.

2. Compute Modulus: n = p x q.

3. Calculate Totient: ø(n) = (p-1)x(q-1)

4. Select integer e: 1 < e < ø(n) && gcd(ø(n), e) == 1

4. Calculate d: multiplicative inverse of e (mod ø(n))
aka (d x e) &cong; 1 (mod ø(n))

5. (e, n), (d, n), public and private keys, respectively


To compile and run:
```bash
mkdir out && javac -d out *.java && java -cp out RSAKey 6551 4733 8311
```
