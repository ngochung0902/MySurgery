package com.mysurgery.service;


import com.google.android.vending.expansion.downloader.impl.DownloaderService;
import com.mysurgery.receiver.APKEFTutoAlarmReceiver;

public class APKEFTutoDownloaderService extends DownloaderService {
	// Votre cl� publique fournie par Google Play
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlZ5C+wSqAdEcRuFsqpZF3xUd/Um5f2Y0j+85RUyBUSuAIOHAYhGknoE7JqqFnQfhMQKRBaZI+PcEdnOG99W/I/ThFC9bLz4X1Iq3LZKGKuoDVeaXuhsHxKi6FV0Ys62vVJguz6CtmVHng1w3hrEgMtsPbq48NF7uHbIaqskcaW6foMRLlzuke1pfOSfAb9mN3hWHEylT8gbRTdBtC02ZC/nc8FXgEg+LmYQ0bg8F1e3mKaBHrRShBgR2X3IZQnT73Re7on8YBmdpqUNDQB9onDcKb+3UOLoU4R+hZLpWJFEwZvv1/s5KSPe7GUgV2EUEHdP1lxCtPumAIX1wjao3XwIDAQAB";
                                                    
    // Vous devez �galement modifier ces chiffres, ils doivent �tre compris entre -128 et +127
    public static final byte[] SALT = new byte[] { 1, 42, -12, -1, 54, 98, -100, -12, 43, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 84
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return APKEFTutoAlarmReceiver.class.getName();
    }
}