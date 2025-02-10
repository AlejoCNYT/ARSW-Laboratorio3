/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import org.example.BlacklistSearch;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Integer> blacklistServers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        BlacklistSearch searcher = new BlacklistSearch(blacklistServers, 10);
        int occurrences = searcher.search("192.168.1.1");

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

        System.out.println("Total de ocurrencias: " + occurrences);
        if (occurrences >= BlacklistSearch.BLACK_LIST_ALARM_COUNT) {
            System.out.println("HOST NO CONFIABLE");
        } else {
            System.out.println("HOST CONFIABLE");
        }

    }

}