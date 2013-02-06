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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.magnos.asset.io.CharacterReader;


/**
 * A writer for JSON objects with formatting.
 * 
 * TODO document
 * 
 * @author Philip Diffenderfer
 * 
 */
public abstract class JsonWriter
{

	public static final boolean DEFAULT_STRINGS_QUOTED = true;

	private boolean stringsQuoted;
	private boolean newlineObjectStart;
	private boolean newlineArrayStart;
	private boolean newlineArraySeparator;
	private boolean newlineMemberSeparator;
	private boolean indent;
	private String indentation = "";
	private int depth;

	public JsonWriter()
	{
		this( DEFAULT_STRINGS_QUOTED );
	}

	public JsonWriter( boolean stringsQuoted )
	{
		this.stringsQuoted = stringsQuoted;
	}

	public abstract void write( char c ) throws IOException;

	public abstract void write( String s ) throws IOException;

	public void write( Object o ) throws IOException
	{
		write( o == null ? Json.NULL : o.toString() );
	}

	public void writeString( String s ) throws IOException
	{
		if (s == null)
		{
			write( Json.NULL );
		}
		else
		{
			write( Json.STRING_START );
			write( Json.encode( s ) );
			write( Json.STRING_END );
		}
	}

	public void writeName( String s ) throws IOException
	{
		if (s == null)
		{
			write( Json.NULL );
		}
		else if (stringsQuoted)
		{
			write( Json.STRING_START );
			write( Json.encode( s ) );
			write( Json.STRING_END );
		}
		else
		{
			write( Json.encode( s ) );
		}
	}

	public void writeArraySeparator() throws IOException
	{
		write( Json.ARRAY_SEPARATOR );

		if (newlineArraySeparator)
		{
			newline();
		}
	}

	public void writeMemberSeparator() throws IOException
	{
		write( Json.ARRAY_SEPARATOR );

		if (newlineMemberSeparator)
		{
			newline();
		}
	}

	public void startArray() throws IOException
	{
		write( Json.ARRAY_START );

		depth++;

		if (newlineArrayStart)
		{
			newline();
		}
	}

	public void endArray() throws IOException
	{
		write( Json.ARRAY_END );

		depth--;
	}

	public void startObject() throws IOException
	{
		write( Json.OBJECT_START );

		depth++;

		if (newlineObjectStart)
		{
			newline();
		}
	}

	public void endObject() throws IOException
	{
		write( Json.OBJECT_END );

		depth--;
	}

	public void newline() throws IOException
	{
		write( CharacterReader.NEWLINE );

		if (indent)
		{
			for (int i = 0; i < depth; i++)
			{
				write( indentation );
			}
		}
	}

	public boolean isStringsQuoted()
	{
		return stringsQuoted;
	}

	public void setStringsQuoted( boolean stringsQuoted )
	{
		this.stringsQuoted = stringsQuoted;
	}

	public boolean isNewlineObjectStart()
	{
		return newlineObjectStart;
	}

	public void setNewlineObjectStart( boolean newlineObjectStart )
	{
		this.newlineObjectStart = newlineObjectStart;
	}

	public boolean isNewlineArrayStart()
	{
		return newlineArrayStart;
	}

	public void setNewlineArrayStart( boolean newlineArrayStart )
	{
		this.newlineArrayStart = newlineArrayStart;
	}

	public boolean isNewlineArraySeparator()
	{
		return newlineArraySeparator;
	}

	public void setNewlineArraySeparator( boolean newlineArraySeparator )
	{
		this.newlineArraySeparator = newlineArraySeparator;
	}

	public boolean isNewlineMemberSeparator()
	{
		return newlineMemberSeparator;
	}

	public void setNewlineMemberSeparator( boolean newlineMemberSeparator )
	{
		this.newlineMemberSeparator = newlineMemberSeparator;
	}

	public boolean isIndent()
	{
		return indent;
	}

	public void setIndent( boolean indent )
	{
		this.indent = indent;
	}

	public String getIndentation()
	{
		return indentation;
	}

	public void setIndentation( String indentation )
	{
		this.indentation = indentation;
	}

	public int getDepth()
	{
		return depth;
	}

	public void setDepth( int depth )
	{
		this.depth = depth;
	}

	public static JsonWriter forOutputStream( final OutputStream oos )
	{
		final DataOutputStream dos = new DataOutputStream( oos );

		return new JsonWriter() {

			public void write( String s ) throws IOException
			{
				dos.writeChars( s );
			}

			public void write( char c ) throws IOException
			{
				dos.writeChar( c );
			}
		};
	}

	public static JsonWriter forAppender( final Appendable appendable )
	{
		return new JsonWriter() {

			public void write( String s ) throws IOException
			{
				appendable.append( s );
			}

			public void write( char c ) throws IOException
			{
				appendable.append( c );
			}
		};
	}

	public static JsonWriter forWriter( final Writer writer )
	{
		return new JsonWriter() {

			public void write( String s ) throws IOException
			{
				writer.write( s );
			}

			public void write( char c ) throws IOException
			{
				writer.write( c );
			}
		};
	}

}
