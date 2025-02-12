package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread
{

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private boolean alive = true;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb)
    {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
    }

    public boolean estaVivo()
    {
        return alive;
    }

    public void morir()
    {
        alive = false;
    }

    public void run()
    {
        while (estaVivo())
        {
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex)
            {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try
            {
                Thread.sleep(1);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void fight(Immortal i2)
    {
        Immortal first = this;
        Immortal second = i2;

        // if (!this.estaVivo() || !second.estaVivo())
        // {
        // System.out.println("ERROR: " + this.name + " intentó pelear con " + second.name + " que está muerto.");
        // return; // Evitar la pelea
        // }
        // System.out.println(this.name + " pelea con " + second.name);

        if (System.identityHashCode(this) > System.identityHashCode(i2))
        {
            first = i2;
            second = this;
        }

        synchronized (first)
        {
            synchronized (second)
            {
                if (i2.getHealth() > 0)
                {
                    i2.changeHealth(i2.getHealth() - defaultDamageValue);
                    this.health += defaultDamageValue;
                    updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
                } else
                {
                    updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
                }
            }
        }
    }

    public void changeHealth(int v)
    {
        health = v;

    }

    public int getHealth()
    {
        return health;
    }

    @Override
    public String toString()
    {
        return name + "[" + health + "]";
    }

}
