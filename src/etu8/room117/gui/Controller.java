package etu8.room117.gui;

import etu8.room117.radix.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Random;

public class Controller {
    private Vector<Canvas> vectorCanvas; //Вектор, хранящий холсты для рисования колонок
    private Vector<ScrollPane> vectorScrollPane;// Вектор, хранящий панели скролинга для колонок
    private Vector<VBox> vectorVBox; // Вектор, хранящий вертикальные слои для колонок
    private ScrollPane prevMark = null; //Хранит панель скролинга, в которую записывались данные на предыдущем шаге (нужно для того, чтобы убирать подсветку с предыдущего столбца)
    private int canvasLength = 3000; //Размер холста в колонке
    private int CanvasWidth = 168; //Размер холста в колонке
    int spaceFont = 4; //Отступ между строками при выводе данных
    int fontSize = 35; //Размер шрифта для рисования
    int isFinished;

    MWController mainController = new MWController();


    @FXML
    private TextField TextFieldTill; // Строка ввода данных максимального числа для генерации
    @FXML
    private TextField TextFieldNum; // Строка ввода данных количества чисел числа для генерации
    @FXML
    private Label DigitLabel; // Строка вывода текущего разряда
    @FXML
    private TextField Text_Field; // Строка ввода данных
    @FXML
    private HBox MainHBox; // Переменная, хранящая слой
    @FXML
    private ScrollPane scrollPaneLow; //Переменная, хранящая панель скроллинга, в которой располагаются все столбцы
    @FXML
    private TextField TextFieldTillStr; //Строка ввода данных максимального количества символов в строке для генерации
    @FXML
    private TextField TextFieldNumStr; //Строка ввода данных количества строк для генерации
    @FXML
    private  Canvas canvasArea; // Холст, для рисования всего массива
    @FXML
    private Button AutoButton;

    Vector<Integer> InitialArrayInt; //Вектор чисел исходных данных
    Vector<String> InitialArrayString; //Вектор строк исходных данных
    Timeline timeline=null;

    public Controller(){
        mainController.setControl(this);
    }
    
    public void onNextStepButtonClicked(ActionEvent event) { // Если нажата клавиша "Следующий шаг"
        isFinished=mainController.onNextStepButtonClicked();
    }

    public void onPreviousStepButtonClicked(ActionEvent event) // Если нажата клавиша "Предыдущий шаг"
    {
        mainController.onPreviousStepButtonClicked();
    }

    public void onEnterDataClicked(ActionEvent event) { // Если нажата клавиша "Ввести данные"
        String str = Text_Field.getText();
        mainController.onEnterDataClicked(str);
    }

    public void onAutoButtonClicked(ActionEvent event) { //Если нажата кнопка Автоматически/Пауза
        if(timeline==null) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                onNextStepButtonClicked(event);
                if(isFinished==-2) {
                    timeline.stop();
                    timeline = null;
                    changeButton(true);
                    return;
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
            changeButton(false);
        }
        else
        {
            timeline.stop();
            timeline=null;
            changeButton(true);
        }
    }
    public void changeButton(boolean value) //true - установить "Play" false - установить "Pause"
    {
       if(value) AutoButton.setText("Автоматически");
       if(!value) AutoButton.setText("Приостановить");
    }

    public void onClearButtonClicked(ActionEvent event) { // Если нажата клавиша "Сброс"
        if(!MainHBox.getChildren().isEmpty()) {
            MainHBox.getChildren().remove(0, vectorCanvas.size());
        }
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        gc.clearRect(0, 0, 1880, 10000);
        Text_Field.clear();
        TextFieldTill.clear();
        TextFieldNum.clear();
        TextFieldNumStr.clear();
        TextFieldTillStr.clear();
        mainController.clearSaves();
        timeline=null;
        updateCurrentDigit(0);
    }

    public void onFinishSortingButtonClicked(ActionEvent event) { // Если нажата клавиша "Завершить сортировку"
        mainController.onFinishSortingButtonClicked();
    }

