package com.obsez.android.lib.smbfilechooser.permissions;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;

import java.util.Random;

public final class PermissionsUtil {
    @SuppressWarnings("unused")
    public static final String TAG = PermissionsUtil.class.getName();

    public interface OnPermissionListener {
        void onPermissionGranted(final String[] permissions);

        void onPermissionDenied(final String[] permissions);

        void onShouldShowRequestPermissionRationale(final String[] permissions);
    }

    public static void checkPermissions(@NonNull Context context, @Nullable final OnPermissionListener onPermissionListener, final String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissions.length == 0) {
            if (onPermissionListener != null) onPermissionListener.onPermissionGranted(permissions);
            return;
        }

        int requestCode = _random.nextInt(1024);
        _permissionListeners.put(requestCode, onPermissionListener);

        context.startActivity(new Intent(context, PermissionActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            .putExtra(PermissionActivity.INTENT_EXTRA_PERMISSIONS, permissions)
            .putExtra(PermissionActivity.INTENT_EXTRA_REQUEST_CODE, requestCode));
    }

    private static final SparseArrayCompat<OnPermissionListener> _permissionListeners = new SparseArrayCompat<>();
    private static final Random _random = new Random();

    static OnPermissionListener getPermissionListener(final int requestCode) {
        OnPermissionListener listener = _permissionListeners.get(requestCode, null);
        _permissionListeners.remove(requestCode);
        return listener;
    }
}
