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

import java.util.ArrayList;

/**
 * A table is a list of rows. A table consists of rows which often have
 * the same number of columns. 
 * 
 * @author Philip Diffenderfer
 *
 */
public class Table extends ArrayList<Row>
{
	
/* @formatter:off
   "extended" ASCII
	private static final char CHAR_UL = 201; // upper left
	private static final char CHAR_UR = 187; // upper right
	private static final char CHAR_UT = 203; // upper T
	private static final char CHAR_BL = 200; // bottom left
	private static final char CHAR_BR = 188; // bottom right
	private static final char CHAR_BT = 202; // bottom T
	private static final char CHAR_LT = 204; // left T
	private static final char CHAR_RT = 185; // right T
	private static final char CHAR_X = 206; // X
	private static final char CHAR_V = 186; // V
	private static final char CHAR_H = 205; // H
	@formatter:on
*/

	private static final char CHAR_UL = '/'; // upper left
	private static final char CHAR_UR = '\\'; // upper right
	private static final char CHAR_UT = '+'; // upper T
	private static final char CHAR_BL = '\\'; // bottom left
	private static final char CHAR_BR = '/'; // bottom right
	private static final char CHAR_BT = '+'; // bottom T
	private static final char CHAR_LT = '+'; // left T
	private static final char CHAR_RT = '+'; // right T
	private static final char CHAR_X = '+'; // X
	private static final char CHAR_V = '|'; // V
	private static final char CHAR_H = '-'; // H
	
	
	private static final long serialVersionUID = 1L;
	
	private Row header;
	private int columns;
	
	/**
	 * Returns the number of columns in this table. The number of columns in
	 * the table is determined by taking the number of columns in the first row
	 * of the table.
	 *  
	 * @return The number of columns in this table.
	 */
	public int getColumns() 
	{
		return columns;
	}
	
	/**
	 * Sets the number of columns in this table. The number of columns in the
	 * table is determined by taking the number of columns in the first row
	 * of the table.
	 * 
	 * @param columns
	 * 		The number of columns in this table.
	 */
	public void setColumns(int columns) 
	{
		this.columns = columns;
	}
	
	/**
	 * Returns the header of this table. The header is just another row which
	 * defines the names of the columns for all of the rows to follow.
	 * 
	 * @return The reference to this tables header.
	 */
	public Row getHeader() 
	{
		return header;
	}
	
	/**
	 * Sets the header of this table. The header is just another row which 
	 * defines the names of the columns for all of the rows to follow.
	 * 
	 * @param header
	 * 		The new header of this table.
	 */
	public void setHeader(Row header) 
	{
		this.header = header;
	}

	/**
	 * Returns whether this table has a header.
	 * 
	 * @return True if this tables header has been defined, otherwise false.
	 */
	public boolean hasHeader()
	{
		return (header != null);
	}
	
	/**
	 * Returns the cell at the given row and column indices.
	 * 
	 * @param row
	 * 		The index of the row.
	 * @param column
	 * 		The index of the cell.
	 * @return The reference to the cell at the given row and column.
	 */
	public Cell get(int row, int column) 
	{
		return get(row).get(column);
	}
	
