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
	
	//private
	
	
	public String[] GeneraLlaves() throws Exception {
        // Generar el par de claves
     KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
     KeyPair keyPair = keyPairGenerator.generateKeyPair();
     PublicKey publicKey = keyPair.getPublic();
     PrivateKey privateKey = keyPair.getPrivate();
       
     // Se salva y recupera de fichero la clave publica
     saveKey(publicKey, "publickey.dat");
     // Se salva y recupera de fichero la clave privada
     saveKey(privateKey, "privatekey.dat");
     
       byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();
       String puK = new String(publicKeyBytes, StandardCharsets.UTF_8);
       String prK = new String(privateKeyBytes, StandardCharsets.UTF_8);

     String[] llaves = { puK, prK };
     return llaves;
   } 
	    
	//Función HASH recibimos el mensaje, devolvemos el resumen
	
	public byte[] hasheador(String tc) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
	      md.update(tc.getBytes());
	      byte[] digest = md.digest(); //Mandamos el digest con algunos caracteres no visibles 
		return digest;
	}
	    
	//Firma digital Ciframos con el resumen y la Clave privada del emisor y se devuelve la Firma del emisor
	    public byte[] enfirma(byte[] Resumen) throws Exception {
	    	 PrivateKey privateKey;
	    	 // Obtener la clase para encriptar/desencriptar
	        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        // Texto a encriptar Resumen
	        privateKey = loadPrivateKey("privatekey.dat");
	        // Se encripta
	        rsa.init(Cipher.ENCRYPT_MODE, privateKey);
	        // cambiamos a privada 
	        byte[] encriptado = rsa.doFinal(Resumen);
	     
			return encriptado;
	    }
	    
	    public byte[] desFirma(byte[] firma ) throws NoSuchAlgorithmException, NoSuchPaddingException,IllegalBlockSizeException, BadPaddingException, InvalidKeyException, FileNotFoundException,IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    	PublicKey publicKey;
	    	publicKey = loadPublicKey("publickey.dat");
	    	 rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	//se pasa string a bytes
	    	//System.out.println(firm.length);
	    	// Se desencripta
	        rsa.init(Cipher.DECRYPT_MODE, publicKey);
	        byte[] newResumen = rsa.doFinal(firma);
	        //String newResumen = new String(bytesDesencriptados);
	    	return newResumen;
	    }
	    
	    public byte[] encriptaMensaje(String mensaje) throws Exception {
	    	 PrivateKey privateKey;
	    	 // Obtener la clase para encriptar/desencriptar
	        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        // Texto a encriptar Resumen
	        privateKey = loadPrivateKey("privatekey.dat");
	        // Se encripta
	        rsa.init(Cipher.ENCRYPT_MODE, privateKey);
	        // cambiamos a privada 
	        byte[] encriptado = rsa.doFinal(mensaje.getBytes());
	     
			return encriptado;
	    }
	    
	    
	    public String desencriptaMensaje(byte[] mensajeEncriptado ) throws NoSuchAlgorithmException, NoSuchPaddingException,IllegalBlockSizeException, BadPaddingException, InvalidKeyException, FileNotFoundException,IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    	PublicKey publicKey;
	    	publicKey = loadPublicKey("publickey.dat");
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
	    private static PublicKey loadPublicKey(String fileName) throws FileNotFoundException,IOException, NoSuchAlgorithmException, InvalidKeySpecException {
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
	    private static PrivateKey loadPrivateKey(String fileName) throws Exception {
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
	    public String stringtobyte(byte[] arrayByte) {
	    	StringBuilder sb = new StringBuilder();
	    	for (byte b : arrayByte) {
	    		sb.append(Integer.toHexString(0xFF & b));
		      }
	    	return new String(sb);
	    }
	    
	
	 public static void main(String[] args) throws Exception {
        Rsa rs = new Rsa();
        
		String[] llaves = rs.GeneraLlaves();
		byte[] hash = rs.hasheador("Hola perrito");
		byte[] Elmensaje = rs.encriptaMensaje("Hola perrito");
		String Lemensaje = rs.stringtobyte(Elmensaje);
		System.out.println("El mensaje Encriptado");
		System.out.println(Lemensaje);
		String mensaje = rs.stringtobyte(hash);
		System.out.println("El hash");
		System.out.println(mensaje);
		
		byte[] firma = rs.enfirma(hash);
		String firmastring = rs.stringtobyte(firma);
		System.out.println("La firma");
		System.out.println(firmastring);
		byte[] resumenRecibido = rs.desFirma(firma);
		
		
		String mensajeDesen  = rs.desencriptaMensaje(Elmensaje);
		System.out.println("El mensaje desencriptado");
		System.out.println(mensajeDesen);
		
		
		String resumenRecibidostring = rs.stringtobyte(resumenRecibido);
		System.out.println("El nuevo resumen");
		System.out.println(resumenRecibidostring);
		if(mensaje.equals(resumenRecibidostring) )
			System.out.println("es igual");
		else System.out.println("no es igual");        
    }
	
	 
}