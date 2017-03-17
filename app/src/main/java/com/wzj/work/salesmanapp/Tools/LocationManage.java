package com.wzj.work.salesmanapp.Tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by 44967 on 2017/3/6.
 */
public class LocationManage implements EasyPermissions.PermissionCallbacks{
    private double latitude = 0.0;
    private double longitude = 0.0;
    private LocationManager locationManager;
    private String provider;
    Context context;

    public LocationManage(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //所要申请的权限
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(context, perms)) {//检查是否获取该权限
            //已获取权限
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(context, "必要的权限", 0, perms);
        }
        doLocation();
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    public void doLocation(){
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(context, "zzz", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            String currentLocation = "getLongitude:" + location.getLongitude() + ",\n"
                    + "getLatitude:" + location.getLatitude();
            Toast.makeText(context, currentLocation+"", Toast.LENGTH_SHORT).show();
        } else {
            String info = "123";
            Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        }
    }
}
