package com.hai;

import io.reactivex.rxjava3.disposables.Disposable;

import java.util.Objects;

public class Job {
    public synchronized void methoda() {

    }

    public synchronized void methodb() {

    }

    static class Mydis implements Disposable {

        @Override
        public void dispose() {

        }

        @Override
        public boolean isDisposed() {
            return false;
        }
    }
}
