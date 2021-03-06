package com.example.accompany;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Partner_Chat extends AppCompatActivity {

    private int SIGN_IN_REQUEST_CODE=10;
    private FirebaseListAdapter<Partner_Chat_Message> adapter;
    private DatabaseReference mFirebaseDatabaseReference;
    public int takenuserid,usernumber;
    public String userkey,displayname;
    public Boolean stopThread = false;
    FirebaseUser userf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_chat);

        final Intent mIntent = getIntent();
        takenuserid = mIntent.getIntExtra("user id", 0);
        usernumber = mIntent.getIntExtra("user exact number", 0);
        userkey = mIntent.getStringExtra("user push key");
        //Log.w("key",userkey);

        Log.w("User exact number","no"+usernumber);

        Log.d("partnerpage", "uder id: " + takenuserid);

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);
        FloatingActionButton leavegroup =
                (FloatingActionButton) findViewById(R.id.leavechatroom);







        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("groups")
                        .child("Ghat")
                        .child("partnerchat"+takenuserid)
                        .push()
                        .setValue(new Partner_Chat_Message(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );


               /* ChatMessage chatmsg = new
                        ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName());
                mFirebaseDatabaseReference.child("groups")
                        .push().setValue(chatmsg); */
                // Clear the input
                input.setText("");
            }
        });
        leavegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("groups").child("Ghat").child(userkey);
                    final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("groups").child("Ghat");
                    final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("users").child("status");

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("groups").child("Ghat").child("partnerchat"+takenuserid);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //ref1.removeValue();
                            //ref3.setValue("offline");


                            Intent myIntent = new Intent(Partner_Chat.this, RatingHandler.class);
                            myIntent.putExtra("user idnumber",usernumber);
                            startActivity(myIntent);

                            ref.removeValue();
                            ref2.child("leaveride"+takenuserid).setValue("true");


                        }
                        else{
                            //ref1.removeValue();
                            //ref3.setValue("offline");
                            Intent myIntent = new Intent(Partner_Chat.this, RatingHandler.class);
                            myIntent.putExtra("user idnumber",usernumber);
                            startActivity(myIntent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




//                final Query uservalue = ref2.orderByChild("userID").equalTo(currentuser);
//                 uservalue.addListenerForSingleValueEvent(new ValueEventListener() {
//                     @Override
//                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                         for (DataSnapshot child: dataSnapshot.getChildren()) {
//                             child.getRef().removeValue();
//                         }
//
//                     }
//
//                     @Override
//                     public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                     }
//                 });

                }
        });
       displayChatMessages();
    }

    private void displayChatMessages() {
        userf = FirebaseAuth.getInstance().getCurrentUser();

        displayname = userf.getDisplayName();
        //final String img_url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();

                ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child("groups").child("Ghat").child("partnerchat"+takenuserid);
        Log.w("This is ref",mFirebaseDatabaseReference.toString());
        adapter = new FirebaseListAdapter<Partner_Chat_Message>(this, Partner_Chat_Message.class,
                R.layout.messages, mFirebaseDatabaseReference) {
            @Override
            protected void populateView(View v, Partner_Chat_Message model, int position) {
                // Get references to the views of message.xml
                RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.messagebg);
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
               // ImageView profilepic = (ImageView)v.findViewById(R.id.profilepic);

              //  Glide.with(Partner_Chat.this).load(img_url).into(profilepic);
                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Log.w("dsplay",messageUser.toString());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
        Log.w("End of disp","hrrfhr");
    }




}
