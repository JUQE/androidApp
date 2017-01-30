package com.example.brianofrim.juqe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class VenueSignupActivity extends Activity {
    EditText venueNameEditText;
    EditText venueCodeEditText;
    Button createButton;
    String venueName;
    String venueCode;
    VenueController venueController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_signup);

        createButton = (Button) findViewById(R.id.createVenueButton);
        venueCodeEditText = (EditText) findViewById(R.id.venueCode);
        venueNameEditText = (EditText) findViewById(R.id.venueName);


        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                venueCode = venueCodeEditText.getText().toString();
                venueName = venueNameEditText.getText().toString();


                VenueController.getDbRef().child("venues").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(!snapshot.hasChild(venueCode)){
                            VenueController.createVenue(venueCode, venueName);
                            if(VenueController.getCurrSongList().getSongPool() != null) {
                                VenueController.getCurrSongList().getSongPool().clear();
                            }
                            if(VenueController.getURIList() != null) {
                                VenueController.getURIList().clear();
                            }
                            Intent intent = new Intent (VenueSignupActivity.this, DjActivity.class);
                            startActivity(intent);
                        }else{
                            displayErrorMessage("Venue Code already in use");

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
