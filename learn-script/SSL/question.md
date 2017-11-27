### 问题列表

*	SSL握手的流程是什么样？客户端和服务端各进行几次操作？
*	证书中包含的内容是什么？是公钥+签名么？
*	客户端如何验证服务器证书是否有效？如何验证签名为权威签名？
	
	*	客户端持有权威机构的公钥。使用该公钥对服务端证书中的内容（服务器公钥）和签名进行验证。

*	公钥私钥如何进行签名与验证，与加解密有何不同？
	
	*	DSA：数字签名算法 (Digital Signature Algorithm, DSA) 由美国国家安全署 (United States National Security Agency, NSA) 发明，已作为数字签名的标准。在DSA数字签名和认证中，发送者使用自己的私钥对文件或消息进行签名，接受者收到消息后使用发送者的公钥来验证签名的真实性。DSA只是一种算法，和RSA不同之处在于它不能用作加密和解密，也不能进行密钥交换，只用于签名，它比RSA要快很多。

### 参考资料

*	[数字证书基础知识](http://www.enkichen.com/2016/02/26/digital-certificate-based/)
*	[PKI系统与数字证书结构](http://www.enkichen.com/2016/04/12/certification-and-pki/)

