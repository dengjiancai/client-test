package com.util.chatone.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author skx<br>
 *         2018年8月13日  下午4:09:20
 */
public class SignUtil {
    
    private static Logger log = LoggerFactory.getLogger(SignUtil.class);
    private static final String SIGN_ALGORITHMS = "SHA1withRSA";// 签名算法
    private static final String AESTYPE = "AES/ECB/PKCS5Padding";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";
    private static final String PUBLIC_KEY = "PUBLIC_KEY";
    private static final String AES_KEY = "AES_KEY";

    /** 存放证书列表 **/
    private static Map<String,Object> keyMap = new HashMap<>();


    
    public void init(){
//        String certKey = signConfig.getCertKey();
//        String alias = signConfig.getAlias();
//        String privateFile = signConfig.getPrivateFile();
//        String publicFile = signConfig.getPublicFile();
//        String storePass = signConfig.getStorePass();
//        String keyPass = signConfig.getKeyPass();
//        String aesKey = signConfig.getAesKey();
        
        String certKey = "erp_fmc";
        String alias = "erp_test";
        String privateFile = "D:/key/fmc/zj/fmc_erp_test.jks";
        String publicFile = "D:/key/fmc/zj/erp_test.cert";
        String storePass = "ss123456";
        String keyPass = "kk123456";
        String aesKey = "Erp_test00000000";
        try {
            //私钥信息
            log.info("初始化客户交互证书：certKey={},alias={},privateFile={},publicFile={}",certKey,alias,privateFile,publicFile);
            KeyStore keyStore = KeyStore.getInstance("JKS");

            InputStream keyStoreInputStream = new FileInputStream(new File(privateFile));
            keyStore.load(keyStoreInputStream, storePass.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());

            //公钥
            InputStream certificateInputStream = new FileInputStream(new File(publicFile));
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(certificateInputStream);
            PublicKey publicKey = certificate.getPublicKey();

            //保存单个客户证书
            keyMap.put(PRIVATE_KEY,privateKey);
            keyMap.put(PUBLIC_KEY,publicKey);
            keyMap.put(AES_KEY,aesKey);
            log.info("初始化客户交互证书成功");
        }catch (Exception e){
            log.error("初始化加/验签证书异常 系统异常退出 -1:{}",e.getMessage(),e);
            System.exit(-1);

        }
    }

    
    /**
     * 通过obj对象进行加签
     * @param obj
     * @return
     * @throws  Exception
     */
    public static String sign(Object obj) throws Exception {
//        init(null);
        return _sign(getSignSrc(obj));
    }

    /**
     * 通过obj对象进行验签
     * @param obj
     * @param sign
     * @return
     * @throws  Exception
     */
    public boolean verifySign( Object obj,String sign) throws Exception {
        return _verifySign(getSignSrc(obj),sign);

    }

    /**
     * 将对象转换成加签串
     * 将除了sign节点以外的所有字段按字段名升序排序后，按顺序取出字段值（为空则用空符串代替）用&连接后拼接成字符串。
     * @param obj
     * @return
     */
    private static String getSignSrc(Object obj) throws Exception{
        Map<String,String> map = new HashMap<>();
        map=getAttribute("", obj);
        List<Map.Entry<String, String>> list = sortKeyAsc(map);
        return coverParamsToString(list);
    }

