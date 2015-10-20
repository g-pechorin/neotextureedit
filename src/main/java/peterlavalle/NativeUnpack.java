package peterlavalle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class NativeUnpack {
	public static final File folder;
	public static final String name = System.getProperty("os.name").replaceAll("^(\\w+)[^\\w].*$", "$1").toLowerCase();
	public static final String arch = System.getProperty("os.arch").toLowerCase();

	static {
		File
				unpackDir = null;
		try {
			unpackDir = File.createTempFile("unpacked", "shared-objects");

			if (!unpackDir.delete()) {
				throw new IOException();
			}

			if (!unpackDir.mkdirs()) {
				throw new IOException();
			}

			unpackDir.deleteOnExit();

			final String config = name + "/" + arch;

			String[] sharedObjects;
			if (config.equals("windows/amd64")) {
				sharedObjects = new String[]{
						"lwjgl64.dll",
						"OpenAL64.dll"
				};
			} else {
				throw new IOException("Unknown system configuration `" + config + "`");
			}

			for (final String sharedObjectName : sharedObjects) {
				final File sharedObjectFile = new File(unpackDir, sharedObjectName);

				FileOutputStream outputStream = new FileOutputStream(sharedObjectFile);
				InputStream inputStream = ClassLoader.getSystemResourceAsStream(sharedObjectName);

				final byte[] data = new byte[256];

				for (int read; -1 != (read = inputStream.read(data)); ) {
					outputStream.write(data, 0, read);
				}
				outputStream.close();
				inputStream.close();
				sharedObjectFile.deleteOnExit();
			}

			//set sys_paths to null so that java.library.path will be reevalueted next time it is needed
			try {
				System.setProperty("java.library.path", unpackDir.getAbsolutePath() + File.pathSeparator + System.getProperty("java.library.path"));
				final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
				sysPathsField.setAccessible(true);
				sysPathsField.set(null, null);
			} catch (NoSuchFieldException e) {
				unpackDir = null;
				throw new RuntimeException("Something is missing", e);
			} catch (IllegalAccessException e) {
				unpackDir = null;
				throw new RuntimeException("I needed to modify the path for this to work", e);
			}
		} catch (IOException e) {
			unpackDir = null;
			throw new RuntimeException(e);
		} finally {
			folder = unpackDir;
		}
	}

	private NativeUnpack() {
	}
}
