//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tapestry.Tapestry;

/**
 *  The most complicated of the adaptors, this one takes an arbitrary serializable
 *  object, serializes it to binary, and encodes it in a Base64 encoding.
 *
 *  <p>Encoding and decoding of Base64 strings uses code adapted from work in the public
 *  domain originally written by Jonathan Knudsen and published in
 *  O'reilly's "Java Cryptography". Note that we use a <em>modified</em> form of Base64 encoding,
 *  with URL-safe characters to encode the 62 and 63 values and the pad character.
 *
 *  <p>TBD:  Work out some class loader issues involved in deserializing.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

class SerializableAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "O";

    /**
     *  The PAD character, appended to the end of the string to make things
     *  line up.  In normal Base64, this is the character '='.
     *
     **/

    private static final char PAD = '.';

    /**
     *  Representation for the 6-bit code 63, normally '+' in Base64.
     *
     **/

    private static final char CH_62 = '-';

    /**
     *  Representation for the 6-bit code 64, normally '/' in Base64.
     *
     **/

    private static final char CH_63 = '_';

    public String squeeze(DataSqueezer squeezer, Object data) throws IOException
    {
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gos = null;
        ObjectOutputStream oos = null;
        byte[] byteData = null;

        try
        {
            bos = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(bos);
            oos = new ObjectOutputStream(gos);

            oos.writeObject(data);
            oos.close();
        }
        finally
        {
            close(oos);
            close(gos);
            close(bos);
        }

        byteData = bos.toByteArray();

        StringBuffer encoded = new StringBuffer(2 * byteData.length);
        char[] base64 = new char[4];

        encoded.append(PREFIX);

        for (int i = 0; i < byteData.length; i += 3)
        {
            encodeBlock(byteData, i, base64);
            encoded.append(base64);
        }

        return encoded.toString();
    }

    private void close(OutputStream stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            {
                // Ignore.
            }
        }
    }

    private void close(InputStream stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            {
                // Ignore.
            }
        }
    }
    public Object unsqueeze(DataSqueezer squeezer, String string) throws IOException
    {
        ByteArrayInputStream bis = null;
        GZIPInputStream gis = null;
        ObjectInputStream ois = null;
        byte[] byteData;

        // Strip off the first character and decode the rest.

        byteData = decode(string.substring(1));

        try
        {
            bis = new ByteArrayInputStream(byteData);
            gis = new GZIPInputStream(bis);
            ois = new ResolvingObjectInputStream(squeezer.getResolver(), gis);

            return ois.readObject();
        }
        catch (ClassNotFoundException ex)
        {
            // The message is the name of the class.

            throw new IOException(
                Tapestry.format("SerializableAdaptor.class-not-found", ex.getMessage()));
        }
        finally
        {
            close(ois);
            close(gis);
            close(bis);
        }
    }

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Serializable.class, this);
    }

    private static void encodeBlock(byte[] raw, int offset, char[] base64) throws IOException
    {
        int block = 0;
        int slack = raw.length - offset - 1;
        int end = (slack >= 2) ? 2 : slack;

        for (int i = 0; i <= end; i++)
        {
            byte b = raw[offset + i];
            int neuter = (b < 0) ? b + 256 : b;
            block += neuter << (8 * (2 - i));
        }

        for (int i = 0; i < 4; i++)
        {
            int sixbit = (block >>> (6 * (3 - i))) & 0x3f;
            base64[i] = getChar(sixbit);
        }

        if (slack < 1)
            base64[2] = PAD;

        if (slack < 2)
            base64[3] = PAD;
    }

    protected static char getChar(int sixBit) throws IOException
    {
        if (sixBit >= 0 && sixBit <= 25)
            return (char) ('A' + sixBit);

        if (sixBit >= 26 && sixBit <= 51)
            return (char) ('a' + (sixBit - 26));

        if (sixBit >= 52 && sixBit <= 61)
            return (char) ('0' + (sixBit - 52));

        if (sixBit == 62)
            return CH_62;

        if (sixBit == 63)
            return CH_63;

        throw new IOException(
            Tapestry.format("SerializableAdaptor.unable-to-convert", Integer.toString(sixBit)));
    }

    public static byte[] decode(String string) throws IOException
    {
        int pad = 0;
        char[] base64 = string.toCharArray();

        for (int i = base64.length - 1; base64[i] == PAD; i--)
            pad++;

        int length = base64.length * 6 / 8 - pad;
        byte[] raw = new byte[length];
        int rawIndex = 0;

        for (int i = 0; i < base64.length; i += 4)
        {
            int block =
                (getValue(base64[i]) << 18)
                    + (getValue(base64[i + 1]) << 12)
                    + (getValue(base64[i + 2]) << 6)
                    + (getValue(base64[i + 3]));

            for (int j = 0; j < 3 && rawIndex + j < raw.length; j++)
                raw[rawIndex + j] = (byte) ((block >> (8 * (2 - j))) & 0xff);

            rawIndex += 3;
        }

        return raw;
    }

    private static int getValue(char c) throws IOException
    {
        if (c >= 'A' && c <= 'Z')
            return c - 'A';

        if (c >= 'a' && c <= 'z')
            return c - 'a' + 26;

        if (c >= '0' && c <= '9')
            return c - '0' + 52;

        if (c == CH_62)
            return 62;

        if (c == CH_63)
            return 63;

        // Pad character

        if (c == PAD)
            return 0;

        throw new IOException(
            Tapestry.format(
                "SerializableAdaptor.unable-to-interpret-char",
                new String(new char[] { c })));
    }

}