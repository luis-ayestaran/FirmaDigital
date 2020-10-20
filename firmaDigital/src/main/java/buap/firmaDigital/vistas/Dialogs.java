package buap.firmaDigital.vistas;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Dialogs{
	
	public static JFXDialog acceptDialog (String heading, String body, StackPane stackPane, Node focusable, boolean error) {
		
		boolean openedDialog = false;
		
		for(Node node : stackPane.getChildren()) {
			if(node.getClass().getSimpleName().toLowerCase().contains("dialog")) {
				openedDialog = true;
			}
		}
		
		if(!openedDialog) {
			
			JFXDialogLayout content = new JFXDialogLayout();
			Text head = new Text(heading);
			if(error) {
				head.setStyle("-fx-font-family: \"SF Pro Display Bold\";"
						+ "-fx-font-weight: normal;"
						+ "-fx-font-size: 1.5em;"
						+ "-fx-fill: -fx-danger;");
			} else {
				head.setStyle("-fx-font-family: \"SF Pro Display Bold\";"
						+ "-fx-font-weight: normal;"
						+ "-fx-font-size: 1.5em;"
						+ "-fx-fill: -fx-primary;");
			}
			content.setHeading(head);
			Text bodyText = new Text(body);
			bodyText.setStyle("-fx-font-family: \"SF Pro Display Medium\";"
					+ "-fx-font-size: 1.3em;"
					+ "-fx-text-fill: -fx-dark;");
			content.setBody(bodyText);
			JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.BOTTOM);
			JFXButton button = new JFXButton("ACEPTAR");
			if(error) {
				button.setStyle("-fx-text-fill: -fx-danger;"
						+ "-fx-font-family: \"SF Pro Display Bold\";"
						+ "-fx-padding: 0.5em 0.8em;"
						+ "-fx-font-size: 1.25em;"
						+ "-fx-background-color: transparent;"
						+ "-fx-background-radius: 2em;"
						+ "-fx-effect: none;"
						+ "-fx-focus-traversable: true;");
			} else {
				button.setStyle("-fx-text-fill: -fx-primary;"
						+ "-fx-font-family: \"SF Pro Display Bold\";"
						+ "-fx-padding: 0.5em 0.8em;"
						+ "-fx-font-size: 1.25em;"
						+ "-fx-background-color: transparent;"
						+ "-fx-background-radius: 2em;"
						+ "-fx-effect: none;"
						+ "-fx-focus-traversable: true;");
			}
			button.setOnAction(new EventHandler<ActionEvent>(){
			    @Override
			    public void handle(ActionEvent event){
			    	stackPane.getChildren().get(0).setEffect(null);
			    	stackPane.getChildren().get(0).setDisable(false);
			        dialog.close();
			        if(focusable != null) {
			        	focusable.requestFocus();
			        }
			    }
			});
			button.setOnKeyPressed(e -> {
				if(e.getCode().equals(KeyCode.ENTER)) {
					stackPane.getChildren().get(0).setEffect(null);
					stackPane.getChildren().get(0).setDisable(false);
			        dialog.close();
			        if(focusable != null) {
			        	focusable.requestFocus();
			        }
				}
			});
			content.setActions(button);
			dialog.setOverlayClose(false);
			
			GaussianBlur blur = new GaussianBlur();
			stackPane.getChildren().get(0).setEffect(blur);
			stackPane.getChildren().get(0).setDisable(true);
			
			
			dialog.show();
			
			
			return dialog;
			
		} else {
			
			return null;
			
		}
		
	}
	
	
	public static String inputDialog(Stage owner, String title, String header, String content, String previousContent) {
		
		String stringReturn = null;
		try {
			Dialog dialog = new Dialog();
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.getStylesheets().add(Dialogs.class.getResource("/hojasestilo/textInputDialog.css").toExternalForm());
			dialogPane.getStylesheets().add(Dialogs.class.getResource("/hojasestilo/color-pallete.css").toExternalForm());
			dialogPane.getStylesheets().add(Dialogs.class.getResource("/hojasestilo/fonts.css").toExternalForm());
			dialogPane.getStyleClass().add("textInputDialog");

			GridPane grid = new GridPane();
			grid.setHgap(25);
			grid.setVgap(20);
			grid.setPadding(new Insets(20, 20, 20, 20));
		    
			Label lblContent = new Label(content);
			
			TextField txtContent = new TextField(previousContent);

			grid.add(lblContent, 0, 0);
			grid.add(txtContent, 1, 0);
			
			dialog.setTitle(title);
			dialog.setHeaderText(header);
			dialogPane.setContent(grid);
			dialog.setGraphic(null);
			
		    ButtonType buttonAccept = new ButtonType("Modificar", ButtonData.OK_DONE);
		    dialog.getDialogPane().getButtonTypes().addAll(buttonAccept);

			Button btnModify = (Button) dialogPane.lookupButton(buttonAccept);
			btnModify.setContentDisplay(ContentDisplay.RIGHT);
			MaterialDesignIconView icoModify = new MaterialDesignIconView(MaterialDesignIcon.PENCIL);
			icoModify.getStyleClass().add("glyph");
			icoModify.setGlyphSize(16);
			btnModify.setGraphic(icoModify);
			btnModify.getStyleClass().add("acceptButton");
			btnModify.setGraphicTextGap(8);
			
		    Platform.runLater(() -> txtContent.requestFocus());
		    
		    Optional<ButtonType> result = dialog.showAndWait();
			if (result.get() == buttonAccept){
				stringReturn = txtContent.getText().trim();
			}
		} catch (NoSuchElementException e) {
			return null;
		}
		return stringReturn;
	}
	
}
