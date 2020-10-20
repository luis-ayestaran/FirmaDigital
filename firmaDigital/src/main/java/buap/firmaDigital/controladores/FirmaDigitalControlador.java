package buap.firmaDigital.controladores;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import buap.firmaDigital.vistas.Dialogs;
import buap.firmaDigital.vistas.FirmaDigital;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
	public TextField txtLlavePublicaBeto;
	@FXML
	public TextField txtLlavePrivadaBeto;
	@FXML
	public TextField txtLlavePublicaAliciaCopia;
	
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
	
	// ---------------------- INICIALIZACIÓN ----------------------- //
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		
		
	}
	
	
	@FXML
	protected void generarLlaves() {
		
		efectosGenerarLlaves();
		
	}
	
	private void efectosGenerarLlaves() {
		
		btnGenerarLlaves.setDisable(true);
		
		lblMensajeClaro.setDisable(false);
		txtMensajeClaro.setDisable(false);
		txtMensajeClaro.setPromptText("Escribe tu mensaje");
		txtMensajeClaro.requestFocus();
		btnHash.setDisable(false);
		
		GaussianBlur blur = new GaussianBlur();
		panelAlicia.setEffect(blur);
		
	}
	
	@FXML
	protected void calcularHash() {
		
		if( !txtMensajeClaro.getText().isEmpty() ) {
			efectosCalcularHash();
		}
		
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
		
		efectosFirmarMensaje();
		
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
		
		Random generadorRandom = new Random();
		if(generadorRandom.nextBoolean()) {
			interceptarMensaje();
		}
		efectosEnviarMensaje();
		
	}
	
	private void interceptarMensaje() {
		Dialogs.inputDialog(
				FirmaDigital.getStage(),
				"Mensaje interceptado",
				"Interceptaste un mensaje",
				"El contenido del mensaje es el siguiente:",
				txtMensajeFirmado.getText()
		);
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
		
		if( txtMensajeClaro.getText().equals( txtMensajeAceptado.getText() ) ) {
			Dialogs.acceptDialog("Mensaje Aceptado",
					"Excelente, el mensaje llegó sin modificaciones.",
					(StackPane) FirmaDigital.getStage().getScene().getRoot(), null, false);
		} else {
			Dialogs.acceptDialog("Mensaje No Aceptado",
					"Cuidado, parece que los mensajes no coinciden.",
					(StackPane) FirmaDigital.getStage().getScene().getRoot(), null, true);
		}
		
		btnGenerarLlaves.setDisable(false);
		btnGenerarLlaves.setText("Generar nuevas llaves");
		
	}
	
}
