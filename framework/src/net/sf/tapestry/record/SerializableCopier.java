package net.sf.tapestry.record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.util.io.ResolvingObjectInputStream;

/**
 *  Copies a {@link java.io.Serializable} object by making a copy of it,
 *  using serialization.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class SerializableCopier implements IValueCopier
{
    private static final Log LOG = LogFactory.getLog(SerializableCopier.class);

    private IResourceResolver _resolver;

    public SerializableCopier(IResourceResolver resolver)
    {
        _resolver = resolver;
    }

    public Object makeCopyOfValue(Object value) throws PageRecorderSerializationException
    {
        try
        {
            byte[] bytes = serialize(value);

            return deserialize(bytes);
        }
        catch (IOException ex)
        {
            throw new PageRecorderSerializationException(ex);
        }
    }

    private byte[] serialize(Object object) throws IOException
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug("Serializing " + object);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(object);

        oos.close();

        byte[] result = bos.toByteArray();

        if (debug)
            LOG.debug("Serialized to " + result.length + " bytes");

        return result;
    }

    private Object deserialize(byte[] bytes) throws IOException
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug("Deserializing " + bytes.length + " bytes");

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ResolvingObjectInputStream(_resolver, bis);

        Object result;
        
        try
        {
            result = ois.readObject();
        }
        catch (ClassNotFoundException ex)
        {
            throw new IOException(ex.getMessage());
        }

        ois.close();

        if (debug)
            LOG.debug("Deserialized as " + result);

        return result;
    }
}
