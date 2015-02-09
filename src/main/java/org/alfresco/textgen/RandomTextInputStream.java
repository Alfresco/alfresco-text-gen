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
import java.util.Random;

/**
 * An output stream of random words from a word generator.
 * 
 * @author Andy
 *
 */
public class RandomTextInputStream extends InputStream
{
    int bytePosition = 0;
    
    long charactersSoFar;
    
    private WordGenerator wg;
    
    private long length;
    
    byte[] currentBytes;
    
    Random random = new Random();
    
    boolean pad = false;
    
    /**
     * @param wg
     * @param seed
     * @param length
     * @param strings
     * @throws IOException 
     */
    public RandomTextInputStream(WordGenerator wg, long seed, long length, String[] strings) throws IOException
    {
        this.wg = wg;
        this.length = length;
        
        if((strings != null) && (strings.length > 0))
        {
            double requiredProbability = 1.0;
            double probability;
            random.setSeed(seed);
            StringBuffer buffer = new StringBuffer();
            for(String string : strings)
            {
                if(string != null)
                {
                    probability = random.nextDouble();
                    if(probability < requiredProbability)
                    {
                        if(buffer.length() > 0)
                        {
                            buffer.append(" ");
                        }
                        buffer.append(string);
                    }
                }
                requiredProbability /= 10;
            }
            
            String fixedStrings = buffer.toString();
            if(fixedStrings.length() > length)
            {
                throw new IllegalStateException("Length "+length+ " is too short for required strings: "+ fixedStrings);
            }
            charactersSoFar = fixedStrings.length();
            
            currentBytes = fixedStrings.getBytes("UTF-8");
      
            pad = true;
        }
        else
        {
            pad = false;
        }
        
        random.setSeed(seed);
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        if((currentBytes != null) && (bytePosition < currentBytes.length))
        {
            return currentBytes[bytePosition++];
        }
      
        getMoreBytes();
        
        if(currentBytes == null)
        {
            return -1;
        }
        else
        {
            return currentBytes[bytePosition++];
        }
    }

    /**
     * @throws IOException 
     * 
     */
    private void getMoreBytes() throws IOException
    {
        
        if(charactersSoFar == length)
        {
            currentBytes = null;
        }
        else
        {
            StringBuffer buffer = new StringBuffer();
            if(pad)
            {
                buffer.append(" ");
            }
            else
            {
                pad = true;
            }
          
            String word = wg.getWord(random.nextDouble());
            if(word == null)
            {
                throw new IOException("Word generation failed");
            }
            buffer.append(word);
            
            if(charactersSoFar + buffer.length() > length)
            {
                // pad with spaces...
                currentBytes = emptyString((int)(length - charactersSoFar)).getBytes("UTF-8");
                bytePosition = 0;
                charactersSoFar += length - charactersSoFar;
            }
            else
            {
                currentBytes = buffer.toString().getBytes("UTF-8");
                bytePosition = 0;
                charactersSoFar += buffer.length();
            }
            
        }
    }

    /**
     * @param l
     * @return
     */
    private String emptyString(int length)
    {
        StringBuffer buffer = new StringBuffer(length);
        for(int i = 0; i < length; i++)
        {
            buffer.append(" ");
        }
        return buffer.toString();
    }

}
