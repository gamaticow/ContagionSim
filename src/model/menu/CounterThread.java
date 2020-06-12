package model.menu;
/*
 *   Created by Corentin on 29/05/2020 at 18:48
 */

public class CounterThread extends Thread {

    private static final long START_VALUE = 200;
    private static final long REMOVE_PER_TURN = 25;
    private static final long MIN = 25;

    private final Runnable runnable;
    private boolean running;

    public CounterThread(Runnable runnable){
        this.runnable = runnable;
    }

    @Override
    public void run() {
        running = true;
        long current = START_VALUE;
        while (running){
            runnable.run();
            try {
                Thread.sleep(current);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            current = Math.max(MIN, current - REMOVE_PER_TURN);
        }
    }

    public void cancel(){
        running = false;
    }
}
