package net.sf.tapestry;

/**
 *  An element that may be asked to render itself to an
 *  {@link IMarkupWriter} using a {@link IRequestCycle}.
 *
 *  <p>This primarily includes {@link IComponent} and {@link IPage},
 *  but also extends to other things, such as objects responsible for
 *  rendering static markup text.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IRender
{
	/**
	 *  The principal rendering/rewinding method.  This will cause
	 *  the receiving component to render its top level elements (HTML
	 *  text and components).
	 *
	 *  <p>Renderring and rewinding are the exact same process.  The
	 *  same code that renders must be able to restore state by going
	 *  through the exact same operations (even though the output is
	 *  discarded).
	 *
	 **/

	public void render(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException;
}