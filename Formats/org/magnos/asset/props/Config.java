
package org.magnos.asset.props;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


/**
 * An extension to the {@link Properties} class.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Config extends Properties
{

	private static final long serialVersionUID = 1L;

	private static final Set<String> TRUES = new HashSet<String>( Arrays.asList( "true", "t", "1", "yes", "ya", "y" ) );
	private static final Set<String> FALSES = new HashSet<String>( Arrays.asList( "false", "f", "0", "bo", "nope", "n" ) );

	/**
	 * Instantiates a new Config.
	 */
	public Config()
	{
	}

	/**
	 * Instantiates a new Config.
	 * 
	 * @param defaults
	 *        A default set of properties.
	 */
	public Config( Properties defaults )
	{
		super( defaults );
	}

	/**
	 * Returns true if the property with the given name exists.
	 * 
	 * @param name
	 *        The property name.
	 * @return True if the property exists, otherwise false.
	 */
	public boolean has( String name )
	{
		return containsKey( name );
	}

	/**
	 * Returns the given value or the defaultValue if the given value is null. If
	 * the given value is null the defaultValue is placed into the Config and
	 * returned.
	 * 
	 * @param <T>
	 *        The value type.
	 * @param name
	 *        The property name.
	 * @param value
	 *        The property value.
	 * @param defaultValue
	 *        The default property value.
	 * @return The value or the defaultValue if the value is null.
	 */
	private <T> T getDefault( String name, T value, T defaultValue )
	{
		if (value == null)
		{
			value = defaultValue;
			put( name, defaultValue.toString() );
		}
		return value;
	}

	/**
	 * Returns the property with the given name.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public String get( String name )
	{
		return super.getProperty( name );
	}

	/**
	 * Returns the property with the given name or the defaultValue if the
	 * property doesn't exist.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public String get( String name, String defaultValue )
	{
		return getDefault( name, getProperty( name ).toString(), defaultValue );
	}

	/**
	 * Returns the boolean property with the given name or the defaultValue if
	 * the property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public boolean getBoolean( String name, boolean defaultValue )
	{
		return getDefault( name, getBoolean( name ), defaultValue );
	}

	/**
	 * Returns the boolean property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public Boolean getBoolean( String name )
	{
		String x = get( name );
		if (x == null)
		{
			return null;
		}
		x = x.toLowerCase();
		if (TRUES.contains( x ))
		{
			return Boolean.TRUE;
		}
		if (FALSES.contains( x ))
		{
			return Boolean.FALSE;
		}
		return null;
	}

	/**
	 * Returns the byte property with the given name or the defaultValue if the
	 * property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public byte getByte( String name, byte defaultValue )
	{
		return getDefault( name, getByte( name ), defaultValue );
	}

	/**
	 * Returns the byte property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public Byte getByte( String name )
	{
		try
		{
			return Byte.parseByte( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the short property with the given name or the defaultValue if the
	 * property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public short getShort( String name, short defaultValue )
	{
		return getDefault( name, getShort( name ), defaultValue );
	}

	/**
	 * Returns the short property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public Short getShort( String name )
	{
		try
		{
			return Short.parseShort( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the integer property with the given name or the defaultValue if
	 * the property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public int getInt( String name, int defaultValue )
	{
		return getDefault( name, getInt( name ), defaultValue );
	}

	/**
	 * Returns the integer property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public Integer getInt( String name )
	{
		try
		{
			return Integer.parseInt( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the long property with the given name or the defaultValue if the
	 * property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public long getLong( String name, long defaultValue )
	{
		return getDefault( name, getLong( name ), defaultValue );
	}

	/**
	 * Returns the long property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public Long getLong( String name )
	{
		try
		{
			return Long.parseLong( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the float property with the given name or the defaultValue if the
	 * property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public float getFloat( String name, float defaultValue )
	{
		return getDefault( name, getFloat( name ), defaultValue );
	}

	/**
	 * Returns the float property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public Float getFloat( String name )
	{
		try
		{
			return Float.parseFloat( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the double property with the given name or the defaultValue if the
	 * property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public double getDouble( String name, double defaultValue )
	{
		return getDefault( name, getDouble( name ), defaultValue );
	}

	/**
	 * Returns the double property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public Double getDouble( String name )
	{
		try
		{
			return Double.parseDouble( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the BigDecimal property with the given name or the defaultValue if
	 * the property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public BigDecimal getDecimal( String name, BigDecimal defaultValue )
	{
		return getDefault( name, getDecimal( name ), defaultValue );
	}

	/**
	 * Returns the BigDecimal property with the given name or null if the
	 * property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public BigDecimal getDecimal( String name )
	{
		try
		{
			return new BigDecimal( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the BigInteger property with the given name or the defaultValue if
	 * the property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public BigInteger getInteger( String name, BigInteger defaultValue )
	{
		return getDefault( name, getInteger( name ), defaultValue );
	}

	/**
	 * Returns the BigInteger property with the given name or null if the
	 * property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public BigInteger getInteger( String name )
	{
		try
		{
			return new BigInteger( getProperty( name ) );
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns the String[] property with the given name or the defaultValue if
	 * the property doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @param defaultValue
	 *        The default property value.
	 * @return The property value.
	 */
	public String[] getStrings( String name, String delimiter, String[] defaultValue )
	{
		return getDefault( name, getStrings( name, delimiter ), defaultValue );
	}

	/**
	 * Returns the String[] property with the given name or null if the property
	 * doesn't exist or is not in proper format.
	 * 
	 * @param name
	 *        The property name.
	 * @return The property value.
	 */
	public String[] getStrings( String name, String delimiter )
	{
		String x = getProperty( name );

		return (x != null ? x.split( delimiter ) : null);
	}

}
