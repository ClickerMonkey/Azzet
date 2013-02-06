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

package org.magnos.asset.ini;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.magnos.asset.FormatUtility;

public class IniReader 
{

	private final char[] ini;
	private int pos;
	
	public IniReader(InputStream input, String charset) throws IOException
	{
		this.ini = getCharacters(input, charset);
	}
	
	private char[] getCharacters(InputStream input, String charset) throws IOException
	{
		ByteArrayOutputStream output = FormatUtility.getOutput(input);
		
		return output.toString(charset).toCharArray();
	}
	
	public Ini parse()
	{
		Ini N = new Ini();
		Section section = null;

		while (pos < ini.length) 
		{
			char c = next();
			
			if (isLineEnd(c)) {
				nextLine();
			}
			else {
				if (c == '[') {
					section = new Section( readSection() );
					N.add(section);
				}
				else {
					String name = trim( readName() );
					if (name.length() == 0) {
						throw new RuntimeException();
					}
					
					char equals = next();
					if (equals != '=') {
						throw new RuntimeException();
					}
					
					String value = readValue();
					
					
					if (section != null) {
						section.put(name, value);
					}
					N.addProperty(name, value);
				}
			}
		}
		
		return N;
	}
	
	private String trim(String x)
	{
		x = x.trim();
		if (x.startsWith("\"") && x.endsWith("\"")) {
			x = x.substring(1, x.length() - 1);
		}
		return x;
	}
	
	private String readSection()
	{
		StringBuilder sb = new StringBuilder();
		while ( hasCurrent() && !isSectionEnd( curr() ) ) 
		{
			read( sb );
		}
		pos++;
		return sb.toString();
	}
	
	private String readName()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( last() );
		while ( hasCurrent() && !isNameEnd( curr() ) ) 
		{
			read( sb );
		}
		return sb.toString();
	}
	
	private String readValue()
	{
		StringBuilder sb = new StringBuilder();
		
		int index = 0, min = Integer.MAX_VALUE, max = 0;
		
		while ( hasCurrent() && !isValueEnd( curr() ) ) 
		{
			boolean escaped = read( sb );
			if (escaped) {
				min = Math.min(min, index);
				max = Math.max(max, index);
			}
			index++;
		}
		min = Math.min(min, sb.length());
		
		if (index == 0) {
			return "";
		}
		
		int i = 0;
		while (i < min && isWhitespace(sb.charAt(i))) {
			i++;
		}
		
		int j = sb.length();
		while (j > max && isWhitespace(sb.charAt(j - 1))) {
			j--;
		}
		
		if (i > j) {
			return "";
		}
		
		if (sb.charAt(i) == '"' && sb.charAt(j - 1) == '"') {
			i++;
			j--;
		}
		
		if (i > j) {
			return "";
		}
		
		return sb.substring(i, j);
	}
	
	private boolean isSectionEnd(char c)
	{
		return (c == ']' || c < ' ');
	}
	
	private boolean isNameEnd(char c)
	{
		return (c == '=' || c < ' ');
	}
	
	private boolean isValueEnd(char c)
	{
		return (c == '#' || c == ';' || c < ' ');
	}
	
	private boolean isLineEnd(char c)
	{
		return (c == '#' || c == ';' || c < ' ');
	}
	
	private boolean isWhitespace(char c)
	{
		return (c <= ' ');
	}
	
	private void nextLine()
	{
		while (hasCurrent() && next() != '\n') {
			
		}
//		swallow('\r');
//		swallow('\n');
//		pos++;
	}
	
//	private void swallow(char terminating)
//	{
//		while (pos < ini.length && ini[pos] != terminating) {
//			pos++;
//		}
//	}
	
	private boolean hasCurrent()
	{
		return (pos < ini.length);
	}
	
	private char curr()
	{
		return ini[pos];
	}
	
	private char next()
	{
		return ini[pos++];
	}
	
	private char last()
	{
		return ini[pos - 1];
	}
	
	private boolean read( StringBuilder sb )
	{
		char c = next();
		
		boolean escaped = (c == '\\');
		
		if (escaped)
		{
			char n = ini[pos++];
			
			switch (n)
			{
			// basic escape characters
			case '\\':	c = '\\'; break;
			case 'n':	c = '\n'; break;
			case '0':	c = '\0'; break;
			case 'b':	c = '\b'; break;
			case 't':	c = '\t'; break;
			case 'r':	c = '\r'; break;
			case ';':	c = ';'; break;
			case '#':	c = '#'; break;
			case '=':	c = '='; break;
			case ':':	c = ':'; break;
			case '[':	c = '['; break;
			case ']':	c = ']'; break;
			case 'x': 	c = hex(); break;
			// continuations
			case ' ':
			case '\t':
			case '\r':
			case '\n':
				nextLine();
				return read( sb );
				
			default:
				throw new RuntimeException("Invalid escaped character: " + n);
			}
		}
		
		sb.append( c );
		
		return escaped;
	}
	
	private char hex()
	{
		int b0 = toHex( next() );
		int b1 = toHex( next() );
		int b2 = toHex( next() );
		int b3 = toHex( next() );
		
		return (char)((b0 << 12) | (b1 << 8) | (b2 << 4) | (b3 << 0));
	}

	private int toHex(char b)
	{
		if (b >= '0' && b <= '9') {
			return (b - '0');
		}
		if (b >= 'A' && b <= 'F') {
			return (b - 'A') + 10;
		}
		if (b >= 'a' && b <= 'f') {
			return (b - 'a') + 10;
		}
		throw new NullPointerException("Invalid hex character: " + b);
	}
	
}
