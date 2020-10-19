package buap.firmaDigital.daos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;

public class Rsa {
	
	private static Cipher rsa;
	
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
	
	public String hasheador(String tc) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
	      md.update(tc.getBytes());
	      byte[] digest = md.digest();

	      /* Se escribe byte a byte en hexadecimal
	      System.out.println("Hexadecimal");
	      for (byte b : digest) {
	         System.out.print(Integer.toHexString(0xFF & b));
	      }
	      System.out.println();*/

	      // Se escribe codificado base 64. Se necesita la librería
	      // commons-codec-x.x.x.jar de Apache
	      byte[] encoded = Base64.encodeBase64(digest);
	     /* System.out.println("Codificado");
	      System.out.println(new String(encoded));*/
	      String Resumen = new String(encoded, StandardCharsets.UTF_8);
		return Resumen;
	}
	    
	//Firma digital Ciframos con el resumen y la Clave privada del emisor y se devuelve la Firma del emisor
	    public String enfirma(String Resumen) throws Exception {
	    	 PrivateKey privateKey;
	    	 // Obtener la clase para encriptar/desencriptar
	        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");

	        // Texto a encriptar Resumen
	        privateKey = loadPrivateKey("privatekey.dat");

	        // Se encripta
	        rsa.init(Cipher.ENCRYPT_MODE, privateKey);
	        // cambiamos a privada 
	        byte[] encriptado = rsa.doFinal(Resumen.getBytes());
	        byte[] encoded = Base64.encodeBase64(encriptado);
	        String firma = new String(encoded, StandardCharsets.UTF_8);
			return firma;
	    }
	    public String desFirma(String firma )throws Exception {
	    	PublicKey publicKey;
	    	publicKey = loadPublicKey("publickey.dat");
	    	 rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	//se pasa string a bytes
	    	byte[] firm = firma.getBytes();
	    	// Se desencripta
	        rsa.init(Cipher.DECRYPT_MODE, publicKey);
	        byte[] bytesDesencriptados = rsa.doFinal(firm);
	        String newResumen = new String(bytesDesencriptados);
	    	return newResumen;
	    }
	    private static void saveKey(Key key, String fileName) throws Exception {
	        byte[] publicKeyBytes = key.getEncoded();
	        FileOutputStream fos = new FileOutputStream(fileName);
	        fos.write(publicKeyBytes);
	        fos.close();
	     }
	    private static PublicKey loadPublicKey(String fileName) throws Exception {
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

	    
	public static void main(String[] args) throws Exception {
        Rsa rs = new Rsa();
      //  String[] llaves = rsa.GeneraLlaves();
    

       /* System.out.println("Publica: ");
        System.out.println(llaves[0]);
        
        System.out.println("Privada: ");
        System.out.println(llaves[1]); 
        
        String mensaje = rsa.hasheador("Hola que tal?");
        System.out.println(mensaje);
        */
        String[] llaves = rs.GeneraLlaves();
        String mensaje = rs.hasheador("Hola que tal?");
        System.out.println("El hash");
        System.out.println(mensaje);
        String firma = rs.enfirma(mensaje);
        System.out.println("la vieja firma");
        System.out.println(firma);
        
        String newResumen = rs.desFirma(firma);
        System.out.println("la el nuevo hash");
        System.out.println(newResumen);
        
        if(mensaje == newResumen) {
        	System.out.println("Sí son iguales we");
        }
        
        
        
    }
	
	 
}
