package com.kenmurrell.zerbert;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DataUtils
{
    private static final String SHA1 = "SHA-1";
    private final static char[] hexArray = "0123456789abcdef".toCharArray();
    private static final String AES = "AES";
    private static final String RandomVector = "RandomInitVector";

    private static byte[] PreProcessing(String key)
    {
        StringBuilder sb = new StringBuilder(key);
        while(sb.length() % 2 != 0)
        {
            sb.append("x");
        }
        String x = sb.toString();
        byte[] decodedKey = Base64.decode(x, 0);
        int keySize = (int) ((Math.ceil(decodedKey.length/16.0)) * 16);
        byte[] keyArr = new byte[keySize];
        for (int i=0; i<keyArr.length; i++)
        {
            keyArr[i] = (i<decodedKey.length) ? decodedKey[i] : 62;
        }
        return keyArr;
    }

    public static String encryptText(String text, String key)
    {
        if(text == null || text.equals("") || key == null || key.equals(""))
        {
            return null;
        }
        byte[] keyArr = PreProcessing(key);
        try
        {
            IvParameterSpec iv = new IvParameterSpec(RandomVector.getBytes(StandardCharsets.UTF_8));
            Key aesKey = new SecretKeySpec(keyArr, AES);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
            byte[] encrypted =  cipher.doFinal(text.getBytes());
            return Base64.encodeToString(encrypted, 0);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptText(String text, String key) throws CryptoException
    {
        if(text == null || text.equals("") || key == null || key.equals(""))
        {
            return null;
        }
        byte[] keyArr = PreProcessing(key);
        try
        {
            IvParameterSpec iv = new IvParameterSpec(RandomVector.getBytes(StandardCharsets.UTF_8));
            Key aesKey = new SecretKeySpec(keyArr, AES);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, iv);
            byte[] decrypted = cipher.doFinal(Base64.decode(text, 0));
            return new String(decrypted);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            throw new CryptoException(e);
        }
        return null;
    }

    public static String SHA1(String text)
    {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA1);
            byte[] textBytes = text.getBytes(StandardCharsets.ISO_8859_1);
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();
            return bytesToHex(sha1hash);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }

    public static class CryptoException extends GeneralSecurityException
    {
        public CryptoException(Exception e)
        {
            super(e.getMessage(), e.getCause());
        }
    }

}
