/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private boolean pause = false;


    private Control() {
        //Create the threads with an empty constructor using start() method
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0; i < NTHREADS - 1; i++) {

            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this);
    }
    
    public static Control newControl() {
        return new Control();
    }

    public boolean isPaused() {return pause;}

    public synchronized void pauseThreads() {
        pause = true;
    }

    public synchronized void resumeThread() {
        pause = false;
        notifyAll();
    }

    /**
     * Run the threads and sleep the thread for 5000 milliseconds, showing the total of prime numbers found,
     * waiting until the user press enter
     * @throws InterruptedException
     */
    @Override
    public void run() {

        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }

        //Wait for enter
        Scanner scanner = new Scanner(System.in);

        while(true){
            try {
                Thread.sleep(TMILISECONDS);
                pauseThreads();

                int totalPrimes = 0;
                for(PrimeFinderThread thread : pft) {
                    totalPrimes += thread.getPrimes().size();
                }

                System.out.println("Total primes found: " + totalPrimes);
                System.out.println("Press enter to continue...");

                scanner.nextLine();
                resumeThread();

            } catch (InterruptedException e) {

                throw new RuntimeException(e);
            }
        }
    }
    
}
