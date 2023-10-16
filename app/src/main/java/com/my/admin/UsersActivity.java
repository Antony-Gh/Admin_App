package com.my.admin;

import static com.my.admin.SketchwareUtil.setloc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class UsersActivity extends AppCompatActivity {
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    public DatabaseReference allaccounts = this._firebase.getReference("allaccounts");
    private LinearLayout bllin;
    
    public EditText block;
    private Button button1;
    private Button button6;

    private Button button2;

    private Button button3;
    
    public EditText dev;

    public EditText exif;
    private LinearLayout exlin;
    
    public EditText imei;

    public EditText key;

    private Button button7;
    
    public String lice = "";
    private LinearLayout liclin;

    public EditText name;
    public SharedPreferences sp;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.users);
        initialize();
        FirebaseApp.initializeApp(this);
        initializeLogic();
    }


    private void initialize() {
        this.liclin = (LinearLayout) findViewById(R.id.liclin);
        this.bllin = (LinearLayout) findViewById(R.id.bllin);
        this.exlin = (LinearLayout) findViewById(R.id.exlin);
        this.name = (EditText) findViewById(R.id.name);
        this.dev = (EditText) findViewById(R.id.dev);
        this.imei = (EditText) findViewById(R.id.imei);
        this.key = (EditText) findViewById(R.id.key);
        Button button5 = (Button) findViewById(R.id.button5);
        this.block = (EditText) findViewById(R.id.block);
        button3 = (Button) findViewById(R.id.button3);
        this.exif = (EditText) findViewById(R.id.exif);
        button2 = (Button) findViewById(R.id.button2);
        this.button1 = (Button) findViewById(R.id.button1);
        this.button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        this.sp = getSharedPreferences("sp", 0);
        button5.setOnClickListener(view -> UsersActivity.this.key.setText(new PasswordGenerator.PasswordGeneratorBuilder().useDigits(true).useLower(false).useUpper(true).usePunctuation(true).build().generate(15)));
        button3.setOnClickListener(view -> {
            if (block.getText().toString().equals("false")) {
                block.setText(R.string.truee);
                button3.setText(R.string.unblock);
            } else {
                block.setText(R.string.falsee);
                button3.setText(R.string.block);
            }
        });

        button2.setOnClickListener(view -> {
            if (UsersActivity.this.exif.getText().toString().equals("false")) {
                UsersActivity.this.exif.setText(R.string.truee);
                button2.setText(R.string.no_extract);
            } else {
                UsersActivity.this.exif.setText(R.string.falsee);
                button2.setText(R.string.extract);
            }
        });
        this.button1.setOnClickListener(view -> {
            UsersActivity.this.allaccounts.orderByChild("lic").equalTo(UsersActivity.this.lice).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ref : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", UsersActivity.this.name.getText().toString());
                        hashMap.put("lic", UsersActivity.this.key.getText().toString());
                        hashMap.put("usri", UsersActivity.this.imei.getText().toString());
                        hashMap.put("device", UsersActivity.this.dev.getText().toString());
                        hashMap.put("exif", UsersActivity.this.exif.getText().toString());
                        hashMap.put("block", UsersActivity.this.block.getText().toString());
                        ref.getRef().updateChildren(hashMap);
                        SketchwareUtil.showMessage(UsersActivity.this.getApplicationContext(), "Saved");
                    }
                }

                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("yii", databaseError.getMessage());
                }
            });
            SketchwareUtil.hideKeyboard(UsersActivity.this.getApplicationContext());
            UsersActivity.this.finish();
        });
        this.button6.setOnClickListener(view -> {
            String generate = new PasswordGenerator.PasswordGeneratorBuilder().useDigits(true).useLower(false).useUpper(true).usePunctuation(true).build().generate(15);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", UsersActivity.this.name.getText().toString());
            hashMap.put("lic", generate);
            hashMap.put("usri", UsersActivity.this.imei.getText().toString());
            hashMap.put("device", UsersActivity.this.dev.getText().toString());
            hashMap.put("exif", "true");
            hashMap.put("block", "true");
            UsersActivity.this.allaccounts.child(String.valueOf(UsersActivity.this.sp.getInt("countt", 0) + 1)).updateChildren(hashMap);
            SketchwareUtil.showMessage(UsersActivity.this.getApplicationContext(), "Added Successfully");
            SketchwareUtil.hideKeyboard(UsersActivity.this.getApplicationContext());
            UsersActivity.this.finish();
        });
        button7.setOnClickListener(view -> {
            UsersActivity.this.allaccounts.orderByChild("lic").equalTo(UsersActivity.this.lice).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ref : dataSnapshot.getChildren()) {
                        UsersActivity.this.allaccounts.child(Objects.requireNonNull(ref.getRef().getKey())).removeValue();
                    }
                }

                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("yii", databaseError.getMessage());
                }
            });
            SketchwareUtil.showMessage(UsersActivity.this.getApplicationContext(), "Removed Successfully");
            UsersActivity.this.finish();
        });
        ChildEventListener _allaccounts_child_listener = new ChildEventListener() {
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String str) {
            }

            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String str) {
            }

            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String str) {
            }

            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getKey();
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        this.allaccounts.addChildEventListener(_allaccounts_child_listener);
    }



    private void initializeLogic() {
        if (getIntent().getStringExtra("kk").equals("true")) {
            setTitle("Adding New User");
            this.liclin.setVisibility(View.GONE);
            this.bllin.setVisibility(View.GONE);
            this.exlin.setVisibility(View.GONE);
            this.button1.setVisibility(View.GONE);
            button7.setVisibility(View.GONE);
            return;
        }
        setTitle("Edit User Details");
        this.button6.setVisibility(View.GONE);
        this.block.setEnabled(false);
        this.exif.setEnabled(false);
        this.lice = this.sp.getString("lic", "");
        this.name.setText(this.sp.getString("name", ""));
        this.dev.setText(this.sp.getString("device", ""));
        this.key.setText(this.lice);
        this.imei.setText(this.sp.getString("usri", ""));
        this.block.setText(this.sp.getString("block", ""));
        this.exif.setText(this.sp.getString("exif", ""));
        if (block.getText().toString().equals("false")) {
            button3.setText(R.string.block);
        } else {
            button3.setText(R.string.unblock);
        }
        if (exif.getText().toString().equals("false")) {
            button2.setText(R.string.extract);
        } else {
            button2.setText(R.string.no_extract);
        }
    }
}
