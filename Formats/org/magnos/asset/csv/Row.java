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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;


/**
 * A row in a table which contains a fixed number of cells.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Row implements Iterable<Cell>
{

	// the array of cells.
	private final Cell[] cells;

	/**
	 * Instantiates a new Row given an array of cells.
	 * 
	 * @param cells
	 *        The cells contained in the Row.
	 */
	public Row( Cell[] cells )
	{
		this.cells = cells;
	}

	/**
	 * Returns the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the cell.
	 */
	public Cell get( int c )
	{
		return cells[c];
	}

	/**
	 * Sets the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @param v
	 *        The cell to set.
	 */
	public void set( int c, Cell v )
	{
		cells[c] = v;
	}

	/**
	 * Sets the cells value to the given string at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @param x
	 *        The value to set in the cell.
	 */
	public void set( int c, String x )
	{
		cells[c].set( x );
	}

	/**
	 * Sets the cells value to the string representation of the given object at
	 * the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @param o
	 *        The object to take the string representation of.
	 */
	public void set( int c, Object o )
	{
		cells[c].set( o.toString() );
	}

	/**
	 * Returns the String representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the String or null if the cell was empty or the
	 *         value was not a valid String.
	 */
	public String getString( int c )
	{
		return cells[c].get();
	}

	/**
	 * Returns the Boolean representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the Boolean or null if the cell was empty or the
	 *         value was not a valid Boolean.
	 */
	public Boolean getBoolean( int c )
	{
		return cells[c].getBoolean();
	}

	/**
	 * Returns the Byte representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the Byte or null if the cell was empty or the
	 *         value was not a valid Byte.
	 */
	public Byte getByte( int c )
	{
		return cells[c].getByte();
	}

	/**
	 * Returns the Short representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the Short or null if the cell was empty or the
	 *         value was not a valid Short.
	 */
	public Short getShort( int c )
	{
		return cells[c].getShort();
	}

	/**
	 * Returns the Integer representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the Integer or null if the cell was empty or the
	 *         value was not a valid Integer.
	 */
	public Integer getInt( int c )
	{
		return cells[c].getInt();
	}

	/**
	 * Returns the Long representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the Long or null if the cell was empty or the
	 *         value was not a valid Long.
	 */
	public Long getLong( int c )
	{
		return cells[c].getLong();
	}

	/**
	 * Returns the Float representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the Float or null if the cell was empty or the
	 *         value was not a valid Float.
	 */
	public Float getFloat( int c )
	{
		return cells[c].getFloat();
	}

	/**
	 * Returns the Double representation of the cell at the given column index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the Double or null if the cell was empty or the
	 *         value was not a valid Double.
	 */
	public Double getDouble( int c )
	{
		return cells[c].getDouble();
	}

	/**
	 * Returns the BigDecimal representation of the cell at the given column
	 * index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the BigDecimal or null if the cell was empty or
	 *         the value was not a valid BigDecimal.
	 */
	public BigDecimal getBigDecimal( int c )
	{
		return cells[c].getBigDecimal();
	}

	/**
	 * Returns the BigInteger representation of the cell at the given column
	 * index.
	 * 
	 * @param c
	 *        The column index of the cell.
	 * @return The reference to the BigInteger or null if the cell was empty or
	 *         the value was not a valid BigInteger.
	 */
	public BigInteger getBigInteger( int c )
	{
		return cells[c].getBigInteger();
	}

	/**
	 * Returns the number of columns in this Row.
	 * 
	 * @return The number of columns in this row.
	 */
	public int getColumns()
	{
		return cells.length;
	}

	/**
	 * Returns an iterator for iterating over the cells in this row.
	 */
	@Override
	public Iterator<Cell> iterator()
	{
		return Arrays.asList( cells ).iterator();
	}

}
