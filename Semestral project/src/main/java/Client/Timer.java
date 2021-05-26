package Client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Timer implements Runnable {
    public String time;
    public boolean stop = false;
    private static final Logger LOG = Logger.getLogger(Timer.class.getName());

    public Timer() {
        Thread thread = new Thread(this, "timer");
        thread.start();
    }

    public void drawTime(){
//        System.out.println(time);
    }

    public void run() {
        int seconds = 0;
        int minutes = 0;
        while (!stop) {
            time = minutes + ":" + seconds;
            drawTime();
            try {
                Thread.sleep(900);
            } catch (InterruptedException e){
                LOG.log(Level.WARNING, "error with timer");
            }
            seconds++;
            if(seconds == 60){
                seconds = 0;
                minutes++;
            }
        }
    }

}
