/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Andy Hind
 * @since 1.0
 */
@RunWith(JUnit4.class)
public class TextGeneratorTest
{
    @Test (expected=RuntimeException.class)
    public void configLoadFail()
    {
        new TextGenerator("org/alfresco/textgen/lexicon-en.txt");
    }
    
    @Test
    public void configLoadStemEnTest() throws IOException
    {
        TextGenerator tg = new TextGenerator( "org/alfresco/textgen/lexicon-stem-en-test.txt");
        assertEquals(7, tg.getWordGenerator().getWordCount());
        Set<String> words = tg.getWordGenerator().getWordSet();
        assertTrue(words.contains("woof"));
        assertTrue(words.contains("banana"));
        assertTrue(words.contains("go"));
        assertTrue(!words.contains("no~"));
        assertTrue(!words.contains("'s"));
        assertTrue(words.contains("one"));
        assertTrue(words.contains("two"));
        assertTrue(!words.contains("'n"));
        assertTrue(words.contains("no"));
        assertTrue(words.contains("tree"));
        assertTrue(!words.contains("sponge"));
    }
    
    
    @Test (expected=IllegalStateException.class)
    public void generateStemEnTestShort() throws IOException
    {
        TextGenerator tg = new TextGenerator( "org/alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is =  tg.getInputStream(Locale.ENGLISH, 0, 2, "P100.00", "P010.00", "P001.00");
    }
        
    @Test
    public void generateStemEnTestFixedonly() throws IOException
    {
        TextGenerator tg = new TextGenerator( "org/alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is =  tg.getInputStream(Locale.ENGLISH, 0, 7, "P100.00", "P010.00", "P001.00");
        assertEquals("P100.00", getString(is));
    }
    
    private String getString(InputStream is) throws IOException
    {
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        int current = -1;
        StringBuffer buffer = new StringBuffer();
        while( (current = reader.read()) != -1)
        {
            buffer.append((char)current);
        }
        return buffer.toString();
    }
    
    @Test
    public void generateStemEnTest() throws IOException
    {
        TextGenerator tg = new TextGenerator( "org/alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is =  tg.getInputStream(Locale.ENGLISH, 0, 200, "P100.00", "P010.00", "P001.00");
        assertEquals("P100.00 one woof go banana banana woof woof tree no tree woof woof woof woof banana tree woof go woof two tree woof one one two two banana no woof woof tree one one woof woof woof woof woof woof no   "
   
, getString(is));
    }
    
    @Test
    public void generateStemEnTestDiffer() throws IOException
    {
        TextGenerator tg = new TextGenerator( "org/alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is0 =  tg.getInputStream(Locale.ENGLISH, 0, 200, "P100.00", "P010.00", "P001.00");
        InputStream is1 =  tg.getInputStream(Locale.ENGLISH, 1, 200, "P100.00", "P010.00", "P001.00");
        assertNotEquals(getString(is0), getString(is1));
    }
    
    
    @Test
    public void configLoadStem() throws IOException
    {
        TextGenerator tg = new TextGenerator("org/alfresco/textgen/lexicon-stem-en.txt");
        assertEquals(13339, tg.getWordGenerator().getWordCount());
        Set<String> words = tg.getWordGenerator().getWordSet();
        assertTrue(!words.contains("'s"));
        assertTrue(words.contains("Alex"));
        assertTrue(words.contains("zero"));
    }
    
    @Test
    public void generateStem() throws IOException
    {
        TextGenerator tg = new TextGenerator("org/alfresco/textgen/lexicon-stem-en.txt");
        InputStream is = tg.getInputStream(Locale.ENGLISH, 0, 1024);
        assertEquals(1024, getString(is).length());
        
        is = tg.getInputStream(Locale.ENGLISH, 0, 1024, "one", "two");
        assertEquals(1024, getString(is).length());
    }
    
    @Test
    public void generateStem10M_second() throws IOException
    {
        TextGenerator tg = new TextGenerator("org/alfresco/textgen/lexicon-stem-en.txt");
        long start = System.nanoTime();
        InputStream is = tg.getInputStream(Locale.ENGLISH, 0, 10*1024*1024);
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        int current = -1;
        int count = 0;
        while( (current = reader.read()) != -1)
        {
            count++;
        }
        long end = System.nanoTime();
        assertEquals(count, 10*1024*1024);
        assertTrue(end-start < 10000000000L);
       
    }
}
