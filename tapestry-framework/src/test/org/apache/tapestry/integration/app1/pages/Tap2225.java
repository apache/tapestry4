package org.apache.tapestry.integration.app1.pages;

/**
 * Integration test for TAPESTRY-2225.
 */
public abstract class Tap2225 extends Home
{
    public abstract void setMessage(String msg);

    public abstract void setMessageAsync(String msg);

    public void onCancel()
    {
        setMessage("cancel");
    }

    public void onRefresh()
    {
        setMessage("refresh");
    }

    public void onCancelAsync()
    {
        setMessageAsync("asyncCANCEL");
    }

    public void onRefreshAsync()
    {
        setMessageAsync("asyncREFRESH");
    }
}
