package peterlavalle.neo

import java.io.{File, FileOutputStream}

object NativeUnpack {
	val name: String = System.getProperty("os.name").replaceAll("^(\\w+)[^\\w].*$", "$1").toLowerCase
	val arch: String = System.getProperty("os.arch").toLowerCase

	val unpackDir: File = {
		val unpackDir: File =
			File.createTempFile("unpacked", "shared-objects")

		require(unpackDir.delete() && unpackDir.mkdirs())
		unpackDir.deleteOnExit()

		// TODO; more-better delete

		((name, arch) match {
			case ("windows", "amd64") =>
				List("lwjgl64.dll", "OpenAL64.dll")
		})
			.foreach {
				sharedObjectName =>

					// create the file
					val sharedObjectFile = new File(unpackDir, sharedObjectName).EnsureParent

					// fill the file with data
					(new FileOutputStream(sharedObjectFile) << ClassLoader.getSystemResourceAsStream(sharedObjectName))
						.close()

					// delete the file when we exit
					sharedObjectFile.deleteOnExit()
			}

		// add our path to java.library.path
		System.setProperty("java.library.path", unpackDir.getAbsolutePath + File.pathSeparator + System.getProperty("java.library.path"))

		// also; add us to the LWJGL path
		System.setProperty("org.lwjgl.librarypath", unpackDir.getAbsolutePath)

		// set sys_paths to null so that java.library.path will be reevalueted next time it is needed
		// ... this is an ugly necessity; we need to do this to kick the JVM
		// ... since this code is only ever called once; we're safe though - right?
		try {
			val sysPathsField = classOf[ClassLoader].getDeclaredField("sys_paths")
			sysPathsField.setAccessible(true)
			sysPathsField.set(null, null)
			unpackDir
		} catch {
			case e: NoSuchFieldException =>
				throw new RuntimeException("Something is missing", e)
			case e: IllegalAccessException =>
				throw new RuntimeException("I needed to modify the path for this to work", e)
		}
	}
}