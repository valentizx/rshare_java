package me.rex.sdk.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


final public class RFileHelper {
    private static final String TAG = "RFileHelper=====>";


    /**
     * 存储文件到外部 cache + share_tmp 文件夹下.
     * @param context
     * @param bitmap
     */
    public static void saveBitmapToExternalSharePath(Context context, Bitmap bitmap) {


        String dir = context.getExternalCacheDir()
                .getAbsolutePath() + "/share_tmp/";
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
        Log.e(TAG, file.getAbsolutePath());

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param context
     * @return 返回外部 cache + share_tmp 文件夹下所有文件的 Uri,
     * 对应存储接口{@link #saveBitmapToExternalSharePath(Context, Bitmap)}.
     */
    public static ArrayList<Uri> getExternalSharePathFileUris(Context context) {

        String dir = context.getExternalCacheDir()
                .getAbsolutePath() + "/share_tmp/";
        File dirFile = new File(dir);
        if (!dirFile.isDirectory() || !dirFile.exists()) {
            return null;
        }

        ArrayList<Uri> uris = new ArrayList<Uri>();

        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i ++) {
            if (files[i].isFile()) {
                Log.v(TAG, files[i].getAbsolutePath());
                uris.add(Uri.fromFile(files[i]));
            }
        }
        return uris;

    }

    /**
     * @param context
     * @return 返回外部 cache + share_tmp 文件夹下所有 File,
     * 对应存储接口{@link #saveBitmapToExternalSharePath(Context, Bitmap)}.
     */
    public static ArrayList<File> getExternalSharePathFiles(Context context) {
        String dir = context.getExternalCacheDir()
                .getAbsolutePath() + "/share_tmp/";
        File dirFile = new File(dir);
        if (!dirFile.isDirectory() || !dirFile.exists()) {
            return null;
        }

        ArrayList<File> files = new ArrayList<File>();

        File[] fs = dirFile.listFiles();
        for (int i = 0; i < fs.length; i ++) {
            if (fs[i].isFile()) {
                Log.v(TAG, fs[i].getAbsolutePath());
                files.add(fs[i]);
            }
        }
        return files;
    }


    /**
     * @param context
     * @return 返回外部 cache + share_tmp 文件夹下所有文件的 Bitmap,
     * 对应存储接口{@link #saveBitmapToExternalSharePath(Context, Bitmap)}.
     */
    public static ArrayList<Bitmap> getExternalSharePathBitmaps(Context context) {
        String dir = context.getExternalCacheDir()
                .getAbsolutePath() + "/share_tmp/";
        File dirFile = new File(dir);
        if (!dirFile.isDirectory() || !dirFile.exists()) {
            return null;
        }

        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

        File[] fs = dirFile.listFiles();
        for (int i = 0; i < fs.length; i ++) {
            if (fs[i].isFile()) {
                Log.e(TAG, fs[i].getAbsolutePath());
                Bitmap bitmap=BitmapFactory.decodeFile(fs[i].getAbsolutePath());

                bitmaps.add(bitmap);
            }
        }
        return bitmaps;
    }


    /**
     * @param context
     * @return 返回外部 cache + share_tmp 文件夹下所有文件的 Path,
     * 对应存储接口{@link #saveBitmapToExternalSharePath(Context, Bitmap)}.
     */
    public static ArrayList<String> getExternalSharePathFilePaths(Context context) {
        ArrayList<String> paths = new ArrayList<String>();
        ArrayList<Uri> uris = getExternalSharePathFileUris(context);
        for (int i = 0; i < uris.size(); i ++) {
            paths.add(getPath(context, uris.get(i)));
        }
        return paths;
    }




    /**
     * 删除外部 cache + share_tmp 目录以及目录下的文件或者文件夹.
     * 对应存储接口 {@link #saveBitmapToExternalSharePath(Context, Bitmap)}
     */
    public static boolean deleteExternalShareDirectory(Context context) {
        String dir = context.getExternalCacheDir()
                .getAbsolutePath() + "/share_tmp/";
        File dirFile = new File(dir);
        if (!dirFile.isDirectory() || !dirFile.exists()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i ++) {
            if (files[i].isFile()) {
                flag = RFileHelper.deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else if (files[i].isDirectory()) {
                files[i].delete();
                if (!flag) break;
            }
        }
        if (!flag) return false;
        return dirFile.delete();
    }


    private static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            Log.v(TAG, "delete " + fileName + "failed! Bcuz it doesnt exist!");
            return false;

        }
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = getDocumentId(uri);

                if (!id.isEmpty()) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:","");
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                                Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }



            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static final String PATH_DOCUMENT = "document";


    private static boolean isDocumentUri(Context context, Uri uri) {
        final List<String> paths = uri.getPathSegments();
        if (paths.size() < 2) {
            return false;
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            return false;
        }

        return true;
    }

    private static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() < 2) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        return paths.get(1);
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     *            [url=home.php?mod=space&uid=7300]@return[/url] The value of
     *            the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static void detectFileUriExposure(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
