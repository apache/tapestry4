package tutorial.workbench.table;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.components.Block;
import net.sf.tapestry.contrib.table.components.ComponentAddress;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.contrib.table.model.ognl.ExpressionTableColumn;
import net.sf.tapestry.contrib.table.model.simple.SimpleSetTableDataModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumnModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableModel;

/**
 * @author mindbridge
 *
 */
public class LocaleSelection
	extends BaseComponent
	implements ILocaleSelectionListener
{
	// immutable
	private ITableColumnModel m_objTableColumnModel;
	private VerbosityRating m_objVerbosityRating;

	// temporary
	private Locale m_objCurrentLocale;

	public LocaleSelection()
	{
		m_objVerbosityRating = new VerbosityRating();
	}

	/**
	 * @see net.sf.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad()
	{
		super.finishLoad();

		// We delay the initialization until now, since some columns
		// rely on a completed page structure
		m_objTableColumnModel = createColumnModel();
	}

	private ITableColumnModel createColumnModel()
	{
		// Using a ComponentAddress is always necessary when working 
		// with IRender (see below)
		final ComponentAddress objAddress = new ComponentAddress(this);

		return new SimpleTableColumnModel(
			new ITableColumn[] {
				new ExpressionTableColumn("Locale", "toString()", true),
				new CurrencyColumn(),
				new DateFormatColumn(new Date()),
				new VerbosityColumn(m_objVerbosityRating, objAddress),
				new DeleteColumn(objAddress)});
	}

	public ITableModel getInitialTableModel()
	{
		SimpleSetTableDataModel objLocaleDataModel =
			new SimpleSetTableDataModel(new HashSet());
		return new SimpleTableModel(objLocaleDataModel, m_objTableColumnModel);
	}

	public SimpleSetTableDataModel getDataModel()
	{
		ITableModel objTableModel =
			((ITableModelSource) getComponent("table")).getTableModel();
		SimpleTableModel objSimpleTableModel = (SimpleTableModel) objTableModel;
		return (SimpleSetTableDataModel) objSimpleTableModel.getDataModel();
	}

	/**
	 * Returns the currentLocale.
	 * @return Locale
	 */
	public Locale getCurrentLocale()
	{
		return m_objCurrentLocale;
	}

	/**
	 * Sets the currentLocale.
	 * @param currentLocale The currentLocale to set
	 */
	public void setCurrentLocale(Locale currentLocale)
	{
		m_objCurrentLocale = currentLocale;
	}

	/**
	 * Returns the currentLocale.
	 * @return Locale
	 */
	public int getCurrentLocaleVerbosity()
	{
		return m_objVerbosityRating.calculateVerbosity(getCurrentLocale());
	}

	/**
	 * @see tutorial.workbench.table.ILocaleSelectionListener#localesSelected(Locale[])
	 */
	public void localesSelected(Locale[] arrLocales)
	{
		SimpleSetTableDataModel objDataModel = getDataModel();
		objDataModel.addRows(Arrays.asList(arrLocales));
	}

	public void deleteLocale(IRequestCycle objCycle)
	{
		Object[] arrParams = objCycle.getServiceParameters();
		Locale objLocale = (Locale) arrParams[0];
		getDataModel().removeRow(objLocale);
	}


    // Column definitions follow
	private static class DateFormatColumn extends SimpleTableColumn
	{
		private Date m_objDate;

		public DateFormatColumn(Date objDate)
		{
			super("Date Format", true);
			m_objDate = objDate;
		}

		public Object getColumnValue(Object objRow)
		{
			Locale objLocale = (Locale) objRow;
			DateFormat objFormat =
				DateFormat.getDateTimeInstance(
					DateFormat.LONG,
					DateFormat.LONG,
					objLocale);
			return objFormat.format(m_objDate);
		}
	}

	private static class CurrencyColumn extends SimpleTableColumn
	{
		public CurrencyColumn()
		{
			super("Currency", true);
		}

		public Object getColumnValue(Object objRow)
		{
			Locale objLocale = (Locale) objRow;
			String strCountry = objLocale.getCountry();
			if (strCountry == null || strCountry.equals(""))
				return "";

			DecimalFormatSymbols objSymbols =
				new DecimalFormatSymbols(objLocale);
            return objSymbols.getCurrencySymbol();
		}
	}

	private static class VerbosityColumn extends SimpleTableColumn
	{
		private VerbosityRating m_objVerbosityRating;
		private ComponentAddress m_objComponentAddress;

		public VerbosityColumn(
			VerbosityRating objVerbosityRating,
			ComponentAddress objComponentAddress)
		{
			super("Language Verbosity", true);
			m_objVerbosityRating = objVerbosityRating;
			m_objComponentAddress = objComponentAddress;
		}

		public Object getColumnValue(Object objRow)
		{
			Locale objLocale = (Locale) objRow;
			return new Integer(
				m_objVerbosityRating.calculateVerbosity(objLocale));
		}

		public IRender getValueRenderer(
			IRequestCycle objCycle,
			ITableModelSource objSource,
			Object objRow)
		{
			LocaleSelection objSelection =
				(LocaleSelection) m_objComponentAddress.findComponent(objCycle);
			objSelection.setCurrentLocale((Locale) objRow);
			Block objBlock =
				(Block) objSelection.getComponent("blockVerbosity");
			return new BlockRenderer(objBlock);
		}
	}

	private static class DeleteColumn extends SimpleTableColumn
	{
		private ComponentAddress m_objComponentAddress;

		public DeleteColumn(ComponentAddress objComponentAddress)
		{
			super("");
			m_objComponentAddress = objComponentAddress;
		}

		public IRender getValueRenderer(
			IRequestCycle objCycle,
			ITableModelSource objSource,
			Object objRow)
		{
			LocaleSelection objSelection =
				(LocaleSelection) m_objComponentAddress.findComponent(objCycle);
			objSelection.setCurrentLocale((Locale) objRow);
			Block objBlock = (Block) objSelection.getComponent("blockDelete");
			return new BlockRenderer(objBlock);
		}
	}

}
