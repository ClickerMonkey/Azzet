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

package org.magnos.asset.source.db;

/**
 * 
 * Test Asset loading/saving with an Oracle database.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestOracle extends TestDatabase 
{

	/*


Server:			http://www.oracle.com/technetwork/database/express-edition/downloads/index.html 
Version:		10g release 2
Connector:		http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-10201-088211.html
				ojdbc14.jar
User: 			assetuser
Password:		assetpassword
Instructions:	Create the user, sign-in as user, create the table.
	
	
CREATE SEQUENCE asset_table_seq;
CREATE TABLE asset_table (
	id			NUMBER,
	name		VARCHAR(255) NOT NULL,
	asset		BLOB NOT NULL,
	PRIMARY KEY (id)
);
CREATE OR REPLACE TRIGGER asset_table_insert BEFORE INSERT ON asset_table
	FOR EACH ROW
		BEGIN
			SELECT asset_table_seq.nextval INTO :new.id FROM dual;
		END
/


	 */
	
	public TestOracle()
	{
		super("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@localhost:1521:XE", "assetuser", "assetpassword");
	}
	
}
