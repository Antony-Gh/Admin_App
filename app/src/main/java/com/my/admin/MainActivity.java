package com.my.admin;

import static com.my.admin.SketchwareUtil.getloc;
import static com.my.admin.SketchwareUtil.setloc;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    public ChildEventListener _allaccounts_child_listener;
    public ArrayList<HashMap<String, Object>> acc = new ArrayList<>();
    public DatabaseReference allaccounts = this._firebase.getReference("allaccounts");
    public Intent ik = new Intent();
    public ListView listview1;
    public SharedPreferences sp;

    private File mFile;

    private final int PICK = 1111;

    private final int PER = 2222;

    private ProgressDialog progress;

    private StorageReference storageRef;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        initialize();
        FirebaseApp.initializeApp(this);
        initializeLogic();
    }

    private void o(String k) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "MY INFO \n\n");
        intent.putExtra("android.intent.extra.TEXT",k);
        startActivity(intent);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/vnd.android.package-archive");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Choose APK"), PICK);
    }

    private void initialize() {
        FloatingActionButton _fab = (FloatingActionButton) findViewById(R.id._fab);
        listview1 = findViewById(R.id.listview1);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageRef =  firebaseStorage.getReference().child("agaupdates");
        progress = new ProgressDialog(this);
        sp = getSharedPreferences("sp", 0);
        listview1.setOnItemClickListener((adapterView, view, i, j) -> {
            MainActivity.this.sp.edit().putString("name", Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("name")).toString()).apply();
            MainActivity.this.sp.edit().putString("usri", Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("usri")).toString()).apply();
            MainActivity.this.sp.edit().putString("lic", Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("lic")).toString()).apply();
            MainActivity.this.sp.edit().putString("device", Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("device")).toString()).apply();
            MainActivity.this.sp.edit().putString("exif", Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("exif")).toString()).apply();
            MainActivity.this.sp.edit().putString("block", Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("block")).toString()).apply();
            MainActivity.this.ik.setClass(MainActivity.this.getApplicationContext(), UsersActivity.class);
            MainActivity.this.ik.putExtra("kk", "false");
            MainActivity.this.startActivity(MainActivity.this.ik);
        });
        _fab.setOnClickListener(view -> {

            CharSequence[] a = {"Add New User", "Upload AGA Disabler Update", "AGA Disabler Link"};
            AlertDialog alertBuilder = new AlertDialog.Builder(this)
                    .setTitle("Choose One")
                    .setItems(a, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                MainActivity.this.ik.setClass(MainActivity.this.getApplicationContext(), UsersActivity.class);
                                MainActivity.this.ik.putExtra("kk", "true");
                                MainActivity.this.startActivity(MainActivity.this.ik);
                                break;
                            case 1:
                                showFileChooser();
                                break;
                            case 2:
                                o(getloc(this));
                             break;
                        }
                    })
                    .create();
            alertBuilder.show();
        });
        this._allaccounts_child_listener = new ChildEventListener() {
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String str) {
                dataSnapshot.getKey();
                MainActivity.this.allaccounts.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MainActivity.this.acc = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> r1 = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot value : dataSnapshot.getChildren()) {
                                MainActivity.this.acc.add((HashMap<String, Object>) value.getValue(r1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MainActivity.this.listview1.setAdapter(new Listview1Adapter(MainActivity.this.acc));
                        MainActivity.this.listview1.smoothScrollToPosition(0);
                        ((BaseAdapter) MainActivity.this.listview1.getAdapter()).notifyDataSetChanged();
                        MainActivity.this.sp.edit().putInt("countt", MainActivity.this.acc.size()).apply();
                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String str) {
                dataSnapshot.getKey();
                MainActivity.this.allaccounts.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MainActivity.this.acc = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> r1 = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot value : dataSnapshot.getChildren()) {
                                MainActivity.this.acc.add((HashMap<String, Object>) value.getValue(r1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MainActivity.this.listview1.setAdapter(new Listview1Adapter(MainActivity.this.acc));
                        MainActivity.this.listview1.smoothScrollToPosition(0);
                        ((BaseAdapter) MainActivity.this.listview1.getAdapter()).notifyDataSetChanged();
                        MainActivity.this.sp.edit().putInt("countt", MainActivity.this.acc.size()).apply();
                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String str) {
            }

            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getKey();
                MainActivity.this.allaccounts.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MainActivity.this.acc = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> r1 = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot value : dataSnapshot.getChildren()) {
                                MainActivity.this.acc.add((HashMap<String, Object>) value.getValue(r1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MainActivity.this.listview1.setAdapter(new Listview1Adapter(MainActivity.this.acc));
                        MainActivity.this.listview1.smoothScrollToPosition(0);
                        ((BaseAdapter) MainActivity.this.listview1.getAdapter()).notifyDataSetChanged();
                        MainActivity.this.sp.edit().putInt("countt", MainActivity.this.acc.size()).apply();
                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                SketchwareUtil.showMessage(MainActivity.this.getApplicationContext(), "Error Code :".concat(String.valueOf((long) databaseError.getCode()).concat("\nError Message : ".concat(databaseError.getMessage()))));
            }
        };
        this.allaccounts.addChildEventListener(this._allaccounts_child_listener);
    }

    private void initializeLogic() {
        setTitle("All Users");
        this.allaccounts.addChildEventListener(this._allaccounts_child_listener);
        this.allaccounts.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.this.acc = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> r1 = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        MainActivity.this.acc.add((HashMap<String, Object>) value.getValue(r1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainActivity.this.listview1.setAdapter(new Listview1Adapter(MainActivity.this.acc));
                MainActivity.this.listview1.smoothScrollToPosition(0);
                MainActivity.this.sp.edit().putInt("countt", MainActivity.this.acc.size()).apply();
                ((BaseAdapter) MainActivity.this.listview1.getAdapter()).notifyDataSetChanged();
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void uploadClick(File f) {
        Log.d("Uploading", "Upload Click");
        mFile = f;
        if (mFile != null && mFile.exists()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Uploading", "Permission true");
                make_down_link(uploadFile(mFile));
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PER);
            }
        }else {
            Toast.makeText(this, "File does not exist", Toast.LENGTH_LONG).show();
        }

    }

    private void prog() {
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setIndeterminate(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Loading....");
        progress.show();
    }

    private void make_down_link(String s) {
        Log.d("Uploading", "Make Download Link");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("aga_update_link")) {
                    snapshot.getRef().child("aga_update_link").setValue(s);
                }else{
                    rootRef.child("aga_update_link").push().setValue(s);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setloc(this, s);
    }

    private String uploadFile(File file){
        Log.d("Uploading", "Upload File");
        final String[] link = {""};
        link[0] = "";
        prog();
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(stream != null){
            Log.d("Uploading", "Stream != null");
            UploadTask uploadTask = storageRef.putStream(stream);
            uploadTask.addOnFailureListener(exception -> {
                if(progress.isShowing()) progress.dismiss();
                Toast.makeText(this, "Uploading failed", Toast.LENGTH_LONG).show();
            }).addOnSuccessListener(taskSnapshot -> {
                if(progress.isShowing()) progress.dismiss();
                Toast.makeText(MainActivity.this, "Uploading Succeed", Toast.LENGTH_LONG).show();
            }).addOnCompleteListener(task -> link[0] = task.getResult().toString());
        } else{
            Toast.makeText(this, "Getting null file", Toast.LENGTH_LONG).show();
        }
        return link[0];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK:
            if (resultCode == RESULT_OK) {
                String filepath;
                if (data != null) {
                    Log.d("Install Apk", "Data != null");
                    ClipData cd = data.getClipData();
                    if (cd != null) {
                        Log.d("Install Apk", "ClipData != null");
                        ClipData.Item item = cd.getItemAt(0);
                        Log.d("Install Apk", "Clip Data : " + cd);
                        filepath = FileUtil.convertUriToFilePath(this, item.getUri());
                        Log.d("Install Apk", "Path : " + filepath);
                    } else {
                        Log.d("Install Apk", "ClipData == null");
                        filepath = FileUtil.convertUriToFilePath(this, data.getData());
                    }
                } else {
                    Log.d("Install Apk", "Data == null");
                    Toast.makeText(this, "data = null", Toast.LENGTH_LONG).show();
                    filepath = "";
                }
                Log.d("Install Apk", "filepath == " + filepath);
                if (filepath != null) uploadClick(new File(filepath));
                else Toast.makeText(this, "Error filepath = null", Toast.LENGTH_LONG).show();
            }
            break;
            case PER:
                make_down_link(uploadFile(mFile));
                break;
        }
    }

    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> arrayList) {
            this._data = arrayList;
        }

        public int getCount() {
            return this._data.size();
        }

        public HashMap<String, Object> getItem(int i) {
            return this._data.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
            if (view == null) {
                view = layoutInflater.inflate(R.layout.cus, (ViewGroup) null);
            }
            ((TextView) view.findViewById(R.id.textview1)).setText(Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("name")).toString());
            ((TextView) view.findViewById(R.id.textview2)).setText(Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("usri")).toString());
            ((TextView) view.findViewById(R.id.textview3)).setText(Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("lic")).toString());
            ((TextView) view.findViewById(R.id.textview4)).setText(Objects.requireNonNull(((HashMap<?, ?>) MainActivity.this.acc.get(i)).get("device")).toString());
            return view;
        }
    }
}
