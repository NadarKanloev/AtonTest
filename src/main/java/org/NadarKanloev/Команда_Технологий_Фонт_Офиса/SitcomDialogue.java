package org.NadarKanloev.Команда_Технологий_Фонт_Офиса;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SitcomDialogue {
    private static final Lock lock = new ReentrantLock();
    private static final Condition[] conditions = new Condition[6];
    private static int currentTurn = 0;

    public static void main(String[] args){
        String[] names = {"Chandler", "Joey", "Monica", "Phoebe", "Chandler", "Joey", "Chandler", "Phoebe", "Joey", "Monica", "Phoebe", "Chandler", "Chandler", "Joey"};
        String[] lines = {
                "Hey, hey.",
                "Hey.",
                "Hey.",
                "And this from the cry-for-help department. Are you wearing makeup?",
                "Yes, I am. As of today, I am officially Joey Tribbiani, actor slash model.",
                "That's so funny, 'cause I was thinking you look more like Joey Tribbiani, man slash woman.",
                "What were you modeling for?",
                "You know those posters for the City Free Clinic?",
                "Oh, wow, so you're gonna be one of those \"healthy, healthy, healthy guys\"?",
                "You know, the asthma guy was really cute.",
                "Do you know which one you're gonna be?",
                "No, but I hear lyme disease is open, so... (crosses fingers)",
                "Good luck, man. I hope you get it.",
                "Thanks."
        };

        for(int i = 0; i < conditions.length; i++){
            conditions[i] = lock.newCondition();
        }

        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new Character(0, names, lines));
        threads[1] = new Thread(new Character(1, names, lines));
        threads[2] = new Thread(new Character(2, names, lines));
        threads[3] = new Thread(new Character(3, names, lines));

        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Character implements Runnable {
        private final int id;
        private final String[] names;
        private final String[] lines;

        public Character(int id, String[] names, String[] lines){
            this.id = id;
            this.names = names;
            this.lines = lines;
        }

        @Override
        public void run(){
            for(int i = id; i < lines.length; i += 6){
                lock.lock();
                try {
                    while (currentTurn % 4 != id) {
                        conditions[id].await();
                    }
                    System.out.println(names[i] + ": " + lines[i]);
                    currentTurn++;
                    conditions[(id + 1) % 4].signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
