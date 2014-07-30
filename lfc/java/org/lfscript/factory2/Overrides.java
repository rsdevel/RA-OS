/*
 * - Overrides.java -
 *
 * Copyright (c) 2011-2014 Marcel van den Boer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package org.lfscript.factory2;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Interfaces with an external list to provide manually verified MD5 checksums.
 */
public class Overrides {
    private Map<String, String> md5sums;
    private static Overrides instance;

    private Overrides() {
        this.md5sums = new HashMap<String, String>();

        try {
            File file = new File("md5sums.list");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = null;
            while((line = reader.readLine()) != null) {
                line = line.trim().replace("\t", " ");
                while(line.indexOf("  ") > -1) {
                    line = line.replace("  ", " ");
                }

                if(line.length() != 0 && line.charAt(0) != '#') {
                    this.md5sums.put(line.split(" ")[1], line.split(" ")[0]);
                }
            }
        } catch(Throwable t) {
            System.err.println("WARNING: No supplemental 'md5sums.list' found");
        }
    }

    /**
    * Provides an instance of <code>Overrides</code>.
    */
    public static Overrides getInstance() {
        if(Overrides.instance == null) {
            Overrides.instance = new Overrides();
        }

        return Overrides.instance;
    }

    /**
     * Returns a verified MD5 from 'md5sums.list', or <code>null</code> if the
     * original MD5 should be used.
     */
    public String getMD5(String file) {
        file = file.split("/")[file.split("/").length - 1];

        final String retVal = this.md5sums.get(file);
        if (retVal == null) {
            throw new RuntimeException("No MD5 for given file");
        }

        return retVal;
    }
}

