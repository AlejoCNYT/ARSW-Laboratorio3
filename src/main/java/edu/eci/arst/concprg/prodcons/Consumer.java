/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread
{

    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue)
    {
        this.queue=queue;
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Integer item = queue.take();

                System.out.println("Consumed: " + item);
                Thread.sleep(3000);

            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
                break;
            }
        }
    }
}
