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
 * Test Asset loading/saving with a PostgreSQL database.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestPostgreSQL extends TestDatabase 
{
	
	/*


Server:			http://www.enterprisedb.com/products-services-training/pgdownload#windows
Version: 		9.0.4-1
Connector:		http://jdbc.postgresql.org/download.html
				postgresql-9.0-801.jdbc3.jar
Database:		assets
Schema:			public
User:			assetuser
Password: 		assetpassword
Instructions:	Create database, create table and use default schema (public).

	
SET SESSION AUTHORIZATION "assetuser";
CREATE TABLE "asset_table" (
	"id"		SERIAL,
	"name"		TEXT NOT NULL,
	"asset"		BYTEA NOT NULL,
	PRIMARY KEY ("id")
);


	 */

	public TestPostgreSQL()
	{
		super("org.postgresql.Driver", "jdbc:postgresql:assets", "assetuser", "assetpassword");
	}
	
}
