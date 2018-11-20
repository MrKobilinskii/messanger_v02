package com.develop.daniil.sms_v01.Send;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.develop.daniil.sms_v01.R;

import java.util.ArrayList;
import java.util.List;

public class SendFragment extends Fragment {

    String SENT_SMS = "SENT_SMS";
    String DELIVER_SMS = "RECEIVE_SMS";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    Intent sent_intent = new Intent(SENT_SMS);
    Intent deliver_intent = new Intent(DELIVER_SMS);

    PendingIntent sent_pi, deliver_pi;

    Button send_button;
    EditText phone_editText,msg_editText;
    String phoneNo, message;
    int key; //Ключ для шифра

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_send, container,false);

        sent_pi = PendingIntent.getBroadcast(view.getContext(),0,sent_intent,0);
        deliver_pi = PendingIntent.getBroadcast(view.getContext(),0,deliver_intent,0);

        send_button= view.findViewById(R.id.send_Button);
        phone_editText= view.findViewById(R.id.phone_editText);
        msg_editText= view.findViewById(R.id.msgText_editText);

        send_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                phoneNo = phone_editText.getText().toString();
                message = msg_editText.getText().toString();

                message = secureMessage(message); //шифруем

                if(phoneNo.length()>0 && message.length()>0) //Todo: добавить проверки
                    if(phoneNo.length() < 50 && message.length() < 100)
                        sendSMS(phoneNo, message);
                    else
                        Toast.makeText(getActivity().getBaseContext(), "Некорректный ввод данных", Toast.LENGTH_SHORT).show();
                else
                    if(getActivity() != null) {
                        Toast.makeText(getActivity().getBaseContext(),
                               "Введите номер и текст сообщения.",
                                Toast.LENGTH_SHORT).show();
                    }
            }
        });

       return view;
    }

    private String secureMessage(String message){
        int a = 0; // Начальное значение диапазона - "от"
        int b = 4; // Конечное значение диапазона - "до"
        List<Character> myAlphabet = new ArrayList<>(); //динамич массив
        char[] CharArray = message.toCharArray();
        char[] newCharArray = new char[CharArray.length];

        key = a + (int) (Math.random() * b);

        int j =0;
        while (j<=4) {
            int t = 97 + j;
            myAlphabet.add((char) t);
            j++;
        }

        for (int i = 0; i <= CharArray.length - 1; i++) {
            int newChar = ((int)CharArray[i] - 97 + key) % 5 + 97;
            newCharArray[i] = (char) newChar;
        }

        char chkey = (char) (key + 97);

        message = "[" + String.valueOf(newCharArray) + "]" + chkey + key;
        return message;
    }

    private void sendSMS(String phoneNumber, String message)
    {
        if(getActivity() != null) { //СЛОЖНАЯ ПРОВЕРКА СИСТЕМЫ, К БОЛЕЕ УСТАРЕВШЕЙ ДРУГОЙ ПОДХОД!!!
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.SEND_SMS)) {
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
        }

        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber,null, message,null,null);
            Toast.makeText(getActivity(),"Сообщение отправлено", Toast.LENGTH_SHORT).show();
        }
        catch (Throwable ignored) //суперкласс всех ошибок и исключений
        {
            Toast.makeText(getActivity(),"Ошибка, повторите попытку", Toast.LENGTH_SHORT).show();
        }
    }


}