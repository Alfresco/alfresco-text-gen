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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
        new TextGenerator("alfresco/textgen/lexicon-en.txt");
    }
    
    @Test
    public void configLoadStemEnTest() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
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
    
    
    @SuppressWarnings("unused")
    @Test (expected=IllegalStateException.class)
    public void generateStemEnTestShort() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is =  tg.getInputStream(0, 2, "P100.00", "P010.00", "P001.00");
    }
        
    @Test
    public void generateStemEnTestFixedonly() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is =  tg.getInputStream(0, 7, "P100.00", "P010.00", "P001.00");
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
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is =  tg.getInputStream(0, 200, "P100.00", "P010.00", "P001.00");
        assertEquals(
                "P100.00 one woof go banana banana woof woof tree no tree woof woof woof woof banana tree "
                + "woof go woof two tree woof one one two two banana no woof woof tree one one woof woof woof woof woof woof no   ",
                getString(is));
    }
    
    @Test
    public void queryStemEnTest() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        
        for(int j = 0; j < 100; j++)
        {
            InputStream is =  tg.getInputStream(j, 2000);
            String content = getString(is);

            int end = 0;
            for(int i = 1; i< 50; i++)
            {
                end = content.indexOf(" ", end+1);
                assertEquals(content.subSequence(0, end), tg.generateQueryString(j, i, i));
            }
          
            for(int i = 0; i < 50; i++)
            {
                assertTrue(content.contains(tg.generateQueryString(j, i, 50)));
            }
        }
    }
    
    /**
     * Ensure that the text generation can grow from zero to 256 bytes i.e. we don't hit a snag at random points
     * in the size choice
     */
    @Test
    public void increasingLengthTest() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        // A bunch of seed values to test
        for (int i = 0; i < 1000; i++)
        {
            // Test for each size
            for (long size = 0; size < 256; size++)
            {
                long seed = (long) (Math.random() * 10000L);
                InputStream is =  tg.getInputStream(seed, size);
                try
                {
                    String content = getString(is);
                    assertEquals("Content length not correct: ", size, content.getBytes("UTF-8").length);
                }
                finally
                {
                    try { is.close(); } catch (Exception e) {}
                }
            }
        }
    }
    
    /**
     * Make sure the concrete streams are not shared
     */
    @Test
    public void uniqueStreamTest() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream isOne = tg.getInputStream(1L, 256L);
        InputStream isTwo = tg.getInputStream(1L, 256L);
        assertTrue("Stream instances must be new and not share any resources: ", isOne != isTwo);
        try
        {
            String strOne = getString(isOne);
            String strTwo = getString(isTwo);
            assertEquals(strOne, strTwo);
        }
        finally
        {
            try { isOne.close(); } catch (Exception e) {}
            try { isTwo.close(); } catch (Exception e) {}
        }
    }
    
    @Test
    public void corpusQueryStemEnTest() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        assertEquals("banana", tg.generateQueryString(1, 20e-6));
        assertEquals("go no", tg.generateQueryString(2, 1e-10));   
    }
    
    @Test
    public void corpusQueryStemEn() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en.txt");
        assertEquals("1950s", tg.generateQueryString(1, 20e-6));
        assertEquals("1992", tg.generateQueryString(1, 100e-6));
        assertEquals("get way", tg.generateQueryString(2, 1e-6));
        assertEquals("as do", tg.generateQueryString(2, 10e-6));
        assertEquals("had they", tg.generateQueryString(2, 20e-6));
        assertEquals("is to", tg.generateQueryString(2, 100e-6));
        assertEquals("is to was", tg.generateQueryString(3, 1e-6));
        assertEquals("not this but", tg.generateQueryString(3, 1e-7));
        assertEquals("said who one", tg.generateQueryString(3, 1e-8));
        assertEquals("get way one", tg.generateQueryString(3, 1e-9));
        assertEquals("children always four", tg.generateQueryString(3, 1e-10));
    }
    
    @Test
    public void generateStemEnTestDiffer() throws IOException
    {
        TextGenerator tg = new TextGenerator( "alfresco/textgen/lexicon-stem-en-test.txt");
        InputStream is0 =  tg.getInputStream(0, 200, "P100.00", "P010.00", "P001.00");
        InputStream is1 =  tg.getInputStream(1, 200, "P100.00", "P010.00", "P001.00");
        assertNotEquals(getString(is0), getString(is1));
    }
    
    
    @Test
    public void configLoadStem() throws IOException
    {
        TextGenerator tg = new TextGenerator("alfresco/textgen/lexicon-stem-en.txt");
        assertEquals(13339, tg.getWordGenerator().getWordCount());
        Set<String> words = tg.getWordGenerator().getWordSet();
        assertTrue(!words.contains("'s"));
        assertTrue(words.contains("Alex"));
        assertTrue(words.contains("zero"));
    }
    
    @Test
    public void generateStem() throws IOException
    {
        TextGenerator tg = new TextGenerator("alfresco/textgen/lexicon-stem-en.txt");
        InputStream is = tg.getInputStream(0, 1024);
        assertEquals(1024, getString(is).length());
        
        is = tg.getInputStream(0, 1024, "one", "two");
        assertEquals(1024, getString(is).length());
    }
    
    @Test
    public void generateStem10M_5second() throws IOException
    {
        TextGenerator tg = new TextGenerator("alfresco/textgen/lexicon-stem-en.txt");
        long start = System.nanoTime();
        InputStream is = tg.getInputStream(0, 10*1024*1024);
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        @SuppressWarnings("unused")
        int current = -1;
        int count = 0;
        while( (current = reader.read()) != -1)
        {
            count++;
        }
        long end = System.nanoTime();
        assertEquals(count, 10*1024*1024);
        assertTrue("took "+(end-start), end-start < 5000000000L);
       
    }
}
