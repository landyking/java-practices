package com.github.landyking.learnShiro.kaitao5;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/9/21 13:56
 * note:
 */
public class KaitaoTest5 {
    @Test
    public void test11() throws Exception {
        String str = "hello";
        String base64Encoded = Base64.encodeToString(str.getBytes());
        System.out.println(base64Encoded);
        String str2 = Base64.decodeToString(base64Encoded);
        System.out.println(str2);
        Assert.assertEquals(str, str2);

    }

    @Test
    public void test222() throws Exception {
        String str = "hello";
        String base64Encoded = Hex.encodeToString(str.getBytes());
        System.out.println(base64Encoded);
        String str2 = new String(Hex.decode(base64Encoded.getBytes()));
        System.out.println(str2);
        Assert.assertEquals(str, str2);
    }

    @Test
    public void test33() throws Exception {
        String str = "hello";
        String salt = "123";
        String md5 = new Md5Hash(str, salt).toString();//还可以转换为 toBase64()/toHex()
        System.out.println(md5);
    }

    @Test
    public void test44() throws Exception {
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha256Hash(str, salt).toString();
        System.out.println(sha1);
    }

    @Test
    public void test5() throws Exception {
        String str = "hello";
        String salt = "123";
//内部使用MessageDigest
        String simpleHash = new SimpleHash("SHA-1", str, salt).toString();
        System.out.println(simpleHash);
    }

    @Test
    public void test6() throws Exception {
        DefaultHashService hashService = new DefaultHashService(); //默认算法SHA-512
        hashService.setHashAlgorithmName("SHA-512");
        hashService.setPrivateSalt(new SimpleByteSource("123")); //私盐，默认无
        hashService.setGeneratePublicSalt(true);//是否生成公盐，默认false
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());//用于生成公盐。默认就这个
        hashService.setHashIterations(1); //生成Hash值的迭代次数

        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello"))
                .setSalt(ByteSource.Util.bytes("123")).setIterations(2).build();
        String hex = hashService.computeHash(request).toHex();
        System.out.println(hex);
    }

    @Test
    public void test7() throws Exception {
        SecureRandomNumberGenerator randomNumberGenerator =
                new SecureRandomNumberGenerator();
        randomNumberGenerator.setSeed("123".getBytes());
        String hex = randomNumberGenerator.nextBytes().toHex();
        System.out.println(hex);
    }

    @Test
    public void test8() throws Exception {
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); //设置key长度
//生成key
        Key key = aesCipherService.generateNewKey();
        System.out.println("key:" + key);
        String text = "hello";
//加密
        String encrptText =
                aesCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
//解密
        System.out.println("encryptText:" + encrptText);
        String text2 =
                new String(aesCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());
        System.out.println("text2:" + text2);
        Assert.assertEquals(text, text2);

    }

    @Test
    public void test9() throws Exception {
        String algorithmName = "md5";
        String username = "liu";
        String password = "123";
        String salt1 = username;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;

        SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
        String encodedPassword = hash.toHex();
        System.out.println(encodedPassword);

    }
}
