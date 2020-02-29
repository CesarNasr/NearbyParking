package com.example.nearbyparking.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nearbyparking.R;


public class ContactUs extends Fragment {

    private Button sendEmailBtn;
    private EditText pNumber, body, name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        sendEmailBtn = view.findViewById(R.id.sendEmailBtn);
        pNumber = view.findViewById(R.id.phone);
        body = view.findViewById(R.id.message);
        name = view.findViewById(R.id.name);
        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendEmail("contact@nearbyparkings.com", body.getText().toString(), pNumber.getText().toString(), name.getText().toString());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    protected void sendEmail(String receiverEmail, String emailBody, String phoneNumber, String name) {


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", receiverEmail, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");

        String nameString = "\r\n Sender Name : " + name;
        String messageString = "\r\n Message : " + emailBody;
        String phoneNumString = "\r\n Phone number : " + phoneNumber;

        emailIntent.putExtra(Intent.EXTRA_TEXT, nameString + phoneNumString + messageString);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receiverEmail); // String[] addresses

        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }

}
