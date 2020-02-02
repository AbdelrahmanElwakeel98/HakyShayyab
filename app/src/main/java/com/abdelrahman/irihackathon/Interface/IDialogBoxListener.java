package com.abdelrahman.irihackathon.Interface;

import android.content.DialogInterface;

public interface IDialogBoxListener {
    void onClickPositiveButton(DialogInterface dialogInterface, String otp);
    void onClickNegativeButton(DialogInterface dialogInterface);
}
