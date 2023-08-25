package com.quectel.permissionmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PermissionManager";
    private String package_name = "com.quectel.demand"; //This is a test package, you can replace your app package name to test
    private Button btn_check,btn_grant,btn_revoke;
    private EditText et_input;
    private TextView txt_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);
        btn_grant = findViewById(R.id.btn_grant);
        btn_grant.setOnClickListener(this);
        btn_revoke = findViewById(R.id.btn_revoke);
        btn_revoke.setOnClickListener(this);
        txt_list = findViewById(R.id.txt_list);
        et_input = findViewById(R.id.et_input);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_check:
                checkPermission();
                break;
            case R.id.btn_grant:
                grantPermission();
                break;
            case R.id.btn_revoke:
                revokePermission();
                break;
        }
    }
    void checkPermission(){
        package_name = et_input.getText().toString();
        if(package_name.isEmpty()){
            Toast.makeText(this,"Please input package name",Toast.LENGTH_SHORT).show();
            return;
        }
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(package_name,PackageManager.GET_PERMISSIONS);
            String permissions[] = packageInfo.requestedPermissions;
            StringBuilder sb = new StringBuilder();
            boolean hasCameraPermission = false;
            if(permissions!=null && permissions.length >0) {
                for (String permission : permissions) {
                    if(permission.equals(Manifest.permission.CAMERA)){
                        hasCameraPermission = true;
                        if(PackageManager.PERMISSION_GRANTED==pm.checkPermission(permission,package_name)){
                            Log.d(TAG,permission+" is granted");
                            sb.append(permission+" is granted.\n");
                        }else{
                            Log.d(TAG,permission+" is not granted");
                            sb.append(permission+" is not granted.\n");
                        }
                        break;
                    }
                }
            }
            if(false ==hasCameraPermission){
                sb.append("This app has no camera permission");
            }
            txt_list.setText(sb.toString());
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this,"No such package name",Toast.LENGTH_SHORT).show();
        }
    }
    void grantPermission(){
        package_name = et_input.getText().toString();
        if(package_name.isEmpty()){
            Toast.makeText(this,"Please input package name",Toast.LENGTH_SHORT).show();
            return;
        }
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(package_name,PackageManager.GET_PERMISSIONS);
            String permissions[] = packageInfo.requestedPermissions;
            if(permissions!=null && permissions.length >0) {
                for (String permission : permissions) {
                    if(permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, package_name)) {
                            Log.d(TAG, permission + " is granted");
                        } else {
                            Log.d(TAG, permission + " is not granted,try to grant");
                            //You can use this API to grant permission for an app.
                            //You can click the button again to check the permission.
                            pm.grantRuntimePermission(package_name, permission, UserHandle.OWNER);
                        }
                    }
                }

            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this,"No such package name",Toast.LENGTH_SHORT).show();
        }
    }
    void revokePermission(){
        package_name = et_input.getText().toString();
        if(package_name.isEmpty()){
            Toast.makeText(this,"Please input package name",Toast.LENGTH_SHORT).show();
            return;
        }
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(package_name,PackageManager.GET_PERMISSIONS);

            String permissions[] = packageInfo.requestedPermissions;
            if(permissions!=null && permissions.length >0) {
                for (String permission : permissions) {
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, package_name)) {
                            Log.d(TAG, permission + " is granted, try to revoke");
                            pm.revokeRuntimePermission(package_name, permission, UserHandle.OWNER);
                        } else {
                            Log.d(TAG, permission + " is not granted");
                        }
                    }
                }
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this,"No such package name",Toast.LENGTH_SHORT).show();
        }
    }
}