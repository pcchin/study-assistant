/*
 * Copyright 2019 PC Chin. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pcchin.studyassistant.functions;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/** Functions used in managing files. **/
public class FileFunctions {

    /** Generates a .txt file based on a path and its contents. **/
    public static void exportTxt(String path, String contents) {
        try {
            FileWriter outputNote = new FileWriter(path);
            outputNote.write(contents);
            outputNote.flush();
            outputNote.close();
        } catch (IOException e) {
            Log.d("StudyAssistant", "File Error: IO Exception occurred when exporting "
                    + "note with path , stack trace is");
            e.printStackTrace();
        }
    }

    /** Generates a valid file in the required directory.
     * If a file with the same name exists,
     * a file with incrementing number will be added to the file.
     * @param extension needs to include the . at the front.**/
    public static String generateValidFile(String filename, String extension) {
        String returnFile = filename + extension;
        int i = 1;
        while (new File(returnFile).exists() && i < Integer.MAX_VALUE) {
            returnFile = filename + "(" + i + ")" + extension;
            i++;
        }
        return returnFile;
    }

    /** For deleting the directory inside list of files and inner Directory.
     * Placed here despite only used once as recursion is needed. **/
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    /** @return a string of text from specific text files in the assets folder **/
    @SuppressWarnings("SameParameterValue")
    @NonNull
    public static String getTxt(@NonNull Context context, String textFileName) {
        String text;
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(textFileName);
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /** Checks the integrity of a note. **/
    public static void checkNoteIntegrity(@NonNull ArrayList<String> original) {
        while (original.size() < 3) {
            original.add("");
        }
        if (original.size() < 6) {
            original.add(null);
        }
    }

    /** Gets the number of bytes required of data from a file.
     * A Toast is created when it fails and it returns an empty array. **/
    @NonNull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static byte[] getBytesFromFile(int byteAmt, FileInputStream stream) {
        byte[] returnByte = new byte[byteAmt];
        try {
            stream.read(returnByte);
            return returnByte;
        } catch (IOException e) {
            Log.w("StudyAssistant", "File Error: byte[] of size " + byteAmt + " could not "
                    + "be retrieved from input stream of file " + stream + ". Stack trace is");
            e.printStackTrace();
            return new byte[0];
        }
    }
}