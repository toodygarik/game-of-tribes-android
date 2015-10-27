package com.garik.android.gameoflife;

import junit.framework.TestCase;

public class GameModelTest extends TestCase {

    private GameModel instance;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        instance = new GameModel(3, 3);
    }

    public void testInit() throws Exception {
        assertEquals(3, instance.getRows());
        assertEquals(3, instance.getCols());
    }

    public void testIsLiveCell() throws Exception {
        assertFalse(instance.isAlive(1, 2));

        assertFalse(instance.isAlive(-1, 0));
        assertFalse(instance.isAlive(0, -1));
        assertFalse(instance.isAlive(3, 0));
        assertFalse(instance.isAlive(0, 3));
    }

    public void testMakeAlive() throws Exception {
        instance.makeAlive(1, 2, 0xff);
        assertTrue(instance.isAlive(1, 2));
        assertFalse(instance.isAlive(0, 0));

        instance.makeAlive(-1, 0, 0xff);
        instance.makeAlive(0, -1, 0xff);
        instance.makeAlive(3, 0, 0xff);
        instance.makeAlive(0, 3, 0xff);
    }

    public void testMakeDead() throws Exception {
        instance.makeAlive(1, 2, 0xff);
        assertTrue(instance.isAlive(1, 2));
        instance.makeDead(1, 2);
        assertFalse(instance.isAlive(1, 2));

        instance.makeAlive(0, 2, 0xff);
        assertTrue(instance.isAlive(0, 2));
        instance.makeDead(0, 2);
        assertFalse(instance.isAlive(0, 2));

        instance.makeDead(-1, 0);
        instance.makeDead(0, -1);
        instance.makeDead(3, 0);
        instance.makeDead(0, 3);
    }

    public void testLiveCell() throws Exception {
        // without any neighbour dies
        instance.makeAlive(1, 2, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // with one neighbour dies
        instance.makeAlive(2, 2, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // with two neighbours lives
        instance.makeAlive(1, 1, 0xff);
        assertTrue(instance.willLive(1, 2) != 0);

        // with three neighbours lives
        instance.makeAlive(0, 2, 0xff);
        assertTrue(instance.willLive(1, 2) != 0);

        // with four neighbours dies
        instance.makeAlive(2, 1, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // with five neighbours dies
        instance.makeAlive(0, 1, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // after an iteration
        instance.next();
        assertFalse(instance.willLive(1, 2) != 0);
    }


    public void testDeadCell() throws Exception {
        assertFalse(instance.willLive(1, 2) > 0);

        // with one neighbour dies
        instance.makeAlive(2, 2, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // with two neighbours lives
        instance.makeAlive(1, 1, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // with three neighbours lives
        instance.makeAlive(0, 2, 0xff);
        assertTrue(instance.willLive(1, 2) != 0);

        // with four neighbours dies
        instance.makeAlive(2, 1, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // with five neighbours dies
        instance.makeAlive(0, 1, 0xff);
        assertFalse(instance.willLive(1, 2) != 0);

        // after an iteration
        instance.next();
        assertFalse(instance.willLive(1, 2) != 0);
    }

    public void testSetupRandom() throws Exception {
        instance.setupRandom(-100);
        assertEquals(0, countAlives(instance));

        instance.setupRandom(3);
        assertEquals(3, countAlives(instance));

        instance.setupRandom(1);
        assertEquals(1, countAlives(instance));
    }

    private int countAlives(GameModel instance) {
        int count = 0;
        for (int r = 0; r < instance.getRows(); r++) {
            for (int c = 0; c < instance.getCols(); c++) {
                if(instance.isAlive(r, c))
                    count++;
            }
        }
        return count;
    }

    public void testClear() throws Exception {
        instance.makeAlive(0, 0, 0xff);
        instance.makeAlive(0, 1, 0xff);
        assertEquals(2, countAlives(instance));

        instance.clear();
        assertEquals(0, countAlives(instance));
    }

    public void testAvgCreate() throws Exception {
        instance.makeAlive(0,0, 0xff);
        instance.makeAlive(0,1, 0xff00);
        instance.makeAlive(0,2, 0xff0000);
        assertEquals(instance.willLive(1, 1), 0x555555);

        assertEquals(instance.willLive(0, 1), 0xff00);
        assertEquals(instance.willLive(0, 0), 0);
    }
}