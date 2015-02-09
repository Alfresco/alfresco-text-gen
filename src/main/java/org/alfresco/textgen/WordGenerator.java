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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Select a word basd on cumulative frequencey
 * 
 * @author Andy
 *
 */
public class WordGenerator 
{
    private static int DEFAULT_SIZE = 1024;
    
    int nextPosition = 0;
    
    long cumulativeFrequency = 0L;
    
    long[] cumulativeFrequencies;
    
    String[] words;
    
    public WordGenerator()
    {
        cumulativeFrequencies = new long[DEFAULT_SIZE];
        words = new String[DEFAULT_SIZE];
    }
    
    public WordGenerator(int initialSize)
    {
        cumulativeFrequencies = new long[initialSize];
        words = new String[initialSize];
    }
    
    /**
     * Add a word.
     *  
     * @param word
     * @param frequency - per million words
     */
    public void addWord(String word, long frequency)
    {
        if(nextPosition == cumulativeFrequencies.length)
        {
            resize();
        }
        cumulativeFrequency += frequency;
        cumulativeFrequencies[nextPosition] = cumulativeFrequency;
        words[nextPosition++] = word;
    }

    /**
     * Get a word from the distribution based on a uniform distribution between 0 and 1 
     * 
     * @param value
     * @return
     */
    public String getWord(double value)
    {
        if((value < 0) || (value > 1))
        {
            return null;
        }
        
        if(nextPosition == 0)
        {
            return null;
        }
        
        long key = (long)(cumulativeFrequency * value);
        int position = Arrays.binarySearch(cumulativeFrequencies, 0, nextPosition, key);
        if(position < 0)
        {
            position = -(position + 1);
            if(position <  nextPosition - 1)
            {
                return words[position];
            }
            else
            {
                return words[nextPosition - 1];
            }
        }
        else
        {
            if(position <  nextPosition - 1)
            {
                return words[position+1];
            }
            else
            {
                return words[nextPosition - 1];
            }
        }
    }
    
    /**
     * 
     */
    private void resize()
    {   
        String[] newWords = new String[words.length * 2];
        System.arraycopy(words, 0, newWords, 0, words.length);
        
        long[] newCumulativeFrequencies = new long[cumulativeFrequencies.length * 2];
        System.arraycopy(cumulativeFrequencies, 0, newCumulativeFrequencies, 0, cumulativeFrequencies.length);
        
        words = newWords;
        cumulativeFrequencies = newCumulativeFrequencies;
        
    }
    
    public int getWordCount()
    {
        return nextPosition;
    }
    
    
    public Set<String> getWordSet()
    {
        HashSet<String> wordSet = new HashSet<String> ();
        for(int i = 0; i < words.length; i++)
        {
            wordSet.add(words[i]);
        }
        return wordSet;
    }
}