    public void setCanvases(int numCanvases) { // Установливает количество столбцов с холстами для вывода по разрядам (37 столбцов для строк, 10 столбцов для чисел)

        if (vectorCanvas != null) {
            if(!MainHBox.getChildren().isEmpty()) {
                MainHBox.getChildren().remove(0, vectorCanvas.size());
            }
        }
        vectorCanvas = new Vector<Canvas>();
        vectorScrollPane = new Vector<ScrollPane>();
        vectorVBox = new Vector<VBox>();
        Character sym = 'A';
        for (int i = 0; i < numCanvases; ++i) {
            String name = new String();
            VBox vBox = new VBox();//Создали вертикальный слой
            Label label = new Label();
            if (i < 10 && numCanvases<11) {
                Integer sign = i;
                name = sign.toString();
            }
            else {
                if (i < 11 && numCanvases > 10) {
                    if (i == 0) {
                        name = "$";
                    } else {
                        Integer sign = i - 1;
                        name = sign.toString();
                    }
                } else {
                    name = sym.toString();
                    char ch = (char) sym;
                    int n = (int) ch;
                    ++n;
                    ch = (char) n;
                    sym = (Character) ch;

                }
            }
            label.setText(name);//Создали название колонки
            label.setFont(new Font(24));
            label.setPrefWidth(185);
            label.setAlignment(Pos.CENTER);
            ScrollPane scrollPane = new ScrollPane();//Создали панель скроллинга
            scrollPane.setStyle("-fx-border-color: white");
            scrollPane.setPrefHeight(400);
            scrollPane.setMinHeight(200);
            scrollPane.setPrefWidth(185);
            Canvas canvas = new Canvas(CanvasWidth, canvasLength);//Создали холст
            scrollPane.setContent(canvas);
            vBox.getChildren().add(scrollPane);
            vBox.getChildren().add(label);
            MainHBox.getChildren().add(vBox);
            vectorCanvas.add(canvas);
            vectorScrollPane.add(scrollPane);
            vectorVBox.add(vBox);
        }
    }


    public void onFillingButtonClicked(ActionEvent event) { // Если нажата клавиша "Заполнить"
        Text_Field.clear();
        boolean was = false;
        final Random random = new Random();
        String strNum = TextFieldNum.getText();
        String strTill = TextFieldTill.getText();
        if(strNum.length()!=0) {
            int count = Integer.parseInt(strNum);
            int edge = Integer.parseInt(strTill);
            for (int i = 0; i < count; ++i) {
                Integer num = random.nextInt(edge);
                if(i!=count-1)
                {
                    Text_Field.appendText(num.toString() + " ");
                }
                else
                {
                    Text_Field.appendText(num.toString());
                }
            }
            was = true;
        }
        String strNumStr = TextFieldNumStr.getText();
        String strTillStr = TextFieldTillStr.getText();
        if(strNumStr.length()!=0) {
            if(was) Text_Field.appendText(" ");
            int count = Integer.parseInt(strNumStr);
            int edge = Integer.parseInt(strTillStr);

            for (int i = 0; i < count; ++i) {
                String result= new String();
                int numRepeat = random.nextInt(edge)+1;
                for (int j = 0; j < numRepeat; j++) {
                    String tempresult = new String();
                    int choiceNumOrLettter =  random.nextInt(6);
                    if(choiceNumOrLettter==1)
                    {
                        Integer num = random.nextInt(10);
                        tempresult = num.toString();
                    }else
                    {
                        int num;
                        num = random.nextInt(26);
                        char ch = 'a';
                        int m = (int) ch;
                        m = m+num;
                        ch = (char) m;
                        tempresult = String.valueOf(ch);
                    }
                    result = result+tempresult;
                }

                if(i!=count-1)
                {
                    Text_Field.appendText(result + " ");
                }
                else
                {
                    Text_Field.appendText(result);
                }
            }
        }
    }

    public void updateCurrentDigit(int digit) { //Выводит строку с текущим разрядом сортировки
        if (digit != 0) {
            Integer dig = new Integer(digit);
            DigitLabel.setText(" Текущий разряд сортировки: " + dig.toString());
        } else {
            DigitLabel.setText(" Cортировка выполнена");
        }
    }

