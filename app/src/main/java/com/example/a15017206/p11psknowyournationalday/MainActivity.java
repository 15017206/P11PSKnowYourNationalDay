package com.example.a15017206.p11psknowyournationalday;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    boolean authenticated = false;
    String phoneNo;
    String message;
    String[] listNDPfacts = {"Singapore National Day is on 9 Aug", "Singapore is a 52 years old", "Theme is '#OneNationTogether'"};
    String bodyText = "";
    int score = 0;
    int doAll = 0;

    RadioGroup rg1, rg2, rg3;

    ListView lv1;


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if (!pref.getBoolean("authenticated", false && !authenticated)) {
            inititePassphrase();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv1 = (ListView) findViewById(R.id.lv1);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listNDPfacts);
        lv1.setAdapter(adapter);

        bodyText = "These are some facts: \n \n";
        for (int i = 0; i < listNDPfacts.length; i++) {
            bodyText += i + 1 + ": " + listNDPfacts[i] + "\n";
        }

    }

    public boolean inititePassphrase() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase = (LinearLayout) inflater.inflate(R.layout.passphrase, null);
        final EditText etPassphrase = (EditText) passPhrase.findViewById(R.id.editTextPassPhrase);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter")
                .setView(passPhrase)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (etPassphrase.getText().toString().equalsIgnoreCase("738964")) {
                            authenticated = true;
                            Toast.makeText(MainActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("authenticated", authenticated);
                            editor.apply();
                        } else {
                            authenticated = false;
                            inititePassphrase();
                            Toast.makeText(MainActivity.this, "Sorry, incorrect passphrase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsactionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sendtofriend) {
            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Toast.makeText(MainActivity.this, "Sending via Email", Toast.LENGTH_SHORT).show();
                                sendEmail();

                            } else if (which == 1) {
                                Toast.makeText(MainActivity.this, "Sending via SMS", Toast.LENGTH_SHORT).show();
                                sendSMSMessage();
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.quiz) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ScrollView quizquestions = (ScrollView) inflater.inflate(R.layout.quizquestions, null);
//            final EditText etPassphrase = (EditText) passPhrase.findViewById(R.id.editTextPassPhrase);

            rg1 = (RadioGroup) quizquestions.findViewById(R.id.rg1);
            rg2 = (RadioGroup) quizquestions.findViewById(R.id.rg2);
            rg3 = (RadioGroup) quizquestions.findViewById(R.id.rg3);

            rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    score = 0;
                    doAll = 0;
                    if (checkedId == R.id.t1) {
                        doAll++;
                    } else if (checkedId == R.id.f1) {
                        score++;
                        doAll++;
                    }
                }
            });


            rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    if (checkedId == R.id.t2) {
                        score++;
                        doAll++;
                    } else if (checkedId == R.id.f2) {
                        doAll++;
                    }
                }
            });

            rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    if (checkedId == R.id.t3) {
                        score++;
                        doAll++;
                    } else if (checkedId == R.id.f3) {
                        doAll++;
                    }


                }
            });


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test yourself!")
                    .setView(quizquestions)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Your score is: " + score, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Dont know lah", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Thats great", Toast.LENGTH_SHORT).show();
                        }
                    });
            ;
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Goodbye", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Thats great", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Goodbye", Toast.LENGTH_SHORT).show();

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.apply();
                            authenticated = false;
                            inititePassphrase();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Thats great", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void sendSMSMessage() {
        phoneNo = "81839173";

        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phoneNo);
        smsIntent.putExtra("sms_body", bodyText);

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"jasonlim@gmail.com"};
        String[] CC = {"hkk@gmail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Celebrate NDP!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
