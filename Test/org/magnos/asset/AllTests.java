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

package org.magnos.asset;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.magnos.asset.source.TestClasspath;
import org.magnos.asset.source.TestFile;
import org.magnos.asset.source.TestJarSource;
import org.magnos.asset.source.TestMulticast;
import org.magnos.asset.source.TestTcp;
import org.magnos.asset.source.TestUdp;
import org.magnos.asset.source.TestWeb;

@RunWith(Suite.class)
@Suite.SuiteClasses(
{
	
	/** SOURCES **/
//	TestMySQL.class, // you need to have a MySQL server setup
//	TestOracle.class, // you need to have an Oracle server setup
//	TestPostgreSQL.class, // you need to have a PostgreSQL server setup
	TestClasspath.class,
	TestFile.class,
//	TestFtp.class, // you need to have an FTP server setup
	TestJarSource.class,
	TestMulticast.class,
//	TestSsl.class, // this requires program arguments
	TestTcp.class,
	TestUdp.class,
	TestWeb.class,

	/** CORE **/
	TestAssets.class,
	
	/** FORMATS **/
	TestAudio.class,
	TestClass.class,
	TestCsv.class,
	TestDat.class,
	TestFont.class,
	TestGif.class,
	TestGzip.class,
	TestImage.class,
	TestJar.class,
	TestJson.class,
	TestMidi.class,
	TestProperties.class,
	TestText.class,
	TestVersionedClass.class,
	TestXml.class,
	TestZip.class
	
})
public class AllTests 
{
	
	@AfterClass
	public static void logStats()
	{
		System.err.format( "Cache Hits: %d\n", Assets.getCacheHits() );
		System.err.format( "Cache Misses: %d\n", Assets.getCacheMisses() );
	}
	
}
