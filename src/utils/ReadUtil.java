package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadUtil {

  public static void read(byte[] bytes, InputStream stream) throws IOException {
    int offset = 0;
    int len = bytes.length;

    while (true) {
      int number = stream.read(bytes, offset, len - offset);
      if (number == -1 || len == offset) break;
      offset += number;
    }

    if (offset != len) {
      throw new RuntimeException("This can't happen");
    }
  }

  public static byte[] read(InputStream is) throws IOException {
    return read(is, 4096);
  }

  public static byte[] read(InputStream is, int size) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream(size);
    byte[] buff = new byte[4096];
    while (true) {
      int read = is.read(buff, 0, buff.length);
      if (read == -1) break;
      os.write(buff, 0, read);
    }
    return os.toByteArray();
  }
}
