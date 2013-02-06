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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;

/**
 * A format for loading a {@link Table} from a CSV file.
 * 
 * TODO account for escaped characters and quoted values
 * 
 * @author Philip Diffenderfer
 *
 */
public class CsvFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new CsvFormat.
	 */
	public CsvFormat()
	{
		super(new String[] {"csv"}, Table.class);
	}
	
	@Override
	public AssetInfo getInfo(Class<?> type) 
	{
		return new CsvInfo();
	}

	@Override
	public Table loadAsset(InputStream input, AssetInfo assetInfo) throws Exception 
	{
		CsvInfo info = (CsvInfo)assetInfo;
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		
		final boolean trim = info.isTrimming();
		final boolean ignore = info.isIgnoreEmpty();
		final boolean strict = info.isStrict();
		final boolean headed = info.isHeaded();
		
		final String delim = info.getDelimiter();
		String line = null;
		
		// the number of actual rows.
		int rowIndex = 0;
		
		Table table = new Table();
		table.setColumns(-1);
		
		while ((line = reader.readLine()) != null)
		{
			// If the line is empty...
			if (line.isEmpty()) 
			{
				if (ignore && table.getColumns() >= 0) 
				{
					table.add(new Row(emptyCells(table.getColumns())));
				}
				
				// continue reading lines, this was not an actual row.
				continue;
			}
			
			String[] values = line.split(delim);
			
			// if parsing is strict and the number of columns has been 
			// determined already ensure that the current row has the right
			// number of columns.
			if (strict && table.getColumns() >= 0) 
			{
				if (values.length != table.getColumns()) {
					throw new RuntimeException();
				}
			}
			
			// set the header and the number of columns in the table.
			if (rowIndex == 0 && headed) 
			{
				table.setColumns(values.length);
				table.setHeader(new Row(toCells(values, trim)));
			}
			else 
			{
				// set the number of columns if unspecified.
				if (table.getColumns() < 0) {
					table.setColumns(values.length);
				}
				table.add(new Row(toCells(values, trim)));
			}
			
			rowIndex++;
		}
		
		return table;
	}
	
	/**
	 * Converts the array of strings to an of cells.
	 * 
	 * @param values
	 * 		The array of strings.
	 * @param trim
	 * 		True if the values should be trimmed before creating the cells or
	 * 		false if the values should remain untouched.
	 * @return
	 * 		An array of cells.
	 */
	private Cell[] toCells(String[] values, boolean trim)
	{
		Cell[] cells = new Cell[values.length];
		for (int i = 0; i < values.length; i++) {
			cells[i] = new Cell(trim ? values[i].trim() : values[i]);
		}
		return cells;
	}
	
	/**
	 * Returns an array of empty cells given the number of cells to return.
	 * 
	 * @param columns
	 * 		The number of cells to return.
	 * @return
	 * 		The array of cells.
	 */
	private Cell[] emptyCells(int columns) 
	{
		Cell[] cells = new Cell[columns];
		for (int i = 0; i < columns; i++) {
			cells[i] = new Cell(null);
		}
		return cells;
	}

}
