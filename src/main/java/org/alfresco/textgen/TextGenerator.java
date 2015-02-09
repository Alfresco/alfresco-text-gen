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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.springframework.core.io.ClassPathResource;

/**
 * @author Andy Hind
 * @since 1.0
 */
public class TextGenerator implements RandomTextProvider
{
    
    WordGenerator wordGenerator = new WordGenerator();
    
    public TextGenerator(String configPath)
    {
        ClassPathResource cpr = new ClassPathResource(configPath);
        if (!cpr.exists())
        {
            throw new RuntimeException("No resource found: " + configPath);
        }
        InputStream is = null;
        InputStreamReader r = null;
        BufferedReader br = null;
        try
        {
            int lineNumber = 0;
            is = cpr.getInputStream();
            r = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(r);
            
            
            String line;
            while((line = br.readLine()) != null)
            {
                lineNumber++;
                if(lineNumber == 1)
                {
                    // skip header
                    continue;
                }
                String[] split = line.split("\t");
                
                if(split.length != 7)
                {
                    //System.out.println("Skipping "+lineNumber);
                    continue;
                }
                
                String word = split[1].replaceAll("\\*", "");
                String mode = split[3].replaceAll("\\*", "");
                long frequency = Long.parseLong(split[4].replaceAll("#", "").trim());
                
                // Single varient
                if(mode.equals(":"))
                {
                    if(!ignore(word))
                    {
                        wordGenerator.addWord(splitAlternates(word),  frequency == 0 ? 1 : frequency);
                    }
                }
                else
                {
                    if(word.equals("@"))
                    {
                        // varient
                        if(!ignore(mode))
                        {
                            wordGenerator.addWord(splitAlternates(mode),  frequency == 0 ? 1 : frequency);
                        }
                    }
                    else
                    {
                        //System.out.println("Skipping totla and using varients for " + word + " @ " + lineNumber);
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load resource " + configPath);
        }
        finally
        {
            if (br != null)
            {
                try { br.close(); } catch (Exception e) {}
            }
            if (r != null)
            {
                try { r.close(); } catch (Exception e) {}
            }
            if (is != null)
            {
                try { is.close(); } catch (Exception e) {}
            }
        }
    }

    /**
     * @param mode
     * @return
     */
    private String splitAlternates(String word)
    {
        String[] alternates = word.split("/");
        return alternates[0].trim();
               
    }

    /**
     * @param mode
     * @return
     */
    private boolean ignore(String word)
    {
        return word.contains("~") || word.contains("'");
    }

    /* (non-Javadoc)
     * @see org.alfresco.textgen.RandomTextProvider#getInputStream(java.util.Locale, long, long, java.lang.String[])
     */
    public InputStream getInputStream(Locale locale, long seed, long length, String... strings) throws IOException
    {
        return new RandomTextInputStream(wordGenerator, seed, length, strings);
    }

    /* (non-Javadoc)
     * @see org.alfresco.textgen.RandomTextProvider#generateQueryString(java.util.Locale, long, int, int)
     */
    public String generateQueryString(Locale locale, long seed, int words, int wordLimit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.alfresco.textgen.RandomTextProvider#geneareQueryString(java.util.Locale, int, int)
     */
    public String geneareQueryString(Locale locale, int words, int approximateFrequencyPerMillionWords)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the wg
     */
    public WordGenerator getWordGenerator()
    {
        return wordGenerator;
    }
    
    
}
