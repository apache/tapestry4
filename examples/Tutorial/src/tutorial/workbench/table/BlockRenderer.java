package tutorial.workbench.table;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.components.Block;

/**
 * @author mindbridge
 *
 */
public class BlockRenderer implements IRender
{
	private Block m_objBlock;

	public BlockRenderer(Block objBlock)
	{
		m_objBlock = objBlock;
	}

	/**
	 * @see net.sf.tapestry.IRender#render(IMarkupWriter, IRequestCycle)
	 */
	public void render(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		m_objBlock.renderWrapped(writer, cycle);
	}

}
