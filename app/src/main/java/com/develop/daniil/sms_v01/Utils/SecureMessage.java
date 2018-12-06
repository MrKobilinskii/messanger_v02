package com.develop.daniil.sms_v01.Utils;

public class SecureMessage {

    public String encryptMessage(String message, int key){
        String encryptMsg = "";
        char c;
        for(int i = 0; i < message.length();i++){
            c = message.charAt(i);

            if (Character.isLetter(c))
            {
                c = (char) (message.charAt(i) + key);

                if ((Character.isLowerCase(message.charAt(i)) && c > 'z' && c < 'а')  //english
                        || (Character.isUpperCase(message.charAt(i)) && c > 'Z' && c < 'А'))
                {
                    c = (char) (message.charAt(i) - (26 - key));
                }

                if ((Character.isLowerCase(message.charAt(i)) && c > 'я')  //russian
                        || (Character.isUpperCase(message.charAt(i)) && c > 'Я'))
                {
                    c = (char) (message.charAt(i) - (32 - key));
                }

            }

            encryptMsg += c;

        }
        encryptMsg = "[" + encryptMsg + "]" + key;
        return encryptMsg;
    }

    public String decryptMessage(String message, int key){
        String decryptMsg = "";
        char c;
        for(int i = 0; i < message.length();i++){
            c = message.charAt(i);

            if (Character.isLetter(c))
            {
                c = (char) (message.charAt(i) - key);

                if ((Character.isLowerCase(message.charAt(i)) && c < 'a') //english
                        || (Character.isUpperCase(message.charAt(i)) && c < 'A'))
                {
                    c = (char) (message.charAt(i) + (26 - key));
                }

                if ((Character.isLowerCase(message.charAt(i)) && c < 'а' && c > 'z') //russian alphabet
                        || (Character.isUpperCase(message.charAt(i)) && c < 'А' && c >'Z'))
                {
                    c = (char) (message.charAt(i) + (32 - key));
                }
            }

            decryptMsg += c;

        }
        return decryptMsg;
    }
}
