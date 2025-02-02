package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	int a, b;
	private List<Integer> primes;
    private final Control control;
	
	public PrimeFinderThread(int a, int b, Control control) {
		super();
        this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
        this.control = control;
	}

    @Override
	public void run(){
            for (int i= a;i < b;i++){
                //avoid race condition, using synchronized block (just one thread can access to the block)
                synchronized (control) {
                    while (control.isPaused()) {
                        try {
                            control.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (isPrime(i)){
                    primes.add(i);
                    System.out.println(i);
                }
            }
	}

    /**
     * Verify if a number is prime
     * @param n number
     * @return boolean with the verification if the number is prime
     */
	boolean isPrime(int n) {
	    boolean ans;

        if (n > 2) {
            ans = n%2 != 0;

            //if n is divisible by any odd number then it is not prime
            for(int i = 3;ans && i*i <= n; i+=2 ) {
                ans = n % i != 0;
            }

        } else {
            ans = n == 2;
        }

	    return ans;
	}

    /**
     * list prime numbers
     * @return list of prime numbers
     */
	public List<Integer> getPrimes() {
		return primes;
	}
	
}
