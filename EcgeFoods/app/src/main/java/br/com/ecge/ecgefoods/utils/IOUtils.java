package br.com.ecge.ecgefoods.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class IOUtils {
    private static final String TAG = "IOUtils";

    /**
     * Converte a InputStream para String utilizando o charset informado
     *
     * @param in
     * @param charset UTF-8 ou ISO-8859-1
     * @return
     * @throws IOException
     */
    public static String toString(InputStream in, String charset) throws IOException {
        byte[] bytes = toBytes(in);
        String texto = new String(bytes, charset);
        return texto;
    }

    /**
     * Converte a InputStream para bytes[]
     *
     * @param in
     * @return
     */
    public static byte[] toBytes(InputStream in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } finally {
            try {
                bos.close();
                in.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public static String convertToBase64(String entrada1, String entrada2) throws UnsupportedEncodingException {
        String novoValor = new String(entrada1 + ":" + entrada2);
        byte[] data = novoValor.getBytes("UTF-8");
        String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        return base64;
    }

    public static void writeString(OutputStream out, String string) {
        writeBytes(out, string.getBytes());
    }

    public static void writeBytes(OutputStream out, byte[] bytes) {
        try {
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Salva o texto em arquivo
     *
     * @param file
     * @param string
     */
    public static void writeString(File file, String string) {
        writeBytes(file, string.getBytes());
    }

    public static void writeBytes(File file, byte[] bytes) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static String readString(File file) {
        try {
            if (file == null || !file.exists()) {
                return null;
            }
            InputStream in = new FileInputStream(file);
            String s = toString(in, "UTF-8");
            return s;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Salva a figura em aruqivo
     *
     * @param file
     * @param bitmap
     */
    public static void writeBitmap(File file, Bitmap bitmap) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public interface Callback {
        public void onFileSaved(File file, boolean exists);
    }

    /**
     * Salva o bitmap em arquivo. Utiliza a URL para descobrir o nome.
     *
     * @param url      URL original para extrair o nome do arquivo
     * @param bitmap   Bitmap que já está em memória
     * @param callback Interface de retorno
     */
    public static void saveBitmapToFile(String url, Bitmap bitmap, Callback callback) {
        try {
            if (url == null || bitmap == null && callback != null) {
                return;
            }

            String fileName = url.substring(url.lastIndexOf("/"));

            File file = getPublicFile(fileName, Environment.DIRECTORY_PICTURES);
            if (file.exists()) {
                callback.onFileSaved(file, true);
            } else {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();

                Log.d(TAG, "Save File: " + file);

                // Salva o arquivo
                IOUtils.writeBitmap(file, bitmap);

                callback.onFileSaved(file, false);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static boolean downloadToFile(String url, File file) {
        try {
            InputStream in = new URL(url).openStream();
            byte[] bytes = IOUtils.toBytes(in);
            IOUtils.writeBytes(file, bytes);
            Log.d(TAG, "downloadToFile: " + file);
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static void checkMainThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("Qualquer operação de I/O não pode ser executado na UI Thread.");
        }
    }

    /**
     * Cria um arquivo público na raiz do sdcard
     *
     * @param type DIRECTORY_MUSIC, DIRECTORY_PODCASTS, DIRECTORY_RINGTONES, DIRECTORY_ALARMS, DIRECTORY_NOTIFICATIONS, DIRECTORY_PICTURES, DIRECTORY_MOVIES, DIRECTORY_DOWNLOADS, ou DIRECTORY_DCIM
     * @return
     */
    public static File getPublicFile(String fileName, String type) {
        File sdCardDir = Environment.getExternalStoragePublicDirectory(type);
        return createFile(sdCardDir, fileName);
    }

    /**
     * Cria o arquivo no SDCard na pasta informada.
     *
     * @param sdCardDir Pasta do sdcard
     * @param fileName  Nome do arquivo
     * @return
     */
    private static File createFile(File sdCardDir, String fileName) {
        if (!sdCardDir.exists()) {
            sdCardDir.mkdir(); // Cria o diretório se não existe
        }
        // Retorna o arquivo para ler ou salvar no sd card
        File file = new File(sdCardDir, fileName);
        return file;
    }

    public static void ocultarTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void exibirTeclado(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
}
