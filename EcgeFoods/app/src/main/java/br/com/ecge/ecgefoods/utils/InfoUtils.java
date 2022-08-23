package br.com.ecge.ecgefoods.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class InfoUtils {

    private static Snackbar snackbar;

    public static Snackbar snack(View view, String msg) {
        snack(view, msg, null);
        return snackbar;
    }

    private static void snack(View view, String msg, final Runnable runnable) {
        snackbar = Snackbar.make(view, msg, 0).setAction("Ok", v -> {
            if (runnable != null) {
                runnable.run();
            }

        });
        snackbar.show();
    }

    public static void toast(Context contexto, String msg) {
        Toast.makeText(contexto, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context contexto, String msg) {
        Toast.makeText(contexto, msg, Toast.LENGTH_LONG).show();
    }
}
