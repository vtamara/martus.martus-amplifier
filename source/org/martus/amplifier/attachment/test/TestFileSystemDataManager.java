package org.martus.amplifier.attachment.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import junit.framework.Assert;

import org.martus.amplifier.attachment.AttachmentStorageException;
import org.martus.amplifier.attachment.DataManager;
import org.martus.amplifier.attachment.FileSystemDataManager;
import org.martus.amplifier.main.MartusAmplifier;
import org.martus.common.crypto.MockMartusSecurity;
import org.martus.common.database.FileDatabase.MissingAccountMapException;
import org.martus.common.database.FileDatabase.MissingAccountMapSignatureException;
import org.martus.common.packet.UniversalId;
import org.martus.util.DirectoryTreeRemover;
import org.martus.util.StringInputStream;

public class TestFileSystemDataManager 
	extends TestAbstractDataManager
{
	
	public TestFileSystemDataManager(String name) 
	{
		super(name);
	}
	
	protected void setUp() throws Exception
	{
		super.setUp();
		MockMartusSecurity security = new MockMartusSecurity();
		security.createKeyPair();
		dataManager = new FileSystemDataManager(getTestBasePath(), security);
	}
	
	public void testMissingAccountMap() throws Exception
	{
		File missingAccountMap = null;
		File emptyAccount = null;
		try
		{
			missingAccountMap = createTempDirectory();
			emptyAccount = new File(missingAccountMap.getAbsolutePath() + "\\ab00");
			emptyAccount.deleteOnExit();
			emptyAccount.mkdir();
			new FileSystemDataManager(missingAccountMap.getAbsolutePath());
			fail("Should have thrown");
		}
		catch (MissingAccountMapException expectedException)
		{
		}		
		finally
		{
			DirectoryTreeRemover.deleteEntireDirectoryTree(missingAccountMap);
		}
	}

	public void testInvalidAccountMap() throws Exception
	{
		File baseDir = null;
		File accountDir = null;
		try
		{
			baseDir = createTempDirectory();
			accountDir = new File(baseDir.getAbsolutePath() + "\\ab00");
			accountDir.deleteOnExit();
			accountDir.mkdir();
			File accountMap = new File(baseDir.getAbsolutePath() + "\\acctmap.txt");
			accountMap.deleteOnExit();
			accountMap.createNewFile();
			new FileSystemDataManager(baseDir.getAbsolutePath());
			fail("Should have thrown");
		}
		catch (MissingAccountMapSignatureException expectedException)
		{
		}		
		finally
		{
			DirectoryTreeRemover.deleteEntireDirectoryTree(baseDir);
		}
	}

	public void testFileSystemClearAllAttachments() 
		throws AttachmentStorageException, IOException
	{
		UniversalId id = UniversalId.createDummyUniversalId();
		String testString = "FileSystemClearAll";
		InputStream sin = new StringInputStream(testString);
		try {
			dataManager.putAttachment(id, sin);
		} finally {
			sin.close();
		}
		
		dataManager.clearAllAttachments();
		File attachmentDir = new File(
			getTestBasePath());
		Assert.assertEquals(
			"attachments directory not empty", 
			0, attachmentDir.listFiles().length);
	}
	
	public void testAccountWithFileSeparators() 
		throws IOException, AttachmentStorageException
	{
		UniversalId id = UniversalId.createFromAccountAndPrefix(
			"AnAccount/With/Slashes", "Test");
		String testString = "AccountWithFileSeparators";
		InputStream sin = new StringInputStream(testString);
		try {
			dataManager.putAttachment(id, sin);
		} finally {
			sin.close();
		}
		
		InputStream in = null;
		try {
			in = dataManager.getAttachment(id);
			Assert.assertEquals(testString, inputStreamToString(in));
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	public void testGetContactInfoFile() throws Exception
	{
		String accountId = "test";
		File info = dataManager.getContactInfoFile(accountId);
		assertTrue("file should end in contactInfo.dat", info.getAbsolutePath().endsWith("contactInfo.dat"));
	}
	
	public void testWriteContactInfoToFile() throws Exception
	{
		String id = "abc";
		File infoFile = dataManager.getContactInfoFile(id);
		infoFile.delete();
		assertFalse("should not contain any contact info yet", infoFile.exists());

		Vector info = new Vector();
		info.add(id);
		info.add(new Integer(1));
		info.add("data");
		info.add("signature");
		dataManager.writeContactInfoToFile(id, info);
		infoFile = dataManager.getContactInfoFile(id);		
		assertTrue("should have a valid file", infoFile.exists());

	}

	
	public void testGetContactInfo() throws Exception
	{
		MockMartusSecurity client = new MockMartusSecurity();
		client.createKeyPair();
		MartusAmplifier.security = client;

		String id = "test";
		String data1 = "data 1";
		String data2 = "data 2";
		String invalidSignature = "invalid sig";
		Vector contactInfo = dataManager.getContactInfo(id);
		assertNull("Data not saved yet should return null", contactInfo);

		Vector original = new Vector();
		original.add(id);
		original.add(new Integer(2));
		original.add(data1);
		original.add(data2);
		original.add(invalidSignature);		
		dataManager.writeContactInfoToFile(id, original);
		contactInfo = dataManager.getContactInfo(id);
		assertNull("contactInfo should be null, invalid signature", contactInfo);

		original.clear();
		String accountId = client.getPublicKeyString();		
		original.add(accountId);
		original.add(new Integer(3));
		original.add(data1);
		original.add("");
		original.add(data2);
		String signature = client.createSignatureOfVectorOfStrings(original);
		original.add(signature);		

		dataManager.writeContactInfoToFile(accountId, original);
		contactInfo = dataManager.getContactInfo(accountId);
		assertNotNull("contactInfo should not be null, valid signature", contactInfo);

		assertEquals(2, contactInfo.size());
		assertEquals(data1, contactInfo.get(0));
		assertEquals(data2, contactInfo.get(1));

		original.clear();
		original.add(accountId);
		original.add(new Integer(10));
		original.add("");
		original.add("");
		original.add("");
		original.add(data1);
		original.add("");
		original.add("");
		original.add("");
		original.add(data2);
		original.add("");
		original.add("");
		signature = client.createSignatureOfVectorOfStrings(original);
		original.add(signature);		

		dataManager.writeContactInfoToFile(accountId, original);
		contactInfo = dataManager.getContactInfo(accountId);
		assertEquals("Empty data elements should be stripped", 2, contactInfo.size());
		assertEquals(data1, contactInfo.get(0));
		assertEquals(data2, contactInfo.get(1));
	}
	
	protected DataManager getAttachmentManager()
	{
		return dataManager;
	}
	private FileSystemDataManager dataManager;

}