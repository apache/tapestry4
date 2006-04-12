package net.sf.tapestry.multipart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
  *  An OutputStream that, when a certain limit is reached,
  *  opens a temporary file and writes to that temporary file.
  * 
  *  @author Howard Lewis Ship
  *  @version $Id$
  *  @since 2.0.1
  *
  **/

class UploadOutputStream extends OutputStream
{
    private static final Log LOG = LogFactory.getLog(UploadOutputStream.class);

    public static final int DEFAULT_LIMIT = 2000;

    private ByteArrayOutputStream _byteArrayStream = new ByteArrayOutputStream();
    private OutputStream _activeStream = _byteArrayStream;
    private int _limit;
    private int _count = 0;
    private FileOutputStream _fileStream;
    private File _file;

    UploadOutputStream()
    {
        this(DEFAULT_LIMIT);
    }

    UploadOutputStream(int limit)
    {
        _limit = limit;
    }

    public void close() throws IOException
    {
        _activeStream.close();
    }

    public void flush() throws IOException
    {
        _activeStream.close();
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        check(len);

        _activeStream.write(b, off, len);
    }

    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }

    public void write(int b) throws IOException
    {
        check(1);

        _activeStream.write(b);
    }

    private void check(int length) throws IOException
    {
        if (_fileStream != null)
            return;

        _count += length;

        if (_count < _limit)
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Limit of " + _limit + " bytes reached.");

        _file = File.createTempFile("upload-", ".bin");

        _fileStream = new FileOutputStream(_file);

        _byteArrayStream.close();

        _byteArrayStream.writeTo(_fileStream);

        if (LOG.isDebugEnabled())
            LOG.debug("Writing upload content to " + _file + ".");

        _byteArrayStream = null;
        _activeStream = _fileStream;
    }

    public File getContentFile()
    {
        return _file;
    }

    public byte[] getContent()
    {
        return _byteArrayStream.toByteArray();
    }
}