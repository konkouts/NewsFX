package org.example;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import exception.MissingCountryException;
import exception.NewsAPIException;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.text.Text;
import model.NewsAPI;
import model.NewsInfo;
import services.NewsAPIService;

import static javafx.scene.text.TextAlignment.CENTER;

public class NewsSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {

      //List of News
      ArrayList<NewsInfo> newsList;
      FlowPane buttonFlowPane;
      GridPane rootGridPane, inputFieldsPane;
      Button searchBtn, backBtn;
      Label countryLbl, categoryLbl;
      TextField countryField;
      TableView<NewsInfo> newsTableView;
      ChoiceBox categoryChoiceBox = new ChoiceBox();
      Text text = new Text();

      public NewsSceneCreator(double width, double height) throws IOException, GeoIp2Exception {
            super(width, height);
            newsList = new ArrayList<NewsInfo>();
            rootGridPane = new GridPane();
            buttonFlowPane = new FlowPane();

            //text will display the messages to the user
           text.setText("");
           text.setWrappingWidth(170);

            countryLbl = new Label("Χώρα: ");
            categoryLbl = new Label("Κατηγορία: ");

            categoryChoiceBox.getItems().add("business");
            categoryChoiceBox.getItems().add("entertainment");
            categoryChoiceBox.getItems().add("general");
            categoryChoiceBox.getItems().add("health");
            categoryChoiceBox.getItems().add("science");
            categoryChoiceBox.getItems().add("sports");
            categoryChoiceBox.getItems().add("technology");
            categoryChoiceBox.setMinWidth(150);
            categoryChoiceBox.setValue("sports");

            countryField = new TextField();
            countryField.setText(CountryFounder.countryCode());

            searchBtn = new Button("Αναζήτηση");
            backBtn = new Button("Επιστροφή");

            inputFieldsPane = new GridPane();
            newsTableView = new TableView<NewsInfo>();
            newsTableView.setMinWidth(750);
            newsTableView.setMinHeight(500);

            //Customise flow pane
            buttonFlowPane.setHgap(10);
            buttonFlowPane.setAlignment(Pos.BOTTOM_CENTER);
            buttonFlowPane.getChildren().add(searchBtn);

            //CUSTOMISE INPUT FIELD GRID PANE
            inputFieldsPane.setAlignment(Pos.CENTER);
            inputFieldsPane.setVgap(10);
            inputFieldsPane.setHgap(10);
            inputFieldsPane.add(countryLbl,0,0);
            inputFieldsPane.add(countryField,1,0);
            inputFieldsPane.add(categoryLbl,0,1);
            inputFieldsPane.add(categoryChoiceBox,1,1);
            inputFieldsPane.add(text, 1,20);

            //Customise root grid pane
            rootGridPane.setVgap(10);
            rootGridPane.setHgap(10);
            rootGridPane.add(inputFieldsPane,0,0);
            rootGridPane.add(newsTableView,1,0);
            rootGridPane.add(buttonFlowPane,0,1);
            rootGridPane.add(backBtn,1,1);

            //Customise table view
            TableColumn<NewsInfo, String> titleColumn = new TableColumn<>("Title");
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            newsTableView.getColumns().add(titleColumn);

            TableColumn<NewsInfo, Object> authorColumn = new TableColumn<>("Author");
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            newsTableView.getColumns().add(authorColumn);

            TableColumn<NewsInfo, String> descriptionColumn = new TableColumn<>("Description");
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            newsTableView.getColumns().add(descriptionColumn);

            TableColumn<NewsInfo, String> urlColumn = new TableColumn<>("URL");
            urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
            newsTableView.getColumns().add(urlColumn);

            TableColumn<NewsInfo, String> publishedColumn = new TableColumn<>("Published At");
            publishedColumn.setCellValueFactory(new PropertyValueFactory<>("publishedAt"));
            newsTableView.getColumns().add(publishedColumn);

            //define column size
            titleColumn.prefWidthProperty().bind(newsTableView.widthProperty().divide(5));
            authorColumn.prefWidthProperty().bind(newsTableView.widthProperty().divide(7));
            descriptionColumn.prefWidthProperty().bind(newsTableView.widthProperty().divide(3));
            urlColumn.prefWidthProperty().bind(newsTableView.widthProperty().divide(5));
            publishedColumn.prefWidthProperty().bind(newsTableView.widthProperty().divide(8));

            // Attach events
            backBtn.setOnMouseClicked(this);
            searchBtn.setOnMouseClicked(this);
      }

      @Override
      Scene createScene() {
            return new Scene(rootGridPane, width, height);
      }

      @Override
      public void handle(MouseEvent event) {
          // in case to back button is clicked, the app returns to the start page
            if (event.getSource() == backBtn){
                  App.primaryStage.setTitle("NEWS APP");
                  App.primaryStage.setScene(App.mainScene);
            }
            // when search button is clicked
            else if (event.getSource() == searchBtn) {
                // get values from fields
                  String category = (String) categoryChoiceBox.getValue();
                  String country = countryField.getText();
                  try {
                      //call country founder method to define the user's country code
                        System.out.println(CountryFounder.countryCode());
                  } catch (IOException e) {
                        e.printStackTrace();
                  } catch (GeoIp2Exception e) {
                        e.printStackTrace();
                  }
                  //call searchNews to perform the request
                  searchNews(country,category);
                  //tableSync updates the tableView with the results of the request
                  tableSync();
                  //clearTextFields clears the fields for a new search
                  clearTextFields();
            }
      }

      public void searchNews(String country, String category) {
            try {
                  text.setText("");
                  final NewsAPIService newsSearchService = NewsAPI.getNewsAPIService();
                  final List<NewsInfo> results;
                  results = newsSearchService.getPopularNews(country, category);
                  newsList.clear();
                  newsList.addAll(results);

                  tableSync();

                  System.out.println(results);

                  results.forEach(System.out::println);
            } catch (NewsAPIException | MissingCountryException e) {

                  App.primaryStage.setTitle("ΤΡΕΧΟΝΤΕΣ ΚΟΡΥΦΑΙΟΙ ΤΙΤΛΟΙ ΕΙΔΗΣΕΩΝ");
                  App.primaryStage.setScene(App.newsScene);

               text.setText(e.getMessage());
            }

      }

      public void tableSync() {
            List<NewsInfo> items = newsTableView.getItems();
            items.clear();

            for (NewsInfo news : newsList) {
                  if (news instanceof NewsInfo) {
                        items.add(news);
                  }
            }
      }

      public void clearTextFields() {
            countryField.setText("");
            categoryChoiceBox.setValue("");
      }
      
}
