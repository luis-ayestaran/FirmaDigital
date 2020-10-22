package buap.firmaDigital.controladores;

import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import buap.firmaDigital.daos.Rsa;
import buap.firmaDigital.vistas.Dialogs;
import buap.firmaDigital.vistas.FirmaDigital;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class FirmaDigitalControlador implements Initializable {

	@FXML
	public JFXButton btnGenerarLlaves;
	
	// ----------------------- Variables Beto----------------------------- //
	
	@FXML
	public BorderPane panelBeto;
	
	@FXML
	public TextArea txtLlavePublicaBeto;
	@FXML
	public TextArea txtLlavePrivadaBeto;
	@FXML
	public TextArea txtLlavePublicaAliciaCopia;
	
	@FXML
	public Label lblMensajeClaro;
	@FXML
	public TextField txtMensajeClaro;
	@FXML
	public JFXButton btnHash;
	
	@FXML
	public Label lblResumen;
	@FXML
	public TextField txtResumen;
	@FXML
	public JFXButton btnFirmar;
	
	@FXML
	public Label lblMensajeFirmado;
	@FXML
	public TextField txtMensajeFirmado;
	@FXML
	public JFXButton btnEnviar;
	
	// ----------------------- Variables Alicia ---------------------------- //
	
	@FXML
	public BorderPane panelAlicia;
	
	@FXML
	public TextArea txtLlavePublicaAlicia;
	@FXML
	public TextArea txtLlavePrivadaAlicia;
	@FXML
	public TextArea txtLlavePublicaBetoCopia;
	
	@FXML
	public Label lblMensajeRecibido;
	@FXML
	public TextField txtMensajeRecibido;
	@FXML
	public JFXButton btnDescifrar;
	
	@FXML
	public Label lblFirmaDescifrada;
	@FXML
	public TextField txtFirmaDescrifrada;
	
	@FXML
	public Label lblMensajeAceptado;
	@FXML
	public TextField txtMensajeAceptado;
	
	private Rsa cifrador;
	
	private PrivateKey privateKeyBeto;
	private PublicKey publicKeyBeto;
	private PrivateKey privateKeyAlicia;
	private PublicKey publicKeyAlicia;
	
	private String llavePublicaBeto;
	private String llavePrivadaBeto;
	private String llavePublicaAlicia;
	private String llavePrivadaAlicia;
	private String mensajeClaro;
	private String resumen;
	private String firmaBeto;
	private String mensajeCifradoEnviado;
	private String mensajeCifradoRecibido;
	
	
	private byte[] hash;
	private byte[] firma;
	
	
	// ---------------------- INICIALIZACIÓN ----------------------- //
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		cifrador = new Rsa();
		
	}
	
	//PASO 1: generación del par de llaves de Beto y el par de llaves de Alicia
	@FXML
	protected void generarLlaves() {
		
		try {
			
			calculaLlaves();
			efectosGenerarLlaves();
			
		} catch (Exception e) {
			Dialogs.acceptDialog("Error al generar llaves",
					"Hubo un error al generar las llaves. Vuelve a intentarlo.",
					(StackPane) FirmaDigital.getStage().getScene().getRoot(), null, true);
		}
		
	}
	
	//Método que genera las llaves pública y privada de Beto y Alicia
	private void calculaLlaves() throws Exception {
			
		//GENERANDO LLAVES DE BETO
		cifrador.generaLlaves();
		privateKeyBeto = Rsa.loadPrivateKey("privatekey.dat");	//Llave privada de Beto
		publicKeyBeto = Rsa.loadPublicKey("publickey.dat");		//Llave publica de Beto
		
		byte[] privateKeyBetoBytes = privateKeyBeto.getEncoded();	//Se transforma de un objeto llave a la codificacion primitiva
		byte[] publicKeyBetoBytes = publicKeyBeto.getEncoded();		
		
		llavePrivadaBeto = cifrador.byteToString(privateKeyBetoBytes);
		llavePublicaBeto = cifrador.byteToString(publicKeyBetoBytes);
		
		System.out.println("Llave privada de Beto\t\t" + llavePrivadaBeto);
		System.out.println("Llave pública de Beto\t\t" + llavePublicaBeto + "\n");
		
		//GENERANDO LLAVES DE ALICIA
		cifrador.generaLlaves();
		privateKeyAlicia = Rsa.loadPrivateKey("privatekey.dat");	//Llave privada de Alicia
		publicKeyAlicia = Rsa.loadPublicKey("publickey.dat");		//Llave publica de Alicia
		
		byte[] privateKeyAliciaBytes = privateKeyAlicia.getEncoded();
		byte[] publicKeyAliciaBytes = publicKeyAlicia.getEncoded();
		
		llavePrivadaAlicia = cifrador.byteToString(privateKeyAliciaBytes);
		llavePublicaAlicia = cifrador.byteToString(publicKeyAliciaBytes);
		
		System.out.println("Llave privada de Alicia\t\t" + llavePrivadaAlicia);
		System.out.println("Llave pública de Alicia\t\t" + llavePublicaAlicia + "\n");
		
	}
	
	private void efectosGenerarLlaves() {
		
		txtLlavePublicaBeto.setText(llavePublicaBeto);
		txtLlavePrivadaBeto.setText(llavePrivadaBeto);
		txtLlavePublicaAliciaCopia.setText(llavePublicaAlicia);
		txtLlavePublicaAlicia.setText(llavePublicaAlicia);
		txtLlavePrivadaAlicia.setText(llavePrivadaAlicia);
		txtLlavePublicaBetoCopia.setText(llavePublicaBeto);
		
		btnGenerarLlaves.setDisable(true);
		
		lblMensajeClaro.setDisable(false);
		txtMensajeClaro.setDisable(false);
		txtMensajeClaro.setPromptText("Escribe tu mensaje");
		txtMensajeClaro.requestFocus();
		btnHash.setDisable(false);
		
		GaussianBlur blur = new GaussianBlur();
		panelAlicia.setEffect(blur);
		
	}
	
	
	//PASO 2: Calcular el hash del mensaje que escribio Beto
	@FXML
	protected void calcularHash() {
		try {
			if( !txtMensajeClaro.getText().trim().isEmpty() ) {
				aplicaHash();
				efectosCalcularHash();
			} else {
				txtMensajeClaro.requestFocus();
			}
		} catch( Exception e ) {
			Dialogs.acceptDialog("Error al calcular hash",
					"Hubo un error al calcular el hash del mensaje. Vuelve a intentarlo.",
					(StackPane) FirmaDigital.getStage().getScene().getRoot(), null, true);
		}
		
	}
	
	private void aplicaHash() throws Exception {
		
		mensajeClaro = txtMensajeClaro.getText().trim();
		hash = cifrador.hasheador( mensajeClaro );
		resumen = cifrador.byteToString(hash).trim();
		System.out.println("Resumen del mensaje\t\t" + resumen + "\n");
		txtResumen.setText(resumen);
		
	}
	
	private void efectosCalcularHash() {
		
		lblMensajeClaro.setDisable(true);
		txtMensajeClaro.setDisable(true);
		btnHash.setDisable(true);
		
		lblResumen.setDisable(false);
		txtResumen.setDisable(false);
		txtResumen.requestFocus();
		btnFirmar.setDisable(false);
		
	}
	
	@FXML
	protected void firmarMensaje() {
		
		try {
			
			aplicaFirma();
			cifraMensaje();
			efectosFirmarMensaje();
			
		} catch( Exception e ) {
			Dialogs.acceptDialog("Error al firmar el mensaje",
					"Hubo un error al firmar el mensaje. Vuelve a intentarlo.",
					(StackPane) FirmaDigital.getStage().getScene().getRoot(), null, true);
			e.printStackTrace();
		}
		
	}
	
	private void aplicaFirma() throws Exception {
		
		firma = cifrador.enfirma(hash, privateKeyBeto);
		firmaBeto = cifrador.byteToString(firma).trim();
		txtMensajeFirmado.setText(firmaBeto);
		System.out.println("Firma de Beto\t\t\t" + firmaBeto + "\n");
		
	}
	
	private void cifraMensaje() throws Exception {
		
		byte[] mensaje = cifrador.encriptaMensaje(mensajeClaro, publicKeyAlicia );
		mensajeCifradoEnviado = cifrador.byteToString(mensaje);
		txtMensajeFirmado.appendText("," + mensajeCifradoEnviado);
		System.out.println("Mensaje cifrado enviado\t\t" + mensajeCifradoEnviado + "\n");
		
	}
	
	private void efectosFirmarMensaje() {
		
		lblResumen.setDisable(true);
		txtResumen.setDisable(true);
		btnFirmar.setDisable(true);
		
		lblMensajeFirmado.setDisable(false);
		txtMensajeFirmado.setDisable(false);
		txtMensajeFirmado.requestFocus();
		btnEnviar.setDisable(false);
		
	}
	
	@FXML
	protected void enviarMensaje() {
		
		System.out.println("\n\n------------------ BETO ENVÍA MENSAJE A ALICIA ---------------\n");
		
		enviar(firmaBeto, mensajeCifradoEnviado);
		recibir(firmaBeto, mensajeCifradoRecibido);
		efectosEnviarMensaje();
		
	}
	
	private void enviar(String firmaEmisor, String mensajeCifrado) {
		
		Random generadorRandom = new Random();
		if(generadorRandom.nextBoolean()) {
			
			System.out.println("\n El mensaje fue interceptado por un hacker \n");
			String mensajeInterceptado = interceptarMensaje();
			
			if(	mensajeInterceptado != null ) {
				
				mensajeCifradoRecibido = mensajeInterceptado;
				
			} else {
				
				mensajeCifradoRecibido = mensajeCifradoEnviado;
				
			}
			
		} else {
			
			mensajeCifradoRecibido = mensajeCifradoEnviado;
			
		}
		
	}
	
	private String interceptarMensaje() {
		
		return Dialogs.inputDialog (
			FirmaDigital.getStage(),
			"Mensaje interceptado",
			"Interceptaste un mensaje",
			"El contenido del mensaje es el siguiente:",
			mensajeCifradoEnviado
		);
		
	}
	
	private void recibir(String firmaEmisor, String mensajeCifrado) {
		txtMensajeRecibido.setText(firmaEmisor + "," + mensajeCifrado);
		System.out.println("Firma recibida\t\t" + firmaBeto + "\n");
		System.out.println("Mensaje cifrado recibido\t\t" + mensajeCifradoRecibido + "\n");
		
	}
	
	private void efectosEnviarMensaje() {
		
		lblMensajeFirmado.setDisable(true);
		txtMensajeFirmado.setDisable(true);
		btnEnviar.setDisable(true);
		
		lblMensajeRecibido.setDisable(false);
		txtMensajeRecibido.setDisable(false);
		txtMensajeRecibido.requestFocus();
		btnDescifrar.setDisable(false);
		
		panelAlicia.setEffect(null);
		GaussianBlur blur = new GaussianBlur();
		panelBeto.setEffect(blur);
		
	}
	
	@FXML
	protected void descifrarMensaje() {
		
		efectosDescifrarMensaje();
		
	}
	
	private void efectosDescifrarMensaje() {
		
		lblMensajeRecibido.setDisable(true);
		txtMensajeRecibido.setDisable(true);
		btnDescifrar.setDisable(true);
		
		panelBeto.setEffect(null);
		
		if( txtMensajeClaro.getText().trim().equals( txtMensajeAceptado.getText().trim() ) ) {
			Dialogs.acceptDialog("Mensaje aceptado",
					"Excelente, el mensaje llegó sin modificaciones.",
					(StackPane) FirmaDigital.getStage().getScene().getRoot(), null, false);
		} else {
			Dialogs.acceptDialog("Mensaje no aceptado",
					"Cuidado, parece que los mensajes no coinciden.",
					(StackPane) FirmaDigital.getStage().getScene().getRoot(), null, true);
		}
		
		btnGenerarLlaves.setDisable(false);
		btnGenerarLlaves.setText("Generar nuevas llaves");
		
	}
	
}
