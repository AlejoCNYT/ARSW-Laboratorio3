/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartProduction
{

    public static void main(String[] args)
    {
        
        BlockingQueue<Integer> queue=new LinkedBlockingQueue<>(10);
        
        int numProducers = 3;
        for (int i = 0; i < numProducers; i++)
        {
            new Producer(queue, 100).start();
        }

        int numConsumers = 1;
        for (int i = 0; i < numConsumers; i++)
        {
            new Consumer(queue).start();
        }

    }

}