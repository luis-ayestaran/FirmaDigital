package buap.firmaDigital.daos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
//import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Rsa {
	
	private static Cipher rsa;
	
	public void generaLlaves() throws Exception {
        // Generar el par de claves
     KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
     KeyPair keyPair = keyPairGenerator.generateKeyPair();
     PublicKey publicKey = keyPair.getPublic();
     PrivateKey privateKey = keyPair.getPrivate();
       
     // Se salva y recupera de fichero la clave publica
     saveKey(publicKey, "publickey.dat");
     // Se salva y recupera de fichero la clave privada
     saveKey(privateKey, "privatekey.dat");
     
      // byte[] publicKeyBytes = publicKey.getEncoded();
      //  byte[] privateKeyBytes = privateKey.getEncoded();
      // String puK = new String(publicKeyBytes, StandardCharsets.UTF_8);
      // String prK = new String(privateKeyBytes, StandardCharsets.UTF_8);

     //String[] llaves = { puK, prK };
     //return llaves;
   } 
	    
	//Función HASH recibimos el mensaje, devolvemos el resumen
	
	public byte[] hasheador(String tc) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
	      md.update(tc.getBytes());
	      byte[] digest = md.digest(); //Mandamos el digest con algunos caracteres no visibles 
		return digest;
	}
	    
	//Firma digital Ciframos con el resumen y la Clave privada del emisor y se devuelve la Firma del emisor
	    public byte[] enfirma(byte[] Resumen, PrivateKey privateKey) throws Exception {
	    	// PrivateKey privateKey;
	    	 // Obtener la clase para encriptar/desencriptar
	        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        // Texto a encriptar Resumen
	        //privateKey = loadPrivateKey("privatekey.dat");
	        // Se encripta
	        rsa.init(Cipher.ENCRYPT_MODE, privateKey);
	        // cambiamos a privada 
	        byte[] encriptado = rsa.doFinal(Resumen);
	     
			return encriptado;
	    }
	    
	    public byte[] desFirma(byte[] firma, PublicKey publicKey ) throws NoSuchAlgorithmException, NoSuchPaddingException,IllegalBlockSizeException, BadPaddingException, InvalidKeyException, FileNotFoundException,IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    	//PublicKey publicKey;
	    	//publicKey = loadPublicKey("publickey.dat");
	    	 rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	//se pasa string a bytes
	    	//System.out.println(firm.length);
	    	// Se desencripta
	        rsa.init(Cipher.DECRYPT_MODE, publicKey);
	        byte[] newResumen = rsa.doFinal(firma);
	        //String newResumen = new String(bytesDesencriptados);
	    	return newResumen;
	    }
	    
	    public byte[] encriptaMensaje(String mensaje, PublicKey publicKey) throws Exception {
	    	 //PrivateKey privateKey;
	    	 // Obtener la clase para encriptar/desencriptar
	        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        // Texto a encriptar Resumen
	       // privateKey = loadPrivateKey("privatekey.dat");
	        // Se encripta
	        rsa.init(Cipher.ENCRYPT_MODE, publicKey);
	        // cambiamos a privada 
	        byte[] encriptado = rsa.doFinal(mensaje.getBytes());
	     
			return encriptado;
	    }
	    
	    
	    public String desencriptaMensaje(byte[] mensajeEncriptado, PublicKey publicKey ) throws NoSuchAlgorithmException, NoSuchPaddingException,IllegalBlockSizeException, BadPaddingException, InvalidKeyException, FileNotFoundException,IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    	//PublicKey publicKey;
	    	//publicKey = loadPublicKey("publickey.dat");
	    	 rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	//se pasa string a bytes
	    	//System.out.println(firm.length);
	    	// Se desencripta
	        rsa.init(Cipher.DECRYPT_MODE, publicKey);
	        byte[] newMensaje = rsa.doFinal(mensajeEncriptado);
	        String mensajeDesen = new String(newMensaje);
	    	return mensajeDesen;
	    }
	    
	   
	    private static void saveKey(Key key, String fileName) throws Exception {
	        byte[] publicKeyBytes = key.getEncoded();
	        FileOutputStream fos = new FileOutputStream(fileName);
	        fos.write(publicKeyBytes);
	        fos.close();
	     }
	    public static PublicKey loadPublicKey(String fileName) throws FileNotFoundException,IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	        FileInputStream fis = new FileInputStream(fileName);
	        int numBtyes = fis.available();
	        byte[] bytes = new byte[numBtyes];
	        fis.read(bytes);
	        fis.close();

	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        KeySpec keySpec = new X509EncodedKeySpec(bytes);
	        PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
	        return keyFromBytes;
	     }
	    public static PrivateKey loadPrivateKey(String fileName) throws Exception {
	        FileInputStream fis = new FileInputStream(fileName);
	        int numBtyes = fis.available();
	        byte[] bytes = new byte[numBtyes];
	        fis.read(bytes);
	        fis.close();

	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
	        PrivateKey keyFromBytes = keyFactory.generatePrivate(keySpec);
	        return keyFromBytes;
	     }
	    public String byteToString(byte[] arrayByte) {
	    	StringBuilder sb = new StringBuilder();
	    	for (byte b : arrayByte) {
	    		sb.append(Integer.toHexString(0xFF & b));
		      }
	    	return new String(sb);
	    }
	    
	
	public static void main(String[] args) throws Exception {
        Rsa rs = new Rsa();
        
		rs.generaLlaves();
		PrivateKey privateKeyA = loadPrivateKey("privatekey.dat");
		PublicKey publicKeyA = loadPublicKey("publickey.dat");
		
		byte[] privateKeyABytes = privateKeyA.getEncoded();
		byte[] publicKeyABytes = publicKeyA.getEncoded();
		
		String privateA = rs.byteToString(privateKeyABytes);
		String publicA = rs.byteToString(publicKeyABytes);
		
		System.out.println("Privada Beto");
		System.out.println(privateA);
		System.out.println("Publica Beto");
		System.out.println(publicA + "\n");
		
		rs.generaLlaves();
		PrivateKey privateKeyB = loadPrivateKey("privatekey.dat");
		PublicKey publicKeyB = loadPublicKey("publickey.dat");
		
		byte[] privateKeyBBytes = privateKeyB.getEncoded();
		byte[] publicKeyBBytes = publicKeyB.getEncoded();
		
		String privateB = rs.byteToString(privateKeyBBytes);
		String publicB = rs.byteToString(publicKeyBBytes);
		
		System.out.println("Privada Alicia");
		System.out.println(privateB);
		System.out.println("Publica Alicia");
		System.out.println(publicB + "\n");
		
		byte[] hash = rs.hasheador("Hola perrito");
		String mensaje = rs.byteToString(hash);
		System.out.println("El hash");
		System.out.println(mensaje);
		
		byte[] Elmensaje = rs.encriptaMensaje("Hola perrito", publicKeyB );
		String Lemensaje = rs.byteToString(Elmensaje);
		System.out.println("El mensaje Encriptado");
		System.out.println(Lemensaje);
		
		byte[] firma = rs.enfirma(hash, privateKeyA);
		String firmastring = rs.byteToString(firma);
		System.out.println("La firma");
		System.out.println(firmastring);
		byte[] resumenRecibido = rs.desFirma(firma,publicKeyA);
		
		
		String mensajeDesen  = rs.desencriptaMensaje(Elmensaje,publicKeyA);
		System.out.println("El mensaje desencriptado");
		System.out.println(mensajeDesen);
		
		
		String resumenRecibidostring = rs.byteToString(resumenRecibido);
		System.out.println("El nuevo resumen");
		System.out.println(resumenRecibidostring);
		if(mensaje.equals(resumenRecibidostring) )
			System.out.println("es igual");
		else System.out.println("no es igual");        
    } 
	
	 
}