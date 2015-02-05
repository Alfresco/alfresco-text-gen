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
        new TextGenerator("org/alfresco/textgen/lexicon-en_US.txt");
    }
    
    @Test
    public void configLoadDefaultEnUS()
    {
        new TextGenerator("org/alfresco/textgen/lexicon-default-en_US.txt");
    }
    
    @Test
    public void configLoadCustomEnUS()
    {
        new TextGenerator("org/alfresco/textgen/lexicon-test1-en_US.txt");
    }
}
