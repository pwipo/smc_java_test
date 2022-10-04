package ru.smcsystem.test.emulate;

import ru.smcsystem.api.tools.FileTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileToolImpl implements FileTool {

    private final File fileObject;

    public FileToolImpl(File fileObject) {
        this.fileObject = fileObject;
    }

    @Override
    public String getName() {
        return fileObject.getName();
    }

    @Override
    public boolean exists() {
        try {
            return fileObject.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isDirectory() {
        try {
            return fileObject.isDirectory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FileTool[] getChildrens() {
        try {
            File[] childrens = fileObject.listFiles();
            if (childrens == null || childrens.length == 0)
                return null;
            List<FileTool> fileTools = new ArrayList<>(childrens.length);
            for (File fileObject : childrens)
                fileTools.add(new FileToolImpl(fileObject));
            return fileTools.toArray(new FileTool[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(fileObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long length() {
        return fileObject.length();
    }
}
