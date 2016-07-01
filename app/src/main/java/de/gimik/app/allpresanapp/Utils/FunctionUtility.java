package de.gimik.app.allpresanapp.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SuppressLint("DefaultLocale")
public class FunctionUtility extends Activity {
		private static final int BUFFER_SIZE = 8192;

	public static void zipDir(String basePath, String zipFileName, String dir)
			throws Exception {
		File dirObj = new File(dir);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));

		addDir(basePath, dirObj, out);
		out.close();
	}

	static void addDir(String basePath, File dirObj, ZipOutputStream out)
			throws IOException {
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(basePath, files[i], out);
				continue;
			}
			FileInputStream in = new FileInputStream(files[i].getPath());
			String fileName = files[i].getAbsolutePath().replace(basePath, "");
			System.out.println(" Adding: " + fileName);
			out.putNextEntry(new ZipEntry(fileName));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}

	public static void zipDir(String zipFileName, String dir) throws Exception {
		File dirObj = new File(dir);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));

		addDir(dirObj, out);
		out.close();
	}

	static void addDir(File dirObj, ZipOutputStream out) throws IOException {
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(files[i], out);
				continue;
			}
			FileInputStream in = new FileInputStream(files[i].getPath());
			System.out.println(" Adding: " + files[i].getPath());
			out.putNextEntry(new ZipEntry(files[i].getName()));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}

	/**
	 * Converts a file to ByteArray
	 * 
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByteArray(File file) throws Exception {
		if (file != null) {
			InputStream is = new FileInputStream(file);
			long length = file.length();
			byte[] bytes = new byte[(int) length];
			int offset = 0, n = 0;
			while (offset < bytes.length
					&& (n = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += n;
			}
			is.close();
			return bytes;
		}
		return null;

	}

	/**
	 * converts a byteArray to base64
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String byteToBase64(byte[] byteArray) {
		return Base64.encodeToString(byteArray, Base64.NO_WRAP);
	}

	/**
	 * Loads an image and encodes it to base64 String to put it in XML to send
	 * to server
	 * 
	 * @param imagePath
	 * @return
	 */
	public static String encodeImageTobase64(String imagePath) {

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
		return imageEncoded;

	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}


	/**
	 * 
	 * Creates a directory to save pictures. fileFolder should be sth. like
	 * username
	 * 
	 * @param fileFolder
	 * @return
	 */
	public static final String createMyAccountDirectory(String fileFolder) {
		File appDirectory = Environment.getExternalStorageDirectory();
		String bundleDirectoryPath = appDirectory + "/" + fileFolder;

		File bundleDirectory = new File(bundleDirectoryPath);
		if (!bundleDirectory.exists()) {
			bundleDirectory.mkdir();
		}

		return bundleDirectoryPath;
	}


	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
				// System.out.println("Directory is deleted : " +
				// file.getAbsolutePath());

			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					// System.out.println("Directory is deleted : " +
					// file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			file.delete();
			// System.out.println("File is deleted : " +
			// file.getAbsolutePath());
		}
	}

	public static Bitmap getBitmap(String imagePath) {
		try {
			File file = new File(imagePath);
			if (!file.exists())
				return null;
			int size = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			while (true) {
				try {
					options.inSampleSize = size;
					Bitmap bitmap = BitmapFactory
							.decodeFile(imagePath, options);
					// bitmap.recycle();
					return bitmap;
				} catch (OutOfMemoryError e) {
					size = size * 2;
				}

			}
		} catch (Exception e) {
		}
		return null;
	}

	public static Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 800;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale >= REQUIRED_SIZE
					&& o.outHeight / scale >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		} catch (OutOfMemoryError e) {
		}
		return null;
	}

	public static Bitmap getBitmap(String imagePath, int size) {
		File file = new File(imagePath);
		if (!file.exists())
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		while (true) {
			try {
				options.inSampleSize = size;
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
				return bitmap;
			} catch (OutOfMemoryError e) {
				size = size * 2;
			}
		}
	}

	public static float rotationForImage(File photo) {
		try {
			ExifInterface exif = new ExifInterface(photo.getPath());
			int rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL));
			return rotation;
		} catch (IOException e) {
			Log.e("Exif", "Error checking exif", e);
		}
		return 0f;
	}

	public static float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;

	}

	public static Bitmap scaleImage(File photo, Bitmap scaled, int newWidth) {
		int width = scaled.getWidth();
		int height = scaled.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		int newHeight = (int) (height * scaleWidth);
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();

		float rotation = rotationForImage(photo);
		if (rotation != 0f) {
			matrix.preRotate(rotation);
		}
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(scaled, 0, 0, width, height, matrix, true);
	}

	public static void close(OutputStream out) {
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}




	public static class DirFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory())
				return true;

			return false;
		}

	}

	public static List<File> listFiles(File rootDir, FileFilter filter,
			boolean recursive) {
		List<File> result = new ArrayList<File>();
		if (!rootDir.exists() || !rootDir.isDirectory())
			return result;

		// Add all files that comply with the given filter
		File[] files = rootDir.listFiles(filter);
		for (File f : files) {
			if (!result.contains(f))
				result.add(f);
		}

		// Recurse through all available dirs if we are scanning recursively
		if (recursive) {
			File[] dirs = rootDir.listFiles(new DirFilter());
			for (File f : dirs) {
				if (f.canRead()) {
					result.addAll(listFiles(f, filter, recursive));
				}
			}
		}

		return result;
	}

	public static void removeSigantureImages(File f) {
		new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.getName().equalsIgnoreCase("signatur.jpg")
						|| f.getName().equalsIgnoreCase("signatur.jpeg"))
					try {
						delete(f);
					} catch (IOException e) {
						e.printStackTrace();
					}
				return f.getName().toLowerCase().endsWith(".jpeg")
						|| f.getName().toLowerCase().endsWith(".jpg");
			}
		};

	}

	public static int getPhotoCountWithoutSignature(File f) {

		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.getName().equalsIgnoreCase("signatur.jpg")
						|| f.getName().equalsIgnoreCase("signatur.jpeg"))
					return false;
				return f.getName().toLowerCase().endsWith(".jpeg")
						|| f.getName().toLowerCase().endsWith(".jpg");
			}
		};
		List<File> listFile = FunctionUtility.listFiles(f, filter, true);

		return listFile.size();
	}

	public static int getPhotoCount(File f) {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".jpeg")
						|| f.getName().toLowerCase().endsWith(".jpg");
			}
		};
		List<File> listFile = FunctionUtility.listFiles(f, filter, true);

		return listFile.size();
	}



	public static String getQuery(List<NameValuePair> nameValuePairs) {
		if (nameValuePairs == null || nameValuePairs.size() == 0) {
			return "";
		}

		String query = "";
		String name;
		String value;
		boolean firstNameValuePair = true;

		for (NameValuePair nameValuePair : nameValuePairs) {

			// get the parameter
			name = nameValuePair.getName();
			value = nameValuePair.getValue();

			// parameter name must not be empty
			if (name != null && name.trim().length() != 0) {

				// append parameter name with appropriate separator
				if (firstNameValuePair) {
					query += "?" + Uri.encode(name);
				} else {
					query += "&" + Uri.encode(name);
				}

				// append parameter value if not empty
				if (value != null) {
					query += "=" + Uri.encode(value);
				}

				firstNameValuePair = false;
			}
		}

		return query;
	}

	public static String getStringValue(JSONObject jsonObject, String name) {
		try {
			return jsonObject.getString(name);
		} catch (Exception e) {
		}
		return "";
	}

	public static void closeStream(Closeable stream) {
		try {
			if (stream != null)
				stream.close();
		} catch (Exception ex) {
		}
	}

	/**
	 * Copy from one stream to another. Throws IOException in the event of error
	 * (for example, SD card is full)
	 * 
	 * @param is
	 *            Input stream.
	 * @param os
	 *            Output stream.
	 */
	public static void copyStream(InputStream is, OutputStream os)
			throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		copyStream(is, os, buffer, BUFFER_SIZE);
	}

	/**
	 * Copy from one stream to another. Throws IOException in the event of error
	 * (for example, SD card is full)
	 * 
	 * @param is
	 *            Input stream.
	 * @param os
	 *            Output stream.
	 * @param buffer
	 *            Temporary buffer to use for copy.
	 * @param bufferSize
	 *            Size of temporary buffer, in bytes.
	 */
	public static void copyStream(InputStream is, OutputStream os,
			byte[] buffer, int bufferSize) throws IOException {
		try {
			for (;;) {
				int count = is.read(buffer, 0, bufferSize);
				if (count == -1) {
					break;
				}
				os.write(buffer, 0, count);
			}
		} catch (IOException e) {
			throw e;
		}
	}
}
