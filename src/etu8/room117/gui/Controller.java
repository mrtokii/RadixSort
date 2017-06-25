package etu8.room117.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import java.util.Vector;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;

import java.util.Random;

public class Controller {
    private Vector<Canvas> vectorCanvas;
    private Vector<ScrollPane> vectorScrollPane;
    private Vector<VBox> vectorVBox;
    private ScrollPane prevMark=null;
    private int canvasLength=3000;
    private  int CanvasWidth=168;
    int spaceFont = 4;
    int fontSize = 35;
    private Canvas canvasArea;

    @FXML
    private TextField TextFieldTill;
    @FXML
    private TextField TextFieldNum;
    @FXML
    private TextArea Text_Area;
    @FXML
    private Label DigitLabel;
    @FXML
    private TextField Text_Field;
    @FXML
    private HBox MainHBox;
    @FXML
    private ScrollPane scrollAreaArray;
    @FXML
    private ScrollPane scrollPaneLow;

    Vector<Integer> InitialArrayInt;
    Vector<String> InitialArrayString;


    public void onNextStepButtonClicked(ActionEvent event) { // Если нажата клавиша "Следующий шаг"
        //updateWorkingArray(InitialArrayInt);
        //setCanvases(36);


    }

    public  void onEnterDataClicked(ActionEvent event) { // Если нажата клавиша "Ввести данные"
        boolean choice = true; // true - это строка чисел, false - строка
        String str = Text_Field.getText();
        String strArray[] = str.split(" ");
        for (int i = 0; i < strArray.length; i++) {
            if(!isDigit(strArray[i])) //Вернет true, если строка может быть преобразована в число
            {
                choice = false;
            }
        }

        if (choice)
        {
            InitialArrayInt = new Vector<Integer>();
            for (int i = 0; i <strArray.length ; ++i) {
                InitialArrayInt.add(Integer.parseInt(strArray[i]));
            }
            setCanvases(10);
            sortNumbers(InitialArrayInt);
            updateWorkingArrayInteger(InitialArrayInt);
            updateComponentsArrayInteger(InitialArrayInt,1,3);


        }
        else
        {
            InitialArrayString = new Vector<String>();
            for (int i = 0; i <strArray.length ; ++i) {
                InitialArrayString.add(strArray[i]);
            }
            setCanvases(36);
            sortStrings(InitialArrayString);
            updateWorkingArrayString(InitialArrayString);
            updateComponentsArrayString(InitialArrayString,33,3);
        }
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void sortNumbers(Vector<Integer> arr) {

    }

    public void sortStrings(Vector<String> arr) {

    }

    public void onClearButtonClicked(ActionEvent event) { // Если нажата клавиша "Сброс"
            MainHBox.getChildren().remove(0,vectorCanvas.size());
    }
    public void onFinishSortingButtonClicked(ActionEvent event){ // Если нажата клавиша "Завершить сортировку"

    }

    public void setCanvases(int numCanvases){ // Установить количество столбцов

        if(vectorCanvas!=null)
        {
            MainHBox.getChildren().remove(0,vectorCanvas.size());
        }
        vectorCanvas = new Vector<Canvas>();
        vectorScrollPane= new Vector<ScrollPane>();
        vectorVBox = new Vector<VBox>();
        Character sym = 'A';
        for (int i = 0; i < numCanvases; ++i) {
            String name;
            VBox vBox = new VBox();//Создали вертикальный слой
            Label label = new Label();
            if(i<10)
            {
                Integer sign = i;
                name = sign.toString();
            }
            else
            {
                name = sym.toString();
                char ch = (char) sym;
                int n = (int)ch;
                ++n;
                ch = (char)n;
                sym = (Character)ch;
            }
            label = new Label(name);//Создали название колонки
            label.setFont(new Font(24));
            label.setPrefWidth(185);
            label.setAlignment(Pos.CENTER);
            ScrollPane scrollPane = new ScrollPane();//Создали панель скроллинга
            scrollPane.setStyle("-fx-border-color: white");
            scrollPane.setPrefHeight(400);
            scrollPane.setMinHeight(200);
            scrollPane.setPrefWidth(185);
            Canvas canvas = new Canvas(CanvasWidth,canvasLength);//Создали холст
            scrollPane.setContent(canvas);
            vBox.getChildren().add(scrollPane);
            vBox.getChildren().add(label);
            MainHBox.getChildren().add(vBox);
            vectorCanvas.add(canvas);
            vectorScrollPane.add(scrollPane);
            vectorVBox.add(vBox);
            setCanvasArea();
        }
    }

    public void setCanvasArea(){
        canvasArea = new Canvas(1900,3000);
        scrollAreaArray.setContent(canvasArea);
    }

    public  void onFillingButtonClicked(ActionEvent event){ // Если нажата клавиша "Заполнить"
        Text_Field.clear();
        final Random random = new Random();
        String strNum = TextFieldNum.getText();
        String strTill = TextFieldTill.getText();
        int count = Integer.parseInt(strNum);
        int edge = Integer.parseInt(strTill);
        for (int i = 0; i <count ; ++i) {
            Integer num = random.nextInt(edge);
            Text_Field.appendText(num.toString() + " ");
        }
    }

    public  void updateCurrentDigit(int digit){
        if(digit !=0){
            Integer dig = new Integer(digit);
            DigitLabel.setText(dig.toString());
        } else {
            DigitLabel.setText("");
        }
    }
    public void onExitButtonClicked(ActionEvent event) {
        System.exit(0);
    }


    public void updateWorkingArrayInteger(Vector<Integer> array){ // Заполняет Text_Area данными из вектора
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        gc.clearRect(0,0,1900,3000);
        gc.setFont(new Font("Consolas",fontSize));//Courier New
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.LEFT);
        int x = 5;
        int y = 30;

        for (int i = 0; i < array.size() ; i++) {
            Integer num = array.get(i);
            String str = num.toString();
            if(i==0)
            {
                gc.setFill(Color.ORANGE);
            }
            else
            {
                gc.setFill(Color.GREEN);
            }
            for (int j =0; j<= str.length()-1; j++) {
                char ch = str.charAt(j);
                String s = Character.toString(ch);
                gc.fillText(s, x, y);
                x = x + 18;

            }

            x=x+25;
            if(i!=array.size()-1)
            {
                Integer n = array.get(i+1);
                String string = n.toString();
                if(x+string.length()*18>1800) {
                    y = y + fontSize + spaceFont;
                    x = 5;
                }
            }
        }
    }

