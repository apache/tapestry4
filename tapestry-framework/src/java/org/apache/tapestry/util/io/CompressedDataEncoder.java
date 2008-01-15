package org.apache.tapestry.util.io;

import org.apache.commons.codec.binary.Base64;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.util.Defense;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Utility class used by {@link org.apache.tapestry.IRequestCycle} to compress {@link org.apache.tapestry.util.IdAllocator}
 * state.
 */
public class CompressedDataEncoder {

    /**
     * Prefix on the MIME encoding that indicates that the encoded data is not encoded.
     */

    public static final String BYTESTREAM_PREFIX = "B";

    /**
     * Prefix on the MIME encoding that indicates that the encoded data is encoded with GZIP.
     */

    public static final String GZIP_BYTESTREAM_PREFIX = "Z";

    private CompressedDataEncoder() {}

    /**
     * Encodes the given string into a compressed string representation that can later be decoded.
     *
     * @param input
     *          String input to compress and encode into a persistable form.
     *
     * @return encoded string (possibly empty, but not null)
     */
    public static String encodeString(String input)
    {
        Defense.notNull(input, "input");

        if (input.isEmpty())
            return "";

        try {
            ByteArrayOutputStream bosPlain = new ByteArrayOutputStream();
            ByteArrayOutputStream bosCompressed = new ByteArrayOutputStream();

            GZIPOutputStream gos = new GZIPOutputStream(bosCompressed);
            TeeOutputStream tos = new TeeOutputStream(bosPlain, gos);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(tos));
            
            oos.writeUTF(input);
            
            oos.close();

            boolean useCompressed = bosCompressed.size() < bosPlain.size();

            byte[] data = useCompressed ? bosCompressed.toByteArray() : bosPlain.toByteArray();
            
            byte[] encoded = Base64.encodeBase64(data);

            String prefix = useCompressed ? GZIP_BYTESTREAM_PREFIX : BYTESTREAM_PREFIX;

            return prefix + new String(encoded);
        }
        catch (Exception ex) {
            throw new ApplicationRuntimeException(IoMessages.encodeFailure(input, ex), ex);
        }
    }

    /**
     * Takes a string with an encoded and compressed input as produced by {@link #encodeString(String)} , and converts it back
     * into the original String representation.
     *
     * @param input
     *          The data to un-encode, which should be equivalent to the same that
     *          was passed in to {@link #encodeString(String)}.
     *
     * @return The decoded string data.
     */
    public static String decodeString(String input)
    {
        if (HiveMind.isBlank(input))
            return "";

        String prefix = input.substring(0, 1);

        if (!(prefix.equals(BYTESTREAM_PREFIX) || prefix.equals(GZIP_BYTESTREAM_PREFIX)))
            throw new ApplicationRuntimeException(IoMessages.unknownPrefix(prefix));

        try {
            // Strip off the prefix, feed that in as a MIME stream.

            byte[] decoded = Base64.decodeBase64(input.substring(1).getBytes());

            InputStream is = new ByteArrayInputStream(decoded);

            if (prefix.equals(GZIP_BYTESTREAM_PREFIX))
                is = new GZIPInputStream(is);

            // I believe this is more efficient; the buffered input stream should ask the
            // GZIP stream for large blocks of un-gzipped bytes, with should be more efficient.
            // The object input stream will probably be looking for just a few bytes at
            // a time. We use a resolving object input stream that knows how to find
            // classes not normally acessible.

            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            
            String result = ois.readUTF();

            is.close();

            return result;
        }
        catch (Exception ex) {
            throw new ApplicationRuntimeException(IoMessages.decodeFailure(ex), ex);
        }
    }
}
