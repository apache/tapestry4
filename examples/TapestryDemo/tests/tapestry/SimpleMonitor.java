package tests.tapestry;

import com.ibm.logging.*;
import com.primix.tapestry.*;

public class SimpleMonitor implements IRecordType, IMonitor
{
    private MessageLogger logger;

    public SimpleMonitor(MessageLogger logger)
    {
	this.logger = logger;
    }

    public void pageCreateBegin(java.lang.String pageName)
    {
	logger.text(TYPE_INFO, this, "pageCreateBegin",
                    "BEGIN CREATE {0}", pageName);
    }

    public void pageCreateEnd(java.lang.String pageName)
    {
	logger.text(TYPE_INFO, this, "pageCreateEnd",
                    "END CREATE {0}", pageName);
    }

    public void pageLoadBegin(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageLoadBegin",
                    "BEGIN LOAD {0}", pageName);
    }

    public void pageLoadEnd(String pageName) 
    {
	logger.text(TYPE_INFO, this, "pageLoadEnd",
                    "END LOAD {0}", pageName);
    }

    public void pageRenderBegin(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRenderBegin",
                    "BEGIN RENDER {0}", pageName);
    }

    public void pageRenderEnd(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRenderEnd",
                    "END RENDER {0}", pageName);
    }

    public void pageRewindBegin(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRewindBegin",
                    "BEGIN REWIND {0}", pageName);
    }

    public void pageRewindEnd(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRewindEnd", "END REWIND {0}", pageName);
    }

    public void serviceBegin(String serviceName, String detailMessage)
    {
	logger.text(TYPE_INFO, this, "serviceBegin",
                    "BEGIN SERVICE {0} {1}", serviceName, detailMessage);
    }

    public void serviceEnd(String serviceName)
    {
	logger.text(TYPE_INFO, this, "serviceEnd", "END SERVICE {0}", serviceName);
    }

    public void serviceException(Throwable exception)
    {
	logger.exception(TYPE_INFO, this, "serviceException", exception);
    }

    public void sessionBegin()
    {
	logger.text(TYPE_INFO, this, "sessionBegin",
                    "Start of session.");
    }
}
