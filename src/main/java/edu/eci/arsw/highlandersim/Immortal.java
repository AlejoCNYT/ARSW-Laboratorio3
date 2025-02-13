package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback = null;
    private int health;
    private int defaultDamageValue;
    private final List<Immortal> immortalsPopulation;
    private final String name;
    private final Random r = new Random(System.currentTimeMillis());
    private boolean alive = true;
    private static volatile boolean paused = false;
    private static final Object lock = new Object();
    private static volatile boolean stopped = false;


    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback = ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue = defaultDamageValue;
    }

    public boolean estaVivo() {
        return alive;
    }

    public void morir() {
        alive = false;
    }

    public void run() {
        while (estaVivo() && !stopped) {

            // Esperar si está pausado
            synchronized (lock) {
                while (paused && !stopped) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

            if (stopped) break; // Si se presiona STOP, salir inmediatamente

            Immortal im;
            int myIndex = immortalsPopulation.indexOf(this);
            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);
            this.fight(im);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    // Método para detener todos los hilos de inmortales
    public static void stopImmortals() {
        stopped = true;
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }


    public void fight(Immortal i2) {
        Immortal first = this;
        Immortal second = i2;

        if (System.identityHashCode(this) > System.identityHashCode(i2)) {
            first = i2;
            second = this;
        }

        synchronized (first) {
            synchronized (second) {
                if (i2.getHealth() > 0) {
                    i2.changeHealth(i2.getHealth() - defaultDamageValue);
                    this.health += defaultDamageValue;
                    updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
                } else {
                    updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
                }
            }
        }
    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {
        return name + "[" + health + "]";
    }

    // Métodos para pausar y reanudar
    public static void pauseImmortals() {
        synchronized (lock) {
            paused = true;
        }
    }

    public static void resumeImmortals() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }
}
