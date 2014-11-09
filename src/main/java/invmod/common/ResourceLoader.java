//package invmod.common;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.channels.Channels;
//import java.nio.channels.FileChannel;
//import java.nio.channels.ReadableByteChannel;
//
//public class ResourceLoader {
//	public boolean copyResource(ClassLoader classLoader, String resourcePath, File destFile) {
//		InputStream stream = null;
//		FileChannel destChannel = null;
//		ReadableByteChannel sourceChannel = null;
//		try {
//			boolean bool1;
//			if (!destFile.exists()) {
//				mod_Invasion.log("Starting copying of sound file");
//				if (!destFile.createNewFile()) {
//					mod_Invasion.log("Unable to create new sound file.");
//					return false;
//				}
//			}
//
//			destChannel = new FileOutputStream(destFile).getChannel();
//			stream = classLoader.getResourceAsStream(resourcePath);
//			if (stream == null) {
//				mod_Invasion.log("Failed to find resource: " + resourcePath);
//				return false;
//			}
//			sourceChannel = Channels.newChannel(stream);
//			long written = 0L;
//			int position = 0;
//			do {
//				written = destChannel.transferFrom(sourceChannel, position, 1024L);
//				position = (int) (position + written);
//			} while (written > 0L);
//			return true;
//		} catch (IOException e) {
//			mod_Invasion.log("Problem creating file channels");
//			mod_Invasion.log(e.getMessage());
//			return false;
//		} finally {
//			try {
//				if (sourceChannel != null)
//					sourceChannel.close();
//			} catch (IOException e) {
//				mod_Invasion.log("Problem closing source file channel");
//				mod_Invasion.log(e.getMessage());
//			}
//
//			try {
//				if (destChannel != null)
//					destChannel.close();
//			} catch (IOException e) {
//				mod_Invasion.log("Problem closing destination file channel");
//				mod_Invasion.log(e.getMessage());
//			}
//
//			try {
//				if (stream != null)
//					stream.close();
//			} catch (IOException e) {
//				mod_Invasion.log("Problem closing input stream");
//				mod_Invasion.log(e.getMessage());
//			}
//		}
//	}
//}