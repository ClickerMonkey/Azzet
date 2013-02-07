/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.asset.csv;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetInfo;

/**
 * Information for loading a CSV file.
 * 
 * @author Philip Diffenderfer
 *
 */
public class CsvInfo extends BaseAssetInfo
{
	
	/**
	 * The default values for the parsing options.
	 */
	public static final String DEFAULT_DELIMITER = ",";
	public static final boolean DEFAULT_HEADER = true;
	public static final boolean DEFAULT_TRIM = true;
	public static final boolean DEFAULT_IGNORE = true;
	public static final boolean DEFAULT_STRICT = true;

	// properties
	public static final String[] PROPERTIES = {
		"delimiter", "headed", "trimming", "ignoreempty", "strict"
	};
	
	// parsing options
	private String delimiter;
	private boolean headed;
	private boolean trimming;
	private boolean ignoreEmpty;
	private boolean strict;
	
	
	/**
	 * Instantiates a new CsvInfo.
	 */
	public CsvInfo()
	{
		this(DEFAULT_DELIMITER, DEFAULT_HEADER, DEFAULT_TRIM, DEFAULT_IGNORE, DEFAULT_STRICT);
	}
	
	/**
	 * Instantiates a new CsvInfo.
	 * 
	 * @param delimiter
	 * 		The delimiter to use for separating cells in a row.
	 */
	public CsvInfo(String delimiter)
	{
		this(delimiter, DEFAULT_HEADER, DEFAULT_TRIM, DEFAULT_IGNORE, DEFAULT_STRICT);
	}
	
	/**
	 * Instantiates a new CsvInfo.
	 * 
	 * @param delimiter
	 * 		The delimiter to use for separating cells in a row.
	 * @param headed
	 * 		True if the CSV file has a header. A header is a row of cells that
	 * 		define what each column contains.
	 */
	public CsvInfo(String delimiter, boolean headed)
	{
		this(delimiter, headed, DEFAULT_TRIM, DEFAULT_IGNORE, DEFAULT_STRICT);
	}
	
	/**
	 * Instantiates a new CsvInfo.
	 * 
	 * @param delimiter
	 * 		The delimiter to use for separating cells in a row.
	 * @param headed
	 * 		True if the CSV file has a header. A header is a row of cells that
	 * 		define what each column contains.
	 * @param trimming
	 * 		True if the values in the cells should be trimmed. Trimming removes
	 * 		all whitespace in a value.
	 */
	public CsvInfo(String delimiter, boolean headed, boolean trimming)
	{
		this(delimiter, headed, trimming, DEFAULT_IGNORE, DEFAULT_STRICT);
	}
	
	/**
	 * Instantiates a new CsvInfo.
	 * 
	 * @param delimiter
	 * 		The delimiter to use for separating cells in a row.
	 * @param headed
	 * 		True if the CSV file has a header. A header is a row of cells that
	 * 		define what each column contains.
	 * @param trimming
	 * 		True if the values in the cells should be trimmed. Trimming removes
	 * 		all whitespace in a value.
	 * @param ignoreEmpty
	 * 		True if empty lines are to be ignored. If false empty lines will be
	 * 		added as empty rows to the resulting table.
	 */
	public CsvInfo(String delimiter, boolean headed, boolean trimming, boolean ignoreEmpty)
	{
		this(delimiter, headed, trimming, ignoreEmpty, DEFAULT_STRICT);
	}
	
	/**
	 * Instantiates a new CsvInfo.
	 *  
	 * @param delimiter
	 * 		The delimiter to use for separating cells in a row.
	 * @param headed
	 * 		True if the CSV file has a header. A header is a row of cells that
	 * 		define what each column contains.
	 * @param trimming
	 * 		True if the values in the cells should be trimmed. Trimming removes
	 * 		all whitespace in a value.
	 * @param ignoreEmpty
	 * 		True if empty lines are to be ignored. If false empty lines will be
	 * 		added as empty rows to the resulting table.
	 * @param strict
	 * 		True if parsing should validate the number of columns in a row. 
	 * 		When strict a valid CSV file has the same number of columns on 
	 * 		every non-empty row.
	 */
	public CsvInfo(String delimiter, boolean headed, boolean trimming, boolean ignoreEmpty, boolean strict)
	{
		super( Table.class );
		
		this.delimiter = delimiter;
		this.headed = headed;
		this.trimming = trimming;
		this.ignoreEmpty = ignoreEmpty;
		this.strict = strict;
	}

	@Override
	protected Object getProperty(String name)
	{
		if (name.equals("delimiter")) return delimiter;
		if (name.equals("headed")) return headed;
		if (name.equals("trimming")) return trimming;
		if (name.equals("ignoreempty")) return ignoreEmpty;
		if (name.equals("strict")) return strict;
		
		return null;
	}
	
	@Override
	public boolean isMatch( AssetInfo info )
	{
		if ( info instanceof CsvInfo )
		{
			CsvInfo csv = (CsvInfo)info;
			
			return (
				csv.headed == headed &&
				csv.ignoreEmpty == ignoreEmpty &&
				csv.strict == strict &&
				csv.trimming == trimming &&
				csv.delimiter.equals( delimiter )
			);
		}
		
		return false;
	}
	
	@Override
	public String[] getProperties()
	{
		return PROPERTIES;
	}
	
	/**
	 * Returns the delimiter option.
	 * 
	 * @return The delimiter to use for separating cells in a row.
	 */
	public String getDelimiter() 
	{
		return delimiter;
	}

	/**
	 * Sets the delimiter option.
	 * 
	 * @param delimiter
	 * 		The delimiter to use for separating cells in a row.
	 */
	public void setDelimiter(String delimiter) 
	{
		this.delimiter = delimiter;
	}

	/**
	 * Returns the headed option.
	 * 
	 * @return True if the CSV file has a header. A header is a row of cells that
	 * 		   define what each column contains.
	 */
	public boolean isHeaded() 
	{
		return headed;
	}

	/**
	 * Sets the headed option.
	 * 
	 * @param headed
	 * 		True if the CSV file has a header. A header is a row of cells that
	 * 		define what each column contains.
	 */
	public void setHeaded(boolean headed) 
	{
		this.headed = headed;
	}

	/**
	 * Returns the trimming option.
	 * 
	 * @return True if the values in the cells should be trimmed. Trimming removes
	 * 		   all whitespace in a value.
	 */
	public boolean isTrimming() 
	{
		return trimming;
	}

	/**
	 * Sets the trimming option.
	 * 
	 * @param trimming
	 * 		True if the values in the cells should be trimmed. Trimming removes
	 * 		all whitespace in a value.
	 */
	public void setTrimming(boolean trimming) 
	{
		this.trimming = trimming;
	}

	/**
	 * Returns the ignore empty lines option.
	 * 
	 * @return True if empty lines are to be ignored. If false empty lines will be
	 * 		   added as empty rows to the resulting table.
	 */
	public boolean isIgnoreEmpty() 
	{
		return ignoreEmpty;
	}

	/**
	 * Sets the ignore empty lines option.
	 * 
	 * @param ignoreEmpty
	 * 		True if empty lines are to be ignored. If false empty lines will be
	 * 		added as empty rows to the resulting table.
	 */
	public void setIgnoreEmpty(boolean ignoreEmpty) 
	{
		this.ignoreEmpty = ignoreEmpty;
	}

	/**
	 * Returns the strict option.
	 * 
	 * @return True if parsing should validate the number of columns in a row. 
	 * 		   When strict a valid CSV file has the same number of columns on 
	 * 		   every non-empty row.
	 */
	public boolean isStrict() 
	{
		return strict;
	}

	/**
	 * Sets the strict option.
	 * 
	 * @param strict
	 * 		True if parsing should validate the number of columns in a row. 
	 * 		When strict a valid CSV file has the same number of columns on 
	 * 		every non-empty row.
	 */
	public void setStrict(boolean strict) 
	{
		this.strict = strict;
	}
	
}
