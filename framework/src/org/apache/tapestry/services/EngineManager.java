package org.apache.tapestry.services;

import org.apache.tapestry.IEngine;

/**
 * Service responsible for obtaining instances of of {@link org.apache.tapestry.IEngine}
 * to service the current request.  An engine service may be retrieved from a pool, or extracted
 * from the HttpSession. After the request is processed, the engine is re-stored into the
 * HttpSession (if stateful) or back into the pool (if not stateful).
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface EngineManager
{
	/**
	 * Locates or creates an engine instance for the current request.
	 */
	IEngine getEngineInstance();
	
	/**
	 * Store the engine back at the end of the current request.
	 */
	
	void storeEngineInstance(IEngine engine);
}
