/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.textgen;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Andy Hind
 * @since 1.0
 */
@RunWith(JUnit4.class)
public class WordGeneratorTest
{
    @Test
    public void testEmpty()
    {
        WordGenerator wg = new WordGenerator(2);
        assertNull(wg.getWord(0));
        assertNull(wg.getWord(0.5));
        assertNull(wg.getWord(1));
        
        assertNull(wg.getWord(-1));
        assertNull(wg.getWord(2));
    }
    
    @Test
    public void testSingle()
    {
        WordGenerator wg = new WordGenerator(1);
        wg.addWord("Banana", 1);
        
        assertEquals("Banana", wg.getWord(0));
        assertEquals("Banana", wg.getWord(0.5));
        assertEquals("Banana", wg.getWord(1));
        
        assertNull(wg.getWord(-1));
        assertNull(wg.getWord(2));
        
        assertEquals(1, wg.getWordCount());
    }
    
    @Test
    public void testEven()
    {
        WordGenerator wg = new WordGenerator(2);
        wg.addWord("One", 10);
        wg.addWord("Two", 10);
        wg.addWord("Three", 10);
        wg.addWord("Four", 10);
        wg.addWord("Five", 10);
        wg.addWord("Six", 10);
        wg.addWord("Seven", 10);
        wg.addWord("Eight", 10);
        wg.addWord("Nine", 10);
        wg.addWord("Ten", 10);
        
        assertEquals("One", wg.getWord(0));
        assertEquals("One", wg.getWord(0.09999));
        assertEquals("Two", wg.getWord(0.1));
        assertEquals("Two", wg.getWord(0.19999));
        assertEquals("Three", wg.getWord(0.2));
        assertEquals("Three", wg.getWord(0.29999));
        assertEquals("Four", wg.getWord(0.3));
        assertEquals("Four", wg.getWord(0.39999));
        assertEquals("Five", wg.getWord(0.4));
        assertEquals("Five", wg.getWord(0.49999));
        assertEquals("Six", wg.getWord(0.5));
        assertEquals("Six", wg.getWord(0.59999));
        assertEquals("Seven", wg.getWord(0.6));
        assertEquals("Seven", wg.getWord(0.69999));
        assertEquals("Eight", wg.getWord(0.7));
        assertEquals("Eight", wg.getWord(0.79999));
        assertEquals("Nine", wg.getWord(0.8));
        assertEquals("Nine", wg.getWord(0.89999));
        assertEquals("Ten", wg.getWord(0.9));
        assertEquals("Ten", wg.getWord(0.99999));
        assertEquals("Ten", wg.getWord(1.0));
        
        assertNull(wg.getWord(-1));
        assertNull(wg.getWord(2));
        
        assertEquals(10, wg.getWordCount());
    }
    
    @Test
    public void tset2n()
    {
        WordGenerator wg = new WordGenerator(2);
        wg.addWord("One", 1);
        wg.addWord("Two", 2);
        wg.addWord("Three", 4);
        wg.addWord("Four", 8);
        wg.addWord("Five", 16);
        wg.addWord("Six", 32);
        wg.addWord("Seven", 64);
        wg.addWord("Eight", 128);
        wg.addWord("Nine", 256);
        wg.addWord("Ten", 512);
        
        assertEquals("One", wg.getWord(0));
        assertEquals("Two", wg.getWord(0.001953125));
        assertEquals("Three", wg.getWord(0.00390625));
        assertEquals("Four", wg.getWord(0.0078125));
        assertEquals("Five", wg.getWord(0.015625));
        assertEquals("Six", wg.getWord(0.03125));
        assertEquals("Seven", wg.getWord(0.0625));
        assertEquals("Eight", wg.getWord(0.125));
        assertEquals("Eight", wg.getWord(0.24));
        assertEquals("Nine", wg.getWord(0.25));
        assertEquals("Nine", wg.getWord(0.48));
        assertEquals("Ten", wg.getWord(0.5));
        assertEquals("Ten", wg.getWord(0.6));
        assertEquals("Ten", wg.getWord(0.7));
        assertEquals("Ten", wg.getWord(0.8));
        assertEquals("Ten", wg.getWord(0.9));
        assertEquals("Ten", wg.getWord(1.0));
        
        assertNull(wg.getWord(-1));
        assertNull(wg.getWord(2));
        
        assertEquals(10, wg.getWordCount());
    }
}
