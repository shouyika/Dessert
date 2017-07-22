package com.syk.dessert.concurrent;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by shouyika on 2017/7/22.
 */
public class BlockingCell<E> implements Serializable{
    private static final long serialVersionUID = -387911632671298426L;

    final ReentrantLock lock = new ReentrantLock();

    private final Condition full = lock.newCondition();

    private final Condition empty = lock.newCondition();

    private boolean hasCell = false;

    private E currentCell = null;

    public void put(E cell) throws InterruptedException{
        if(cell == null) throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try{
            while (!setCell(cell)) {
                empty.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public E fetch() throws InterruptedException{
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E retVal;
            while((retVal = getCell()) == null){
                full.await();
            }
            return retVal;
        } finally {
            lock.unlock();
        }
    }

    private boolean setCell(E cell){
        //assert have lock
        if(hasCell) {
            return false;
        }
        currentCell = cell;
        hasCell = true;
        full.signal();
        return true;
    }

    private E getCell() {
        //assert have lock
        if(!hasCell) {
            return null;
        }
        E retVal = currentCell;
        currentCell = null;
        hasCell = false;
        empty.signal();
        return retVal;
    }
}