    public void updateWorkingArrayString(Vector<String> array) {
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        gc.clearRect(0,0,1900,3000);
        gc.setFont(new Font("Consolas",fontSize));//Courier New
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.LEFT);
        int x = 5;
        int y = 30;
        for (int i = 0; i < array.size() ; i++) {
            String str = array.get(i);
            if(i==0)
            {
                gc.setFill(Color.ORANGE);
            }
            else
            {
                gc.setFill(Color.GREEN);
            }
            for (int j =0; j<= str.length()-1; j++) {
                char ch = str.charAt(j);
                String s = Character.toString(ch);
                gc.fillText(s, x, y);
                x = x + 18;
            }
            x=x+25;
            if(i!=array.size()-1)
            {
                if(x+array.get(i+1).length()*18>1800) {
                    y = y + fontSize + spaceFont;
                    x = 5;
                }
            }
        }

    }


    /**
     *
     * @param array
     * @param column
     */
    public void updateComponentsArrayInteger(Vector<Integer> array, int column, int digit){
        if(prevMark!=null)prevMark.setStyle("-fx-border-color: white");
        vectorScrollPane.get(column).setStyle("-fx-border-color: red");
        prevMark=vectorScrollPane.get(column);
        GraphicsContext gc = vectorCanvas.get(column).getGraphicsContext2D();
        gc.setFont(new Font("Consolas",fontSize));//Courier New
        gc.clearRect(0,0,CanvasWidth,canvasLength);
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.RIGHT);
        double scrollMiss=1.0;
        int y = canvasLength-spaceFont;
        int x = CanvasWidth-5;
        for (int i = 0; i < array.size(); i++) {
            Integer num = array.get(i);
            String str = num.toString();
            x = CanvasWidth-5;
            if(i==array.size()-1)
            {
                gc.setFill(Color.ORANGE);
            }
            for (int j = str.length()-1; j>=0; j--) {
                char ch = str.charAt(j);
                String s = Character.toString(ch);
                if(j==str.length()-digit)
                {
                    gc.setStroke(Color.RED);
                    gc.setFill(Color.RED);
                    gc.fillText(s, x, y);
                    x = x - 17; //Промежуток между символами
                    gc.setStroke(Color.GREEN);
                    gc.setFill(Color.GREEN);
                    if(i==array.size()-1)
                    {
                        gc.setFill(Color.ORANGE);
                    }
                }
                else {
                    gc.fillText(s, x, y);
                    x = x - 17;
                }
            }
            y= y-fontSize-spaceFont;
            if(i%10==0 && i>9)
            {
                scrollMiss= scrollMiss-0.15;
            }
            vectorScrollPane.get(column).setVvalue(scrollMiss);
        }

    }

    public void updateComponentsArrayString(Vector<String> array, int column, int digit){
        if(prevMark!=null) prevMark.setStyle("-fx-border-color: white");
        vectorScrollPane.get(column).setStyle("-fx-border-color: red");
        prevMark=vectorScrollPane.get(column);
        GraphicsContext gc = vectorCanvas.get(column).getGraphicsContext2D();
        gc.clearRect(0,0,CanvasWidth,canvasLength);
        gc.setFont(new Font("Consolas",fontSize));//Courier New
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.LEFT);
        double scrollMiss=1.0;
        if(column>9 && column<20) scrollPaneLow.setHvalue(0.385);
        if(column>19 && column<30) scrollPaneLow.setHvalue(0.775);
        if(column>29) scrollPaneLow.setHvalue(1.0);
        int y = canvasLength-spaceFont-5;
        int x = 5;
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i);
            x = 5;
            if(i==array.size()-1)
            {
                gc.setFill(Color.ORANGE);
            }
            for (int j =0; j<= str.length()-1; j++) {
                char ch = str.charAt(j);
                String s = Character.toString(ch);
                if(j==digit-1)
                {
                    gc.setStroke(Color.RED);
                    gc.setFill(Color.RED);
                    gc.fillText(s, x, y);
                    x = x + 18; //Промежуток между символами
                    gc.setStroke(Color.GREEN);
                    gc.setFill(Color.GREEN);
                    if(i==array.size()-1)
                    {
                        gc.setFill(Color.ORANGE);
                    }
                }
                else {
                    gc.fillText(s, x, y);
                    x = x + 18;
                }
            }
            y= y-fontSize-spaceFont;
            if(i%10==0 && i>9)
            {
                scrollMiss= scrollMiss-0.15;
            }
            vectorScrollPane.get(column).setVvalue(scrollMiss);
        }

    }
}