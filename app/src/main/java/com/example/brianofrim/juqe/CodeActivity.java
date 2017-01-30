package com.example.brianofrim.juqe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CodeActivity extends Activity {
    private EditText venueCode;
    private TextView venueText;
    private TextView reconnectText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        final Button submitCodeButton = (Button) findViewById(R.id.codeButton);

        venueCode = (EditText) findViewById(R.id.code_edit_text);
        venueText = (TextView) findViewById(R.id.createVenue);
        venueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeActivity.this, VenueSignupActivity.class);
                startActivity(intent);
            }
        });

        reconnectText = (TextView) findViewById(R.id.reconnectText);
        reconnectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VenueController.getCurrVenue() != null) {
                    Intent intent = new Intent(CodeActivity.this, DjActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(CodeActivity.this,"No current Juqe", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        submitCodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                VenueController.getDbRef().child("venues").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.hasChild(venueCode.getText().toString())){
                            setResult(RESULT_OK);
                            String codeText = venueCode.getText().toString();
                            UserController.setCode(codeText);
                            Intent intent;
                            intent =  new Intent(CodeActivity.this, UserView.class);
                            intent.putExtra("code", codeText);
                            startActivity(intent);


                        }else{
                            displayErrorMessage("Venue Code doesn't exist");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        displayErrorMessage("Connection error try again");
                    }
                });
            }
        });
    }

    public void displayErrorMessage(String errorMessage){
        Toast toast = Toast.makeText(this , errorMessage,Toast.LENGTH_SHORT);
        toast.show();
    }

}
