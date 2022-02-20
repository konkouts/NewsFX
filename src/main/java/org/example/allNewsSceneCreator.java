package org.example;

import exception.NewsAPIException;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.NewsAPI;
import model.NewsInfo;
import services.NewsAPIService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class allNewsSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {

    //List of News
    ArrayList<NewsInfo> newsList;
    FlowPane buttonFlowPane;
    GridPane rootGridPane, inputFieldsPane;
    Button searchBtn, backBtn, historyBtn;
    Label queryLbl, sourceLbl, fromLbl, toLbl, languageLbl;
    TextField queryField, sourceField, fromField, toField, languageField;
    TableView<NewsInfo> newsTableView;
    Text text = new Text();
    ChoiceBox languageChoiceBox = new ChoiceBox();
    //array to store previous searches
    String[][] history = new String[5][5];

    //variables to manage the history feature
    int historySize = 0;
    int recent = history.length;

    public allNewsSceneCreator(double width, double height) {
        super(width, height);
        newsList = new ArrayList<NewsInfo>();
        rootGridPane = new GridPane();
        buttonFlowPane = new FlowPane();

        text.setText("");
        text.setWrappingWidth(170);

        queryLbl = new Label("Αναζήτηση: ");
        sourceLbl = new Label("Πηγή: ");
        fromLbl = new Label("Από: ");
        toLbl = new Label("Έως: ");
        languageLbl = new Label("Γλώσσα: ");

        languageChoiceBox.getItems().add("ar");
        languageChoiceBox.getItems().add("de");
        languageChoiceBox.getItems().add("en");
        languageChoiceBox.getItems().add("es");
        languageChoiceBox.getItems().add("fr");
        languageChoiceBox.getItems().add("he");
        languageChoiceBox.getItems().add("it");
        languageChoiceBox.getItems().add("nl");
        languageChoiceBox.getItems().add("no");
        languageChoiceBox.getItems().add("pt");
        languageChoiceBox.getItems().add("ru");
        languageChoiceBox.getItems().add("se");
        languageChoiceBox.getItems().add("ud");
        languageChoiceBox.getItems().add("zh");
        languageChoiceBox.setMinWidth(150);

        queryField = new TextField();
        sourceField = new TextField();
        fromField = new TextField();
        toField = new TextField();
//      languageField = new TextField();

        searchBtn = new Button("Αναζήτηση");
        backBtn = new Button("Επιστροφή");
        historyBtn = new Button("Ιστορικό");

        inputFieldsPane = new GridPane();
        newsTableView = new TableView<NewsInfo>();
        newsTableView.setMinHeight(500);
        newsTableView.setMinWidth(750);

        //Customise flow pane
        buttonFlowPane.setHgap(10);
        buttonFlowPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonFlowPane.getChildren().add(searchBtn);
        buttonFlowPane.getChildren().add(historyBtn);

        //CUSTOMISE INPUT FIELD GRID PANE
        inputFieldsPane.setAlignment(Pos.CENTER);
        inputFieldsPane.setVgap(10);
        inputFieldsPane.setHgap(10);
        inputFieldsPane.add(queryLbl,0,0);
        inputFieldsPane.add(queryField,1,0);
        inputFieldsPane.add(sourceLbl,0,1);
        inputFieldsPane.add(sourceField,1,1);
        inputFieldsPane.add(fromLbl,0,2);
        inputFieldsPane.add(fromField,1,2);
        inputFieldsPane.add(toLbl,0,3);
        inputFieldsPane.add(toField,1,3);
        inputFieldsPane.add(languageLbl,0,4);
        inputFieldsPane.add(languageChoiceBox,1,4);
        inputFieldsPane.add(text, 1, 20);

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
        historyBtn.setOnMouseClicked(this);
    }

    @Override
    Scene createScene() {
        return new Scene(rootGridPane, width, height);
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == backBtn){
            App.primaryStage.setTitle("NEWS APP");
            App.primaryStage.setScene(App.mainScene);
        }
        else if (event.getSource() == searchBtn) {
            String query = queryField.getText();
            String source  = sourceField.getText();
            String from = fromField.getText();
            String to = toField.getText();
            String language = (String) languageChoiceBox.getValue();

            try {
                //performs request
                searchNews(query,formatSource(source), formatDate(from), formatDate(to), language);
                //saves the search in order to be used again in the future
                saveSearch(query,formatSource(source), formatDate(from), formatDate(to), language);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tableSync();
            clearTextFields();
        }
        //when history button is clicked the previous values are returned
        else if (event.getSource() == historyBtn) {
            if (recent > 0) {recent--;}
            else if (recent == 0 ){recent=4;}
            queryField.setText(history[recent][0]);
            sourceField.setText(history[recent][1]);
            fromField.setText(history[recent][2]);
            toField.setText(history[recent][3]);
            languageChoiceBox.setValue(history[recent][4]);
        }
    }

    //this method stores the latest searches to an array
    private void saveSearch(String query, String source, String from, String to, String language) {
        if (historySize > 4) {
            historySize = 0;
            recent = historySize;
        }
        history[historySize][0] = query;
        history[historySize][1] = source;
        history[historySize][2] = from;
        history[historySize][3] = to;
        history[historySize][4] = language;
        historySize++;
        recent = historySize;
    }

    private void searchNews(String query, String source, String from, String to, String language) {
        final NewsAPIService newsSearchService = NewsAPI.getNewsAPIService();
        final List<NewsInfo> results;
        try {
            text.setText("");
            results = newsSearchService.newsByParameters(query,source, from, to, language);
            newsList.clear();
            newsList.addAll(results);
            tableSync();
            System.out.println(results);
        } catch (NewsAPIException e) {
            App.primaryStage.setTitle("ΑΝΑΖΗΤΗΣΗ ΤΙΤΛΩΝ ΕΙΔΗΣΕΩΝ");
            App.primaryStage.setScene(App.allNewsScene);
            text.setText(e.getMessage());
        }
    }

    // format source input in order to be compliant with newsapi format
    private String formatSource(String source) {
        return source.replace(' ' , '-').toLowerCase(Locale.ROOT);
    }

    // format date input in order to be compliant with newsapi format
    private String formatDate(String date) throws ParseException {
        String formattedDate = "";
        if (date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
            Date dateValue = input.parse(date);
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = output.format(dateValue);
        }
        else if (date.matches("\\d{2}-\\d{2}-\\d{4}")){
            SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
            Date dateValue = input.parse(date);
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = output.format(dateValue);
        }
        else if (date.matches("\\d{4}/\\d{2}/\\d{2}")){
            SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd");
            Date dateValue = input.parse(date);
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = output.format(dateValue);
        }
        return formattedDate;
    }

    private void tableSync() {
        List<NewsInfo> items = newsTableView.getItems();
        items.clear();

        for (NewsInfo news : newsList) {
            if (news instanceof NewsInfo) {
                items.add(news);
            }
        }
    }

    private void clearTextFields() {
        queryField.setText("");
        sourceField.setText("");
        fromField.setText("");
        toField.setText("");
        languageChoiceBox.setValue("");
    }
}
