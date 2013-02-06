
package org.magnos.asset.io;

import java.io.IOException;
import java.io.Reader;


/**
 * A simple parser for reading characters. The parser keeps track of character
 * index, line number, and column index.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class CharacterReader
{

	public static final char NEWLINE = '\n';
	public static final char TAB = '\t';
	public static final char RETURN = '\r';
	public static final char FORMFEED = '\f';
	public static final char BACKSPACE = '\b';

	public static final char CHAR_ESCAPE = '\\';
	public static final char CHAR_NEWLINE = 'n';
	public static final char CHAR_TAB = 't';
	public static final char CHAR_RETURN = 'r';
	public static final char CHAR_FORMFEED = 'f';
	public static final char CHAR_BACKSPACE = 'b';
	public static final char CHAR_UNICODE = 'u';

	public static final int NO_DATA = -1;
	public static final int READLIMIT = 6;

	/**
	 * Whitespace are all characters <= the space character.
	 */
	public static final CharacterSet SET_WHITESPACE = new CharacterSet( new char[][] { { '\0', ' ' } } );

	/**
	 * Control characters are all characters < space character.
	 */
	public static final CharacterSet SET_CONTROL_CHARACTERS = new CharacterSet( new char[][] { { '\0', ' ' - 1 } } );

	/**
	 * Hex characters are any digit and any letter in any case.
	 */
	public static final CharacterSet SET_HEX = new CharacterSet( new char[][] { { '0', '9' }, { 'a', 'f' }, { 'A', 'F' } } );

	public Reader in;
	public int data;
	public int line;
	public int character;
	public int column;
	public int lastColumn;

	/**
	 * Instantiates a new CharacterReader.
	 * 
	 * @param in
	 *        The reader to take input from.
	 */
	public CharacterReader( Reader in )
	{
		this.in = in;
	}

	/**
	 * Parses a single character from the current character (data). If escapable
	 * is true and the current character is an escape character, then possible
	 * values will be determined. If the next character is a control character
	 * and control characters are not allowed an {@link IOException} is thrown.
	 * If the next character is a marker for a control character, the actual
	 * control character is returned. If the next character is a marker for
	 * unicode then the next 4 hex characters are read to determine the unicode
	 * value.
	 * 
	 * @param escapable
	 *        If escapable characters are permitted to be read.
	 * @param rejectControlCharacters
	 *        If control characters following an escape are allowed.
	 * @param acceptUnicode
	 *        If unicode is acceptable after an escape.
	 * @return The character parsed.
	 * @throws IOException
	 *         An error occurred reading from the reader.
	 */
	public char parseCharacter( boolean escapable, boolean rejectControlCharacters, boolean acceptUnicode ) throws IOException
	{
		if (escapable && data == CHAR_ESCAPE)
		{
			readData();

			if (rejectControlCharacters && SET_CONTROL_CHARACTERS.has( data ))
			{
				throw new IOException( "Control characters are not acceptable " + getCursor() );
			}

			switch (data)
			{
			case NO_DATA:
				throw new IOException( "Expected character after escape " + getCursor() );
			case CHAR_TAB:
				data = TAB;
				break;
			case CHAR_NEWLINE:
				data = NEWLINE;
				break;
			case CHAR_RETURN:
				data = RETURN;
				break;
			case CHAR_FORMFEED:
				data = FORMFEED;
				break;
			case CHAR_BACKSPACE:
				data = BACKSPACE;
				break;
			case CHAR_UNICODE:
				if (acceptUnicode)
				{
					int h0 = readHexDigitIndexed( 0 );
					int h1 = readHexDigitIndexed( 1 );
					int h2 = readHexDigitIndexed( 2 );
					int h3 = readHexDigitIndexed( 3 );
					data = (h0 << 12) | (h1 << 8) | (h2 << 4) | h3;
				}
				break;
			}
		}

		return (char)data;
	}

	/**
	 * Reads a hex digit from the next character in the reader.
	 * 
	 * @return The hex value, between 0 and 15 inclusive.
	 * @throws IOException
	 *         An error occurred reading the next error.
	 */
	public int readHexDigit() throws IOException
	{
		int hex = CharacterReader.hex( readData() );

		if (hex == NO_DATA)
		{
			throw new IOException( "Expected hex digit " + getCursor() );
		}

		return hex;
	}

	/**
	 * Reads a hex digit from the next character in the reader.
	 * 
	 * @param index
	 *        The index of the hex digit in a unicode character. This is used to
	 *        create a more useful exception message.
	 * @return The hex value, between 0 and 15 inclusive.
	 * @throws IOException
	 *         An error occurred reading the next character.
	 */
	public int readHexDigitIndexed( int index ) throws IOException
	{
		int hex = CharacterReader.hex( readData() );

		if (hex == NO_DATA)
		{
			throw new IOException( "Expected hex digit " + index + " " + getCursor() );
		}

		return hex;
	}

	/**
	 * Reads and returns a string until any one of the characters in a set is
	 * reached.
	 * 
	 * @param stop
	 *        The set to use to determine when to stop.
	 * @param includeCurrent
	 *        If the current character should be included in the output.
	 * @param readAsCharacter
	 *        If {@link #parseCharacter(boolean, boolean, boolean)} should be
	 *        used to build characters in the string, if not the raw character
	 *        value is appended to the output.
	 * @param backup
	 *        If the reader should back up one character so the next character in
	 *        the reader is the character that it stopped on.
	 * @return The string read from the current character to a stopping
	 *         character.
	 * @throws IOException
	 *         An error occurred reading the next character.
	 */
	public String readUntil( CharacterSet stop, boolean includeCurrent, boolean readAsCharacter, boolean backup ) throws IOException
	{
		StringBuilder out = new StringBuilder();

		in.mark( READLIMIT );

		if (includeCurrent)
		{
			out.append( (char)data );
		}

		while (readData() != NO_DATA && !stop.has( data ))
		{
			if (readAsCharacter)
			{
				out.append( parseCharacter( true, true, true ) );
			}
			else
			{
				out.append( (char)data );
			}

			in.mark( READLIMIT );
		}

		if (backup && stop.has( data ))
		{
			unreadData();

			in.reset();
		}

		return out.toString();
	}

	/**
	 * Reads past all whitespace. This will return the next character in the
	 * reader which is not whitespace.
	 * 
	 * @return The non-whitespace character read or -1 if end of reader.
	 * @throws IOException
	 *         An error occurred reading the next character.
	 */
	public int readPastWhitespace() throws IOException
	{
		while (readData() != NO_DATA && SET_WHITESPACE.has( data ))
			;

		return data;
	}

	/**
	 * Reads the next character.
	 * 
	 * @return
	 * 	The next character.
	 * @throws IOException
	 * 	An error occurred reading the next character.
	 */
	public int readData() throws IOException
	{
		data = in.read();

		character++;

		if (data == NEWLINE)
		{
			line++;
			lastColumn = column;
			column = 0;
		}
		else if (data != NO_DATA)
		{
			column++;
		}

		return data;
	}

	/**
	 * Un-reads the last character read.
	 */
	public void unreadData()
	{
		if (data == NEWLINE)
		{
			line--;
			column = lastColumn;
			character--;
		}
		else if (data != NO_DATA)
		{
			column--;
			character--;
		}
	}

	/**
	 * @return A description of the reader consisting of the index of the current
	 *         character, it's line, and it's column.
	 */
	public String getCursor()
	{
		return String.format( "at character %c at index %d line %d column %d", (char)data, character, line, column );
	}

	/**
	 * Converts the character to a hex value. If the character is not a valid hex
	 * digit then {@link #NO_DATA} is returned, otherwise a number between 0 and
	 * 15 inclusive.
	 * 
	 * @param x
	 *        The character to calculate a hex value from.
	 * @return The hex value.
	 */
	public static int hex( int x )
	{
		if (x >= '0' && x <= '9')
		{
			return x - '0';
		}
		if (x >= 'a' && x <= 'f')
		{
			return x - 'a' + 10;
		}
		if (x >= 'A' && x <= 'F')
		{
			return x - 'A' + 10;
		}

		return NO_DATA;
	}

}
