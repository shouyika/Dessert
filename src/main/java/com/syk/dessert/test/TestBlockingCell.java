package com.syk.dessert.test;

import com.syk.dessert.concurrent.BlockingCell;

/**
 * Created by shouyika on 2017/7/22.
 */
public class TestBlockingCell {

    public static void main(String[] args){
        final BlockingCell<Integer> b = new BlockingCell<Integer>();

        for(int i = 0; i < 100; i++) {
            final int t = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        b.put(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        for(int i = 0; i < 100; i++) {

            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println(b.fetch());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
