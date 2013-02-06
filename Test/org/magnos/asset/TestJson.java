
package org.magnos.asset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.magnos.asset.json.Json;
import org.magnos.asset.json.JsonArray;
import org.magnos.asset.json.JsonFormat;
import org.magnos.asset.json.JsonObject;
import org.magnos.asset.json.JsonString;
import org.magnos.asset.json.JsonType;
import org.magnos.asset.json.JsonValue;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link JsonFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestJson
{

	@Test
	public void testBoolean() throws IOException
	{
		JsonValue v0 = Json.valueOf( "true" );
		assertNotNull( v0 );
		assertEquals( JsonType.Boolean, v0.getType() );
		assertEquals( Boolean.TRUE, v0.getObject() );
		assertEquals( "true", v0.toJson() );

		JsonValue v1 = Json.valueOf( "false" );
		assertNotNull( v1 );
		assertEquals( JsonType.Boolean, v1.getType() );
		assertEquals( Boolean.FALSE, v1.getObject() );
		assertEquals( "false", v1.toJson() );
	}

	@Test
	public void testJsonNull() throws IOException
	{
		JsonValue v0 = Json.valueOf( "null" );

		assertNotNull( v0 );
		assertEquals( JsonType.Null, v0.getType() );
		assertEquals( null, v0.getObject() );
		assertEquals( "null", v0.toJson() );
	}

	@Test
	public void testNull() throws IOException
	{
		JsonValue v0 = Json.valueOf( "" );

		assertNull( v0 );
	}

	@Test
	public void testNumber() throws IOException
	{
		assertTrue( Json.valueOf( "1.2" ).getObject() instanceof Float );
		assertTrue( Json.valueOf( "-1.0" ).getObject() instanceof Float );
		assertTrue( Json.valueOf( "3.4028235e+38" ).getObject() instanceof Float );
		assertTrue( Json.valueOf( "-3.4028235e+38" ).getObject() instanceof Float );
		assertTrue( Json.valueOf( "1.4e-45" ).getObject() instanceof Float );
		assertTrue( Json.valueOf( "-1.4e-45" ).getObject() instanceof Float );
	}

	@Test
	public void testArray() throws IOException
	{
		JsonArray v0 = Json.valueOf( "[]", JsonArray.class );
		assertEquals( 0, v0.length() );

		JsonArray v1 = Json.valueOf( "[0.0]", JsonArray.class );
		assertEquals( 1, v1.length() );
		assertEquals( 0.0f, v1.get( 0 ) );

		JsonArray v2 = Json.valueOf( "[ \"Hello!\" , 0.0 , true , null ]", JsonArray.class );
		assertEquals( 4, v2.length() );
		assertEquals( "Hello!", v2.get( 0 ) );
		assertEquals( 0.0f, v2.get( 1 ) );
		assertEquals( true, v2.get( 2 ) );
		assertEquals( null, v2.get( 3 ) );
	}

	@Test
	public void testObject() throws IOException
	{
		JsonObject v0 = Json.valueOf( "{}", JsonObject.class );
		assertEquals( 0, v0.size() );

		JsonObject v1 = Json.valueOf( "{x:0.0,y:5.0}", JsonObject.class );
		assertEquals( 2, v1.size() );
		assertTrue( v1.has( "x" ) );
		assertTrue( v1.has( "y" ) );
		assertEquals( 0.0f, v1.get( "x" ) );
		assertEquals( 5.0f, v1.get( "y" ) );

		JsonObject v2 = Json.valueOf( "{x:[0,1,2], y:{w:true,z:null},k:\"meow\"}", JsonObject.class );
		assertEquals( 3, v2.size() );
		JsonArray a0 = v2.getValue( "x", JsonArray.class );
		assertEquals( 3, a0.length() );
		assertEquals( 0, a0.get( 0 ) );
		assertEquals( 1, a0.get( 1 ) );
		assertEquals( 2, a0.get( 2 ) );
		JsonObject a1 = v2.getValue( "y", JsonObject.class );
		assertEquals( 2, a1.size() );
		assertEquals( true, a1.get( "w" ) );
		assertEquals( null, a1.get( "z" ) );
		JsonString a2 = v2.getValue( "k", JsonString.class );
		assertEquals( "meow", a2.get() );
	}
	
	@Test
	public void testSpeed() throws Exception
	{
		String text = getLargeJson();
		
		System.out.println( text.length() );
		
		long start = System.nanoTime();
		
		JsonValue obj = Json.valueOf( text );
		
		long end = System.nanoTime();

		String out = obj.toJson();
		
		System.out.println( out.length() );
		
		System.out.format( "Elapsed: %.6f\n", (end - start) * 0.000000001 );
	}
	
	private String getLargeJson() throws Exception
	{
		// > 8mb file
		AssetSource source = new ClasspathSource();
		InputStream stream = source.getStream( "big.json" );
		byte[] data = new byte[ stream.available() ];
		int read = stream.read( data );
		return new String( data, 0, read );
	}

}
