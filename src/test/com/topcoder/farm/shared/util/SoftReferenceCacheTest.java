/*
 * SoftReferenceCacheTest
 *
 * Created 03/29/2007
 */
package com.topcoder.farm.shared.util;

import java.util.Set;

import junit.framework.TestCase;

/**
 * Unit Test for SoftReferenceCache
 *
 * @author Diego Belfer (mural)
 * @version $Id$
 */
public class SoftReferenceCacheTest extends TestCase {

    /**
     * What we put is what we get
     */
    public void testPutAndGet() throws Exception {
        SoftReferenceCache cache = new SoftReferenceCache();
        Object obj1 = getNewObject();
        Object obj2 = getNewObject();
        cache.put("1", obj1);
        cache.put("2", obj2);
        assertEquals(obj1, cache.get("1"));
        assertEquals(obj2, cache.get("2"));
        assertEquals(2, cache.size());
    }

    /**
     * Unreachable value is removed after a GC.
     */
    public void testPutAndGetNoReachable() throws Exception {
        SoftReferenceCache cache = new SoftReferenceCache();
        Object obj2 = getNewObject();
        cache.put("1", getNewObject());
        cache.put("2", obj2);
        forceCollect();
        assertEquals(null, cache.get("1"));
        assertEquals(obj2, cache.get("2"));
        assertEquals(1, cache.size());
    }

    /**
     * Unreachable value is removed after a GC.
     */
    public void testPutAndContains() throws Exception {
        SoftReferenceCache cache = new SoftReferenceCache();
        Object obj2 = getNewObject();
        cache.put("1", getNewObject());
        cache.put("2", obj2);
        forceCollect();
        assertFalse(cache.containsKey("1"));
        assertTrue(cache.containsKey("2"));
    }

    /**
     * Removes should remove the value
     */
    public void testPutAndRemove() throws Exception {
        SoftReferenceCache cache = new SoftReferenceCache();
        Object obj2 = getNewObject();
        cache.put("1", getNewObject());
        cache.put("2", obj2);
        forceCollect();
        cache.remove("2");
        assertFalse(cache.containsKey("1"));
        assertFalse(cache.containsKey("2"));
        assertEquals(0, cache.size());
    }


    /**
     * Clear should remove everything
     */
    public void testClear() throws Exception {
        SoftReferenceCache cache = new SoftReferenceCache();
        Object obj2 = getNewObject();
        cache.put("1", getNewObject());
        cache.put("2", obj2);
        forceCollect();
        cache.clear();
        assertFalse(cache.containsKey("1"));
        assertFalse(cache.containsKey("2"));
        assertEquals(0, cache.size());
    }

    /**
     * Keyset should return only keys with values associated
     */
    public void testKeySet() throws Exception {
        SoftReferenceCache cache = new SoftReferenceCache();
        Object obj2 = getNewObject();
        cache.put("1", getNewObject());
        cache.put("2", obj2);
        forceCollect();
        Set set = cache.keySet();
        assertTrue(set.contains("2"));
        assertEquals(1, set.size());
    }

    private void forceCollect() throws InterruptedException {
        for (int i = 0; i < 200; i++) {
            int[] x = new int[1000000];
            System.gc();
            x[0]++;
        }
        Thread.sleep(100);
    }

    private Object getNewObject() {
        return new Object();
    }
}