	/**
	 * Returns the delimited representation of this table as a string. This
	 * format is a valid CSV format. 
	 * 
	 * @param delimiter
	 * 		The delimiter to use to separate columns on a row.
	 * @return The string representation of this Table.
	 */
	public String toDelimitedString(String delimiter)
	{
		StringBuilder sb = new StringBuilder(columns * size());
		
		if (header != null) {
			accumulateRow(sb, delimiter, header);
			sb.append('\n');
		}
		for (Row row : this) {
			accumulateRow(sb, delimiter, row);
			sb.append('\n');
		}
		
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
	/**
	 * Returns the formatted representation of this table as a string. This 
	 * format will create a fixed width grid of cells which is fitted to
	 * the contents of the cells, ensuring the returned string is a minimal
	 * size.
	 * 
	 * @param padding
	 * 		True if spaces should surround the cell values, otherwise false.
	 * @param outlined
	 * 		True if the grid should have an outline of characters around it,
	 * 		otherwise false.
	 * @return The string representation of this Table.
	 */
	public String toFormattedString(boolean padding, boolean outlined)
	{
		StringBuilder sb = new StringBuilder(columns * size());
		
		int[] max = calculateMaxWidth();
		String[] formats = calculateFormats(max);

		if (outlined) {
			appendBar(sb, max, outlined, padding, CHAR_UL, CHAR_UR, CHAR_UT);
			sb.append('\n');
		}
		
		if (header != null) {
			accumulateFormatted(sb, formats, padding, outlined, header);
			sb.append('\n');

			appendBar(sb, max, outlined, padding, CHAR_LT, CHAR_RT, CHAR_X);
			sb.append('\n');
		}
		for (Row row : this) {
			accumulateFormatted(sb, formats, padding, outlined, row);
			sb.append('\n');
		}
		
		if (outlined) {
			appendBar(sb, max, outlined, padding, CHAR_BL, CHAR_BR, CHAR_BT);
			sb.append('\n');
		}
		
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
	/**
	 * Calculates the max width of each column by taking the values in the
	 * header (if given) and all of the rows.
	 * 
	 * @return An array of lengths where the i'th value is the length of the
	 * 		   longest value in column i.
	 */
	private int[] calculateMaxWidth()
	{
		int[] max = new int[columns];
		
		if (header != null) {
			accumulateMax(max, header);
		}
		for (Row row : this) {
			accumulateMax(max, row);
		}
		return max;
	}
	
	/**
	 * Accumulates the max widths with a given Row.
	 * 
	 * @param max
	 * 		The max widths.
	 * @param row
	 * 		The row.
	 */
	private void accumulateMax(int[] max, Row row)
	{
		int clamp = Math.min(columns, row.getColumns());
		for (int i = 0; i < clamp; i++) 
		{
			max[i] = Math.max(max[i], row.getString(i).length());
		}
	}

	/**
	 * Calculates the format strings per column given the array of max column
	 * widths.
	 * 
	 * @param max
	 * 		The array of max column widths.
	 * @return An array of format strings for each column.
	 */
	private String[] calculateFormats(int[] max)
	{
		String[] formats = new String[ max.length ];
		
		for (int i = 0; i < max.length; i++) 
		{
			formats[i] = String.format("%%%ds", max[i]);
		}
		
		return formats;
	}
	
	/**
	 * Appends a horizontal bar to the given StringBuilder.
	 * 
	 * @param sb
	 * 		The StringBuilder to append to.
	 * @param max
	 * 		The array of max column widths.
	 * @param outlined
	 * 		True if the grid should have an outline of characters around it,
	 * 		otherwise false.
	 * @param padding
	 * 		True if spaces should surround the cell values, otherwise false.
	 * @param left
	 * 		The character that should be appended first if outlined is true.
	 * @param right
	 * 		The character that should be appended last if outlined is true.
	 * @param middle
	 * 		The character that is the intersection of a horizontal and vertical
	 * 		line.
	 */
	private void appendBar(StringBuilder sb, int[] max, boolean outlined, boolean padding, char left, char right, char middle)
	{
		if (outlined) {
			sb.append( left );
		}
		for (int i = 0; i < columns; i++) 
		{
			if (i > 0) {
				sb.append( middle );
			}
			
			int j = max[i] + (padding ? 2 : 0);
			
			for (int k = 0; k < j; k++) { 
				sb.append(CHAR_H);
			}
		}
		if (outlined) {
			sb.append( right );
		}
	}
	
	/**
	 * Appends a delimited row to the given StringBuilder.
	 * 
	 * @param sb
	 * 		The StringBuilder to append to.
	 * @param delimiter
	 * 		The delimiter to place between the cells of the row.
	 * @param row
	 * 		The row to append to the StringBuilder.
	 */
	private void accumulateRow(StringBuilder sb, String delimiter, Row row)
	{
		for (int i = 0; i < row.getColumns(); i++) 
		{
			if (i > 0) {
				sb.append( delimiter );
			}
			sb.append( row.getString(i) );
		}
	}
	
	/**
	 * Appends a formatted row to the given StringBuilder.
	 * 
	 * @param sb
	 * 		The StringBuilder to append to.
	 * @param formats
	 * 		The format strings for each column.
	 * @param padding
	 * 		True if spaces should surround the cell values, otherwise false.
	 * @param outlined
	 * 		True if the grid should have an outline of characters around it,
	 * 		otherwise false.
	 * @param row
	 * 		The row to append to the StringBuilder.
	 */
	private void accumulateFormatted(StringBuilder sb, String[] formats, boolean padding, boolean outlined, Row row )
	{
		if (outlined) {
			sb.append(CHAR_V);
		}
		if (padding) {
			sb.append(' ');
		}
		
		for (int i = 0; i < row.getColumns(); i++)
		{
			if (i > 0) {
				if (padding) {
					sb.append(' ');
				}
				sb.append(CHAR_V);
				if (padding) {
					sb.append(' ');
				}
			}
			sb.append( String.format(formats[i], row.getString(i)) );
		}

		if (padding) {
			sb.append(' ');
		}
		if (outlined) {
			sb.append(CHAR_V);
		}
	}
	
	/**
	 * Returns the string representation of this Table as a formatted string 
	 * with no padding and no outline.
	 * 
	 * @return The String representation of this Table.
	 */
	public String toString()
	{
		return toFormattedString(false, false);
	}
	
}
