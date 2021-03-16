package main;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.text.StyledEditorKit.FontFamilyAction;
import javax.swing.text.StyledEditorKit.FontSizeAction;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Logger;
import com.firebase.client.ValueEventListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Kisi;
import model.Otopark;

public class App extends Application {

	private Stage stage;
	private Otopark[] otoparkDizisi;
	private int selectedOtoparkIndex = -1;
	private List<Button> btnList = new ArrayList<>();
	private Kisi selectedKisi = null;

	private TextField fieldIsim, fieldSoyisim, fieldPlaka, fieldSure;

	private boolean first = false;
	private Scene loginScene;

	private Firebase firebase;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Firebase.getDefaultConfig().setLogLevel(Logger.Level.INFO);

		firebase = new Firebase("https://otoparksistemi-b39b4.firebaseio.com/");

		firebase.equals("otoparkListesi");
		

		firebase.child("otoparkListesi").addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot ds) {
				otoparkDizisi = ds.getValue(Otopark[].class);
				System.out.println(Arrays.toString(otoparkDizisi));
				if (first) {
					updateBtnList();
					System.out.println("update");
				}
				first = true;
			}

			@Override
			public void onCancelled(FirebaseError fe) {

			}
		});

		this.stage = primaryStage;
		
		loginScene = loginScene();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});

		primaryStage.setTitle("Otopark");
		primaryStage.setScene(loginScene);
		// primaryStage.resizableProperty().set(false);
		primaryStage.show();

	}

	public Scene loginScene() {

		AnchorPane loginPane = new AnchorPane();

		Label lblUsername = new Label("Kullanici Adi");
		lblUsername.setLayoutX(20);
		lblUsername.setLayoutY(45);

		TextField fieldUsername = new TextField();
		fieldUsername.setLayoutX(110);
		fieldUsername.setLayoutY(40);

		Label lblPassword = new Label("Şifre");
		lblPassword.setLayoutX(20);
		lblPassword.setLayoutY(95);

		PasswordField fieldPassword = new PasswordField();
		fieldPassword.setLayoutX(110);
		fieldPassword.setLayoutY(90);

		Button btnLogin = new Button("Giriş");
		btnLogin.setMinSize(70, 40);
		btnLogin.setLayoutX(110);
		btnLogin.setLayoutY(140);

		btnLogin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String username = fieldUsername.getText().trim();
				String password = fieldPassword.getText().trim();

				if (username.length() == 0 || password.length() == 0) {
					showAlertDialog(AlertType.WARNING, "", "Eksik Bilgi", "Giriş yapmak için tüm alanlari doldurunuz!");
				} else {
					if(otoparkDizisi == null) {
						return;
					}
					boolean loginStatus = false;
					for (int i = 0; i < otoparkDizisi.length; i++) {
						Otopark o = otoparkDizisi[i];
						if (o.getKullaniciAdi().equals(username) && o.getSifre().equals(password)) {
							loginStatus = true;
							selectedOtoparkIndex = i;
						}
					}
					if (loginStatus) {
						stage.setScene(mainScene());
						fieldPassword.clear();
						fieldUsername.clear();
					} else {
						showAlertDialog(AlertType.ERROR, "", "Hatali Bilgi", "Kullanici Adi veya Şifre Hatali!");
					}
				}
			}
		});

		loginPane.getChildren().addAll(lblUsername, fieldUsername, lblPassword, fieldPassword, btnLogin);

		return new Scene(loginPane, 300, 200);
	}

	public Scene mainScene() {

		AnchorPane mainPane = new AnchorPane();

		int x = 10, y = 10;
		int counter = 0;
		int btnCounter = 0;
		
		btnList.clear();

		for (int i = 0; i < otoparkDizisi[selectedOtoparkIndex].getToplamYerSayisi(); i++) {
			Button btn = new Button();
			btn.setMinSize(50, 50); // #31a556-boş // #ce3b3b-dolu
			btn.setText(String.valueOf(++btnCounter));
			btn.textFillProperty().set(Paint.valueOf("#fff"));
			btn.setLayoutX(x);
			btn.setLayoutY(y);

			x += 60;
			counter++;

			if (counter == 6) {
				x = 10;
				y += 60;
				counter = 0;
			}

			mainPane.getChildren().add(btn);
			btnList.add(btn);
		}

		updateBtnList();
		
		
		Button btnCikis = new Button("Çıkış");
				btnCikis.setLayoutX(530);
				btnCikis.setLayoutY(240);
				btnCikis.setMinSize(80,30);
				btnCikis.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						stage.setScene(loginScene);
						selectedKisi = null;
						selectedOtoparkIndex = -1;
						if(currentThread != null) {
							currentThread.interrupt();
						}
					}
				});

		Label lblIsim = new Label("İsim");

		lblIsim.setLayoutX(400);
		lblIsim.setLayoutY(35);
		lblIsim.setFont(Font.font(FontFamilyAction.DEFAULT, FontWeight.BOLD, Font.getDefault().getSize()));

		fieldIsim = new TextField();
		fieldIsim.setDisable(true);
		fieldIsim.setLayoutX(460);
		fieldIsim.setLayoutY(30);

		Label lblSoyisim = new Label("Soyisim");

		lblSoyisim.setLayoutX(400);
		lblSoyisim.setLayoutY(85);
		lblSoyisim.setFont(Font.font(FontFamilyAction.DEFAULT, FontWeight.BOLD, Font.getDefault().getSize()));

		fieldSoyisim = new TextField();
		fieldSoyisim.setDisable(true);
		fieldSoyisim.setLayoutX(460);
		fieldSoyisim.setLayoutY(80);

		Label lblPlaka = new Label("Plaka");

		lblPlaka.setLayoutX(400);
		lblPlaka.setLayoutY(135);
		lblPlaka.setFont(Font.font(FontFamilyAction.DEFAULT, FontWeight.BOLD, Font.getDefault().getSize()));

		fieldPlaka = new TextField();
		fieldPlaka.setDisable(true);
		fieldPlaka.setLayoutX(460);
		fieldPlaka.setLayoutY(130);

		Label lblSure = new Label("Sure");

		lblSure.setLayoutX(400);
		lblSure.setLayoutY(185);
		lblSure.setFont(Font.font(FontFamilyAction.DEFAULT, FontWeight.BOLD, Font.getDefault().getSize()));

		fieldSure = new TextField();
		fieldSure.setDisable(true);
		fieldSure.setLayoutX(460);
		fieldSure.setLayoutY(180);
		
		Button btnSil = new Button("Sil");
		btnSil.setLayoutX(420);
		btnSil.setLayoutY(240);
		
		btnSil.setMinSize(80, 30);
		
		btnSil.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(selectedKisi == null) {
					showAlertDialog(AlertType.ERROR, "Hata", "Kişi Seçmediniz!", "Silmek istediğiniz kaydi seçmediniz!");
				} else {
					ButtonType typeSil = new ButtonType("Sil");
					ButtonType typeSakla = new ButtonType("iptal");

					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Uyari");
					alert.setHeaderText("Silmek istediğinizden Emin Misiniz?");
					alert.setContentText(selectedKisi.getIsim() + " " + selectedKisi.getSoyisim()
							+ " kişisi silenecek onayliyor musunuz?");

					alert.getButtonTypes().clear();
					alert.getButtonTypes().addAll(typeSil, typeSakla);

					Optional<ButtonType> btnType = alert.showAndWait();
					
					if(btnType.get() == typeSil) {
						otoparkDizisi[selectedOtoparkIndex].getRezerveEdenler().remove(selectedKisi);
						otoparkDizisi[selectedOtoparkIndex]
								.setBosYerSayisi(otoparkDizisi[selectedOtoparkIndex].getBosYerSayisi() + 1);
						firebase.child("otoparkListesi").setValue(otoparkDizisi);
						updateBtnList();
						selectedKisi = null;
						fieldIsim.clear();
						fieldSoyisim.clear();
						fieldPlaka.clear();
						fieldSure.clear();
					} else if(btnType.get() == typeSakla) {
						
					}
					
					
				}
			}
		});
		
		

		mainPane.getChildren().addAll(lblIsim, fieldIsim, lblSoyisim, fieldSoyisim, lblPlaka, fieldPlaka, lblSure,
				fieldSure, btnSil, btnCikis);
		currentThread = new Thread(runnable);
		currentThread.start();

		return new Scene(mainPane, 700, 400);
	}

	public void updateBtnList() {

		if (selectedOtoparkIndex == -1)
			return;

		for (int i = 0; i < btnList.size(); i++) {
			Button btn = btnList.get(i);

			btn.setBackground(
					new Background(new BackgroundFill(Paint.valueOf("#31a556"), new CornerRadii(5), new Insets(10))));
			btn.setOnAction(null);
		}

		for (int i = 0; i < otoparkDizisi[selectedOtoparkIndex].getRezerveEdenler().size(); i++) {
			Button btn = btnList.get(i);
			btn.setBackground(
					new Background(new BackgroundFill(Paint.valueOf("#ce3b3b"), new CornerRadii(5), new Insets(10))));

			Kisi kisi = otoparkDizisi[selectedOtoparkIndex].getRezerveEdenler().get(i);

			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					selectedKisi = kisi;
					fieldIsim.setText(kisi.getIsim());
					fieldSoyisim.setText(kisi.getSoyisim());
					fieldPlaka.setText(kisi.getPlaka());
					fieldSure.setText(kisi.getKalinacakSure());
				}
			});
		}
	}

	public void showAlertDialog(AlertType type, String title, String header, String contentText) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(contentText);
		alert.setHeaderText(header);
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	Thread currentThread = null;

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				Date now = new Date();

				if (selectedOtoparkIndex > -1) {
					List<Kisi> kisiler = otoparkDizisi[selectedOtoparkIndex].getRezerveEdenler();

					System.out.println("dk");

					for (int i = 0; i < kisiler.size(); i++) {
						Kisi kisi = kisiler.get(i);
						
						long diff = new Date().getTime() - kisi.getRezervasyonSaati().getTime();

						long diffMinutes = diff / (60 * 1000) % 60;
						
						diffMinutes += 58; 
						
						System.out.println(diffMinutes);

						if (kisi.isOnay() == false && diffMinutes >= 5) {
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									System.out.println(kisi.getIsim() + " süresi dolmuş.");

									ButtonType typeSil = new ButtonType("Sil");
									ButtonType typeSakla = new ButtonType("Sakla");

									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("Uyari");
									alert.setHeaderText("Süre Doldu!");
									alert.setContentText(kisi.getIsim() + " " + kisi.getSoyisim()
											+ " isimli kişinin rezervasyon süresi doldu.");

									alert.getButtonTypes().clear();
									alert.getButtonTypes().addAll(typeSil, typeSakla);

									Optional<ButtonType> btnType = alert.showAndWait();

									if (btnType.get() == typeSil) {
										
										kisiler.remove(kisi);
										otoparkDizisi[selectedOtoparkIndex]
												.setBosYerSayisi(otoparkDizisi[selectedOtoparkIndex].getBosYerSayisi() + 1);

										updateBtnList();
									} else if (btnType.get() == typeSakla) {
										kisi.setOnay(true);
									}
									firebase.child("otoparkListesi").setValue(otoparkDizisi);
								}
							});
							
						}
					}
				}

				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	};

}
