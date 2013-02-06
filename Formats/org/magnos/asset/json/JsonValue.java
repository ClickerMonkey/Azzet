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

package org.magnos.asset.json;

import java.io.IOException;


/**
 * A generic value in JSON.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface JsonValue
{

	/**
	 * @return The internal object.
	 */
	public Object getObject();

	/**
	 * @return The value type.
	 */
	public JsonType getType();

	/**
	 * @return This value converted to a JSON string.
	 */
	public String toJson();

	/**
	 * Writes this value out to a writer. A writer has formatting options and can
	 * write out to several different things.
	 * 
	 * @param out
	 *        The JsonWriter to write to.
	 * @throws IOException
	 *         An error occurred writing this value out to the writer.
	 */
	public void write( JsonWriter out ) throws IOException;
}
