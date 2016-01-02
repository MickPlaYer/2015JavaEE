package converter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Converter
{
	public static String convert(String original) throws NoSuchAlgorithmException
	{
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(original.getBytes());
		byte[] digest = messageDigest.digest();
		StringBuffer stringBuffer = new StringBuffer();
		for (byte b : digest)
		{
			stringBuffer.append(String.format("%02x", b & 0xff));
		}
		return stringBuffer.toString();
	}
}
