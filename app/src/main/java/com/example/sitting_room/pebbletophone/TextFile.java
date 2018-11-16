package com.example.sitting_room.pebbletophone;


import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class TextFile{
    private String state = Environment.getExternalStorageState();
    private File newFolder;
    private File file;

    private static final String TAG = "TextFile";
    private static final boolean logging = true;


    public TextFile(){
        if(canWriteToCard()){
            createFolderIfNoneExists();
            createFile();
        }
    }

    private boolean canWriteToCard(){
        if (logging) Log.i(TAG, "Checking if can writeTocard.");
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            if (logging) Log.i(TAG, "We can read and write to card");
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            if (logging) Log.i(TAG, "We can only read to card");
            return false;
        } else {
            if (logging) Log.i(TAG, "We can NOT read or write to card");
            return false;
        }
    }

    private void createFolderIfNoneExists(){
        if (logging) Log.i(TAG, "Creating folder if none exists");
        try {
            newFolder = new File(Environment.getExternalStorageDirectory(), "TestFolder");
            if (!newFolder.exists()) {
                if (logging) Log.i(TAG, "Creating folder in ." + newFolder);
                newFolder.mkdir();
            }else{
                if (logging) Log.i(TAG, "Folder exists: " + newFolder);
            }
        } catch (Exception e) {
            if (logging) Log.i(TAG, "Exception 2: " + e);
        }
    }

    private void createFile(){
        try {
            file = new File(newFolder, "MyCoordinates" + ".txt");
            if(!file.exists()) {
                file.createNewFile();
                //clearFileContents();
            }
        } catch (Exception ex) {
            if (logging) Log.i(TAG, "Exception(createNewFileClearContents): " + ex);
        }
    }

    public void writeData(String data, boolean append) {
        try {
            FileWriter writer = new FileWriter(file, append);//true turns on append mode
            writer.append(data);
            writer.flush();
            writer.close();
            if (logging)  Log.i(TAG, "writing to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}//class


