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
import java.util.Locale;

/**
 * Provides an input stream of text generated at random from real words and supported creating query text for use 
 * against this generated content. 
 * 
 * @author Andy Hind
 *
 */
public interface RandomTextProvider
{
    /**
     * Generate a repeatable input stream made up of random words. The stream may be padded with
     * empty space to achieve the required length.
     * 
     * @param locale - the locale for the generated text 
     * @param seed - the initial seed for the state of the generator - to give repeatable content
     * @param length - the length of content required in bytes
     * @param strings - an optional array of strings. The first will be in 100% of streams generated, the next 10%, the next 1% etc.
     *                  Null values will be skipped, so you could add a fixed word to 10% and 0.1% of documents using ...., null, "10", null, ".1")
     * @return
     * @throws IOException 
     */
    public InputStream getInputStream(Locale locale, long seed, long length, String...strings) throws IOException;
    
    /**
     * Generate a query string that exactly matches the specific random content in some way
     * 
     * @param locale - the locale for the generated text
     * @param seed - the initial seed for the state of the generator - to give repeatable content
     * @param words - the number of words required in the query string
     * @param wordLimit - the number of words to consider from the generated stream.
     * @return
     */
    public String generateQueryString(Locale locale, long seed, int words, int wordLimit);
    
    /**
     * Generate a query string that may match in some way against the corpus of generated documents as a whole.
     * 
     * @param locale - the locale for the generated text
     * @param words - the number of words required in the query string
     * @param approximateFrequency
     * @return
     */
    public String generateQueryString(Locale locale, int words, double approximateFrequency);
    
}
