package com.mhframework.resources;

public class MHResourceHeader
{
    private Object resource;
    private long lastAccessTime;
    private String id;
    
    public MHResourceHeader(String id, Object resource)
    {
        this.id = id;
        this.resource = resource;
        lastAccessTime = System.currentTimeMillis();
    }
    
    
    public Object getResource()
    {
        lastAccessTime = System.currentTimeMillis();
        return resource;
    }

    public long getLastAccessTime()
    {
        return lastAccessTime;
    }

    public String getId()
    {
        return id;
    }

}
