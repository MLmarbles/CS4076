package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Controller implements Initializable {
	private final Queries queries = new Queries();
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Label monday9;
	@FXML
	private Label monday10;
	@FXML
	private Label monday11;
	@FXML
	private Label monday12;
	@FXML
	private Label monday13;
	@FXML
	private Label monday14;
	@FXML
	private Label monday15;
	@FXML
	private Label monday16;
	@FXML
	private Label monday17;

	@FXML
	private Label tuesday9;
	@FXML
	private Label tuesday10;
	@FXML
	private Label tuesday11;
	@FXML
	private Label tuesday12;
	@FXML
	private Label tuesday13;
	@FXML
	private Label tuesday14;
	@FXML
	private Label tuesday15;
	@FXML
	private Label tuesday16;
	@FXML
	private Label tuesday17;

	@FXML
	private Label wednesday9;
	@FXML
	private Label wednesday10;
	@FXML
	private Label wednesday11;
	@FXML
	private Label wednesday12;
	@FXML
	private Label wednesday13;
	@FXML
	private Label wednesday14;
	@FXML
	private Label wednesday15;
	@FXML
	private Label wednesday16;
	@FXML
	private Label wednesday17;

	@FXML
	private Label thursday9;
	@FXML
	private Label thursday10;
	@FXML
	private Label thursday11;
	@FXML
	private Label thursday12;
	@FXML
	private Label thursday13;
	@FXML
	private Label thursday14;
	@FXML
	private Label thursday15;
	@FXML
	private Label thursday16;
	@FXML
	private Label thursday17;

	@FXML
	private Label friday9;
	@FXML
	private Label friday10;
	@FXML
	private Label friday11;
	@FXML
	private Label friday12;
	@FXML
	private Label friday13;
	@FXML
	private Label friday14;
	@FXML
	private Label friday15;
	@FXML
	private Label friday16;
	@FXML
	private Label friday17;

	@FXML
	private TextField usernameSignIn;
	private String globalUsername = "";
	@FXML
	private TextField passwordSignIn;
	@FXML
	private Button buttonSignIn;
	@FXML
	private Label signInError;
	@FXML
	private TextField usernameSignUp;
	@FXML
	private TextField passwordSignUp;
	@FXML
	private Button buttonSignUp;
	@FXML
	private Label signUpError;
	@FXML
	private TextField confirmPassword;
	@FXML
	private TextField enterModuleCode;
	@FXML
	private Button addModule;
	@FXML
	private Button removeModule;
	@FXML
	private Label serverOutput;
	@FXML
	private Label module1;
	@FXML
	private Label module2;
	@FXML
	private Label module3;
	@FXML
	private Label module4;
	@FXML
	private Label module5;
	private Label[] modules;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modules = new Label[]{module1, module2, module3, module4, module5};
	}

	public void switchToMain() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage) usernameSignIn.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToSignIn(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToSignUp(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void signInButtonAction() throws IOException {
		String username = usernameSignIn.getText();
		String password = passwordSignIn.getText();
		try {
			boolean userExists = queries.userExists(username);

			if (userExists) {
				// Username exists
				// Now check if login credentials match
				boolean loginMatch = queries.loginMatch(username, password);
				if (loginMatch) {
					// Login successful
					globalUsername = username;
					switchToMain();
				} else {
					// Login failed
					signInError.setText("Username and Password do not match");
				}
			} else {
				// Username doesn't exist
				signInError.setText("User does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void signUpButtonAction() {
		String username = usernameSignUp.getText();
		String password = passwordSignUp.getText();
		String confirmPasswordText = confirmPassword.getText();
		try {
			boolean userExists = queries.userExists(username);
			if (password.equals(confirmPasswordText)) {
				if (userExists) {
					signUpError.setText("Username Already Taken");
				} else {
					queries.insertUser(username, password);
					signUpError.setText("Account created, Sign In");
				}
			} else {
				signUpError.setText("Passwords do not match");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String[] moduleList = new String[5];
	public void addModuleButtonAction() {
	    String moduleCode = enterModuleCode.getText();
	    try {
	        for (int i = 0; i < 5; i++) {
	            if (moduleList[i] == null) {
	                queries.addModules(moduleCode, globalUsername);
	                modules[i].setText(moduleCode);
	                moduleList[i] = moduleCode; // Update the moduleList array
	                break; // Exit the loop once a null element is found
	            } else {
	                serverOutput.setText("Server: You can only have 5 modules at one time");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	
	public void removeModuleButtonAction() {
		String moduleCode = enterModuleCode.getText();
	    try {
	        queries.removeModule(moduleCode, globalUsername);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