    /**
     * 找出object中的属性拼接成字符串
     *
     * @param parentAttrName
     * @param obj
     * @return
     * @throws Exception
     */
    private static Map<String,String> getAttribute(String parentAttrName,Object obj) throws Exception {
        Map<String,String> map=new HashMap<>();
        if(null == obj){
            return map;
        }

        //得到类对象
        Class userCla = (Class) obj.getClass();
         /*
        * 得到类中的所有属性集合
        */
        Field[] fs = userCla.getDeclaredFields();
        for(int i = 0 ; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); //设置些属性是可以访问的s
            String type = f.getType().toString();//得到此属性的类型

            //sign不处理
            if("sign".equalsIgnoreCase(f.getName())){
                continue;
            }
            Object o = null;
            try {
                Method m = (Method) obj.getClass().getMethod("get" + getMethodName(f.getName()));
                o = m.invoke(obj);// 调用getter方法获取属性值
            }catch (Exception e){
                //log.error("没有找到属性[{}]对应的get方法，忽略该属性取值。",f.getName());
            }
            if(null != o){
                if (type.endsWith("String")) {
                    map.put(getKeyName(parentAttrName, f.getName()), o.toString());
                } else if (type.endsWith("java.util.List")) {//list
                    if (null != o) {
                        int l = -1;
                        for (Object ch : (List<Object>) o) {
                            l++;
                            map.putAll(getAttribute(getKeyName(parentAttrName, f.getName()) + l, ch));
                        }
                    }
                } else {
                    map.putAll(getAttribute(getKeyName(parentAttrName, f.getName()), o));
                }
            }


        }
        return map;
    }

    /**
     * 做签名
     * @param signSrc
     * @return
     * @throws Exception
     */
    private static String _sign(String signSrc) throws Exception {
        log.info(" 加签参数 : signSrc={}",signSrc);

        Map<String,Object> map = keyMap;
        initTest();
        if(null == map || map.isEmpty()){
            throw new RuntimeException("没有找到证书信息");
        }
        if(StringUtils.isEmpty(signSrc)){
            throw new RuntimeException("加签源串为空！");
        }
        try{
            String sha1Str = encryptWithSha1(signSrc).toLowerCase();
            log.info(" sha1加密加签参数 : " + sha1Str);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign((PrivateKey) map.get(PRIVATE_KEY));

            signature.update(sha1Str.getBytes());
            byte[] signatures = signature.sign();
            String signStr = new HexBinaryAdapter().marshal(signatures);
            log.debug(" 签名摘要明文 : " + signStr);
            String signStrWithAes = encryptWithAes(map.get(AES_KEY).toString(), signatures);
            log.debug(" AES加密后的摘要 : " + signStrWithAes);
            return signStrWithAes;
        }catch (Exception e){
            throw new RuntimeException("签名出错："+e.getMessage(),e);
        }

    }

    public static void initTest(){
        String certKey = "erp_fmc";
        String alias = "erp_test";
        String privateFile = "D:/app/key/erp/fmc_erp_test.jks";
        String publicFile = "D:/app/key/erp/erp_test.cert";
        String storePass = "ss123456";
        String keyPass = "kk123456";
        String aesKey = "Erp_test00000000";
        try {
            //私钥信息
            log.info("初始化客户交互证书：certKey={},alias={},privateFile={},publicFile={}",certKey,alias,privateFile,publicFile);
            KeyStore keyStore = KeyStore.getInstance("JKS");

            InputStream keyStoreInputStream = new FileInputStream(new File(privateFile));
            keyStore.load(keyStoreInputStream, storePass.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());

            //公钥
            InputStream certificateInputStream = new FileInputStream(new File(publicFile));
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(certificateInputStream);
            PublicKey publicKey = certificate.getPublicKey();

            //保存单个客户证书
            keyMap.put(PRIVATE_KEY,privateKey);
            keyMap.put(PUBLIC_KEY,publicKey);
            keyMap.put(AES_KEY,aesKey);
            log.info("初始化客户交互证书成功");
        }catch (Exception e){
            log.error("初始化加/验签证书异常 系统异常退出 -1:{}",e.getMessage(),e);
            System.exit(-1);

        }
    }
    /**
     * 做验签
     * @param signSrc
     * @param sign
     * @return
     * @throws Exception
     */
    private boolean _verifySign(String signSrc, String sign) throws Exception {
        log.debug(" 验签入参 : certKey={},sign={}",signSrc,sign);

        Map<String,Object> map = keyMap;
        if(null == map || map.isEmpty()){
            throw new RuntimeException("没有找到证书信息");
        }
        if(StringUtils.isEmpty(signSrc)){
            throw new RuntimeException("验签源串为空！");
        }
        if(StringUtils.isEmpty(sign)){
            throw new RuntimeException("签名串为空！");
        }
        try{
            String sha1Str = encryptWithSha1(signSrc).toLowerCase();
            log.debug(" sha1加密后的字符串 : " + sha1Str);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify((PublicKey) map.get(PUBLIC_KEY));

            signature.update(sha1Str.getBytes());
            byte[] signStrWithAes = decryptForAes(map.get(AES_KEY).toString(), sign);
            log.debug(" 解密后的摘要 : " + new HexBinaryAdapter().marshal(signStrWithAes));
            boolean verifyFlag = signature.verify(signStrWithAes);
            if (verifyFlag) {
                log.debug(" 验签成功");
                return verifyFlag;
            } else {
                log.warn(" 验签失败: certKey={},sign={}",signSrc,sign);
                return verifyFlag;
            }
        }catch(Exception e){
            throw new RuntimeException("验证签名出错："+e.getMessage(),e);
        }

    }

    private static String encryptWithAes(String keyStr, byte[] plainText) throws Exception {
        Key key = generateKey(keyStr);
        Cipher cipher = Cipher.getInstance(AESTYPE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypt = cipher.doFinal(plainText);
        return new HexBinaryAdapter().marshal(encrypt);
    }

    private static byte[] decryptForAes(String keyStr, String encryptData) throws Exception {
        Key key = generateKey(keyStr);
        Cipher cipher = Cipher.getInstance(AESTYPE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypt = cipher.doFinal(new HexBinaryAdapter().unmarshal(encryptData));
        return decrypt;
    }

    private static Key generateKey(String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        return keySpec;
    }

    private static String encryptWithSha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(data.getBytes());
        return new HexBinaryAdapter().marshal(md.digest());
    }


    private static String getKeyName(String parentAttrName,String attrName){
        if(!StringUtils.isEmpty(parentAttrName)){
            return parentAttrName+"."+attrName;
        }else{
            return attrName;
        }
    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) throws Exception{
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    /**
     * 按key升序排序
     * @param parameters
     * @return
     */
    private static List<Map.Entry<String, String>> sortKeyAsc(Map<String, String> parameters) {
        List<Map.Entry<String, String>> mappingList = new ArrayList<Map.Entry<String, String>>(parameters.entrySet());
        Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
                return mapping1.getKey().compareTo(mapping2.getKey());
            }
        });
        return mappingList;
    }

    private static String coverParamsToString(List<Map.Entry<String, String>> params) {
        StringBuffer signStr = new StringBuffer();
        for (Map.Entry<String, String> entry : params) {
            signStr.append(entry.getValue() == null || "null".equalsIgnoreCase(entry.getValue()) ? "" : entry.getValue());
        }
        return signStr.toString();
    }
    

}
