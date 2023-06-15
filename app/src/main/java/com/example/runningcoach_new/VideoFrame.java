package com.example.runningcoach_new;

import java.io.File;

public class VideoFrame {
    File file;

    public VideoFrame(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
