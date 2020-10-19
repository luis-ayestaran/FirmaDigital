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

public class Rsa {
	
	public String[] GeneraLlaves() throws Exception {
        // Generar el par de claves
     KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
     KeyPair keyPair = keyPairGenerator.generateKeyPair();
     PublicKey publicKey = keyPair.getPublic();
     PrivateKey privateKey = keyPair.getPrivate();
       
       byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();
       String puK = new String(publicKeyBytes, StandardCharsets.UTF_8);
       String prK = new String(privateKeyBytes, StandardCharsets.UTF_8);

     String[] llaves = { puK, prK };
     return llaves;
   } 
	    
	//Función HASH recibimos el mensaje, devolvemos el resumen
	
	public void hasheador(String tc) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
	      md.update(tc.getBytes());
	      byte[] digest = md.digest();

	      // Se escribe byte a byte en hexadecimal
	      System.out.println("Hexadecimal");
	      for (byte b : digest) {
	         System.out.print(Integer.toHexString(0xFF & b));
	      }
	      System.out.println();

	      // Se escribe codificado base 64. Se necesita la librería
	      // commons-codec-x.x.x.jar de Apache
	      byte[] encoded = Base64.encodeBase64(digest);
	      System.out.println("Codificado");
	      System.out.println(new String(encoded));
		
	}
	    
	//Firma digital Ciframos con el resumen y la Clave privada del emisor y se devuelve la Firma del emisor
	    
	    
	public static void main(String[] args) throws Exception {
        Rsa rsa = new Rsa();
      //  String[] llaves = rsa.GeneraLlaves();
    

       /* System.out.println("Publica: ");
        System.out.println(llaves[0]);
        
        System.out.println("Privada: ");
        System.out.println(llaves[1]); */
        
        rsa.hasheador("Hola que tal?");
        
    }
	
	 
}
