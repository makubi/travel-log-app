package at.droelf.travellogapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class DiskStorage {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DiskStorage() {
        objectMapper.registerModule(new JodaModule());
    }

    Future<Void> saveData(String path, Object data) {
        return new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {
               // TODO save
            }
        }, null);
    }

    <E> Future<E> getData(String path) {
        return new FutureTask<E>(new Callable<E>() {
            @Override
            public E call() throws Exception {
                // TODO get
                return null;
            }
        });
    }

    File[] listFilesInPath(String path) {
        return new File(path).listFiles();
    }
}