    public void onExitButtonClicked(ActionEvent event) {
        System.exit(0);
    }

    public void updateWorkingArrayInteger(Vector<Integer> array) { // Выводит на экран содержание всего массива чисел, подлежащих сортировке
        Vector<String> strings = new Vector<String>();
        for (int i = 0; i < array.size(); i++) {
            Integer num = array.get(i);
            strings.add(num.toString());
        }
        updateWorkingArrayString(strings);
    }

    public void updateWorkingArrayString(Vector<String> array) {// Выводит на экран содержание всего массива строк, подлежащих сортировке
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        gc.clearRect(0, 0, 1880, 3000);
        gc.setFont(new Font("Consolas", fontSize));//Courier New
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.LEFT);
        int x = 5;
        int y = 30;
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i).replaceAll("[$]", "");
            if (i == 0) {
                gc.setFill(Color.ORANGE);
            } else {
                gc.setFill(Color.GREEN);
            }
            for (int j = 0; j <= str.length() - 1; j++) {
                char ch = str.charAt(j);
                String s = Character.toString(ch);
                gc.fillText(s, x, y);
                x = x + 18;
            }
            x = x + 25;
            if (i != array.size() - 1) {
                if (x + array.get(i + 1).length() * 18 > 1800) {
                    y = y + fontSize + spaceFont;
                    x = 5;
                }
            }
        }

    }

    /**
     * @param array
     * @param column
     */
    public void updateComponentsArrayInteger(Vector<Integer> array, int column, int digit) { //Выводит в столбец column числа и подсвечивает разряд digit
            Vector<String> vectorStr = new Vector<String>();
            for (int i = 0; i < array.size(); i++) {
                Integer num = array.get(i);
                vectorStr.add(num.toString());
            }
            updateComponentsArrayString(vectorStr,column,digit);
    }

    public void updateComponentsArrayString(Vector<String> array, int column, int digit) {//Выводит в столбец column строки и подсвечивает разряд digit
        GraphicsContext gc = vectorCanvas.get(column).getGraphicsContext2D();
        gc.clearRect(0, 0, CanvasWidth, canvasLength);
        gc.setFont(new Font("Consolas", fontSize));//Courier New
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.RIGHT);
        double scrollMiss = 1.0;
        int y = canvasLength - spaceFont - 5;
        int x = CanvasWidth - 5;;
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i);
                str = str.replaceAll("[$]", "_");

            x = CanvasWidth - 5;
            for (int j = str.length() - 1; j >= 0; j--) {
                char ch = str.charAt(j);
                String s = Character.toString(ch);
                if (j == str.length() - digit) {
                    gc.setStroke(Color.RED);
                    gc.setFill(Color.RED);
                    gc.fillText(s, x, y);
                    x = x - 18; //Промежуток между символами
                    gc.setStroke(Color.GREEN);
                    gc.setFill(Color.GREEN);
                } else {
                    gc.fillText(s, x, y);
                    x = x - 18;
                }
            }
            y = y - fontSize - spaceFont;
            if (i % 10 == 0 && i > 9) {
                scrollMiss = scrollMiss - 0.15;
            }
            vectorScrollPane.get(column).setVvalue(scrollMiss);
        }
    }


    public void lightColumn(int column) { //Подсветка столбца column
        if(column < 0) {

            if(prevMark != null) {
                prevMark.setStyle("-fx-border-color: white");
            }
            prevMark = null;
            return;

        } else {

            if(mainController.mode == MWController.Mode.STRINGS && column > 36)
                return;

            if(mainController.mode == MWController.Mode.NUMBERS && column > 9)
                return;

            if (prevMark != null) {
                prevMark.setStyle("-fx-border-color: white");
            }
            vectorScrollPane.get(column).setStyle("-fx-border-color: red");
            prevMark = vectorScrollPane.get(column);
        }
        scrollPaneLow.setHvalue(0);
        if (column > 8 && column < 19) scrollPaneLow.setHvalue(0.373);
        if (column > 18 && column < 29) scrollPaneLow.setHvalue(0.746);
        if (column > 28) scrollPaneLow.setHvalue(1.0);
    }
}