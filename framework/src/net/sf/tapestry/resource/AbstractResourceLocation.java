package net.sf.tapestry.resource;

import net.sf.tapestry.IResourceLocation;

public abstract class AbstractResourceLocation implements IResourceLocation
{
    private String _path;
    private String _name;
    private String _folderPath;

    protected AbstractResourceLocation(String path)
    {
        _path = path;
    }

    public String getName()
    {
        if (_name == null)
            split();

        return _name;
    }

    public IResourceLocation getRelativeLocation(String name)
    {
        if (name.startsWith("/"))
        {
            if (name.equals(_path))
                return this;

            return buildNewResourceLocation(name);
        }

        if (_folderPath == null)
            split();

        if (name.equals(_name))
            return this;

        return buildNewResourceLocation(_folderPath + name);
    }

    protected String getPath()
    {
        return _path;
    }

    protected abstract IResourceLocation buildNewResourceLocation(String path);

    private void split()
    {
        int lastSlashx = _path.lastIndexOf('/');

        _folderPath = _path.substring(0, lastSlashx + 1);
        _name = _path.substring(lastSlashx + 1);
    }


    /**
     *  Returns true if the other object is an instance of the
     *  same class, and the paths are equal.
     * 
     **/

    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;

        if (obj.getClass().equals(getClass()))
        {
            AbstractResourceLocation otherLocation = (AbstractResourceLocation) obj;

            return _path.equals(otherLocation._path);
        }

        return false;
    }
}
