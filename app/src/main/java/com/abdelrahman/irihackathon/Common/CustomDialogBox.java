package com.abdelrahman.irihackathon.Common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.abdelrahman.irihackathon.Interface.IDialogBoxListener;
import com.abdelrahman.irihackathon.R;
import com.chaos.view.PinView;

public class CustomDialogBox {

    private PinView pinView;
    private Button verify;
    private Button resend;
    private Dialog dialog;

    public static CustomDialogBox mDialog;
    public IDialogBoxListener iDialogBoxListener;

    public static CustomDialogBox getInstance() {
        if(mDialog == null)
            mDialog = new CustomDialogBox();

        return mDialog;
    }

    public void showLoginDialog(Context context,
                                final IDialogBoxListener iDialogBoxListener)
    {
        this.iDialogBoxListener = iDialogBoxListener;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_verification_dialog);

        pinView = dialog.findViewById(R.id.pinView);
        verify = dialog.findViewById(R.id.btn_verify);
        resend = dialog.findViewById(R.id.btn_resend);

        dialog.setCancelable(false);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDialogBoxListener.onClickPositiveButton(dialog, pinView.getText().toString());
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void dismissDialog(){
        if (dialog != null)
        dialog.dismiss();
    }

}
