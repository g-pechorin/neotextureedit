package peterlavalle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NativeUnpack {
	public static final File folder;
	public static final String name = System.getProperty("os.name").replaceAll("^(\\w+)[^\\w].*$", "$1").toLowerCase();
	public static final String arch = System.getProperty("os.arch").toLowerCase();

	static {
		File unpackDir = null;
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

			final byte[] data = new byte[128];
			for (final String sharedObjectName : sharedObjects) {
				final File sharedObjectFile = new File(unpackDir, sharedObjectName);

				FileOutputStream outputStream = new FileOutputStream(sharedObjectFile);
				InputStream inputStream = ClassLoader.getSystemResourceAsStream(sharedObjectName);

				for (int read; -1 != (read = inputStream.read(data)); ) {
					outputStream.write(data, 0, read);
				}

				outputStream.close();
				inputStream.close();
				sharedObjectFile.deleteOnExit();
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
