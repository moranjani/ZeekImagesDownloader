package com.example.morana.zeekimages;

/**
 * Created by Morana on 9/14/2017.
 */
public class ZeekSingleton {
    private static ZeekSingleton ourInstance = new ZeekSingleton();

    public static ZeekSingleton getInstance() {
        return ourInstance;
    }

    private ZeekSingleton() {
    }
}
