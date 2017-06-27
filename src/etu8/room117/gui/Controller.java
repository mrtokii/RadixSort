package etu8.room117.gui;

import etu8.room117.radix.*;

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
    private ScrollPane prevMark = null;
    private Canvas prevCanvas = null;
    private int canvasLength = 3000;
    private int CanvasWidth = 168;
    int spaceFont = 4;
    int fontSize = 35;
    int kk=0;
    private Canvas canvasArea;

    /**
     * Список режимов сортировки
     */
    enum Mode {
        STRINGS, NUMBERS
    }

    ;

    private Mode mode;

    @FXML
    private TextField TextFieldTill;
    @FXML
    private TextField TextFieldNum;
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

    Vector<IntRadixSorter> intRadixSortHistory;
    Vector<StringRadixSorter> stringRadixSortHistory;

    /**
     * Устанавливает режим сортировки - числа или строки
     *
     * @param m - название режима
     */
    private void setMode(Mode m) {
        System.out.println("Setting mode to " + m.toString());

        clearSaves();
        mode = m;
    }

    /**
     * Запоминает промежуточные состояния сортировки
     *
     * @param sorter - сортировщик целых чисел
     */
    private void memorize(IntRadixSorter sorter) {
        IntRadixSorter irs = new IntRadixSorter(sorter);
        intRadixSortHistory.add(irs);
    }

    /**
     * Запоминает промедуточные состояния сортировки
     *
     * @param sorter - сортировщик строк
     */
    private void memorize(StringRadixSorter sorter) {
        StringRadixSorter srs = new StringRadixSorter(sorter);
        stringRadixSortHistory.add(srs);
    }

    /**
     * Очищает все сохраненные состояния сортировщиков
     */
    private void clearSaves() {
        System.out.println("Clearing saves!");

        intRadixSortHistory = new Vector<>();
        stringRadixSortHistory = new Vector<>();

    }

    /**
     * Отдает последний использовавшийся сортировщик целых чисел
     *
     * @return
     */
    private IntRadixSorter getIntRadixSorter() {
        if (intRadixSortHistory.isEmpty()) {
            intRadixSortHistory.add(new IntRadixSorter());
        }

        return intRadixSortHistory.lastElement();
    }

    /**
     * Отдает последний использовавшийся сортировщик строк
     *
     * @return
     */
    private StringRadixSorter getStringRadixSorter() {
        if (stringRadixSortHistory.isEmpty()) {
            stringRadixSortHistory.add(new StringRadixSorter());
        }

        return stringRadixSortHistory.lastElement();
    }

    /**
     * Отматывает состояние сортировщика назад на одну операцию
     *
     * @return
     */
    private boolean rewind() {
        if (mode == Mode.NUMBERS) {

            if (intRadixSortHistory.size() == 1) {
                return false;
            } else {
                intRadixSortHistory.removeElementAt(intRadixSortHistory.size() - 1);
                return true;
            }

        } else if (mode == Mode.STRINGS) {

            if (stringRadixSortHistory.size() == 1) {
                return false;
            } else {
                stringRadixSortHistory.removeElementAt(stringRadixSortHistory.size() - 1);
                return true;
            }

        }

        return false;
    }


    public void onNextStepButtonClicked(ActionEvent event) { // Если нажата клавиша "Следующий шаг"
        if (mode == Mode.NUMBERS) {

            IntRadixSorter irs = getIntRadixSorter();
            int col = irs.doStep();

            // Теперь обновляем
            updateWorkingArrayInteger(irs.getWorkingArray());
            for (int i = 0; i < 10; ++i) {
                updateComponentsArrayInteger(irs.getCategoryArray(i), i, irs.getCurrentDigit());
            }
            updateCurrentDigit(irs.getCurrentDigit());
            lightColumn(col);

            // Заносим состояние сортировщика в историю
            memorize(irs);

        } else if (mode == Mode.STRINGS) {

            StringRadixSorter srs = getStringRadixSorter();
            srs.doStep();

            // Теперь обновляем
            updateWorkingArrayString(srs.getWorkingArray());
            for (int i = 0; i < 37; ++i) {
                updateComponentsArrayString(srs.getCategoryArray(i), i, srs.getCurrentDigit());
            }
            updateCurrentDigit(srs.getCurrentDigit());

            // Заносим состояние сортировщика в историю
            memorize(srs);

        }
    }

    public void onPreviousStepButtonClicked(ActionEvent event) // Если нажата клавиша предыдущий шаг
    {
        if (mode == Mode.NUMBERS) {

            rewind();
            IntRadixSorter irs = getIntRadixSorter();

            // Теперь обновляем
            updateWorkingArrayInteger(irs.getWorkingArray());
            for (int i = 0; i < 10; ++i) {
                updateComponentsArrayInteger(irs.getCategoryArray(i), i, irs.getCurrentDigit());
            }
            updateCurrentDigit(irs.getCurrentDigit());

        } else if (mode == Mode.STRINGS) {

            rewind();
            StringRadixSorter srs = getStringRadixSorter();

            // Теперь обновляем
            updateWorkingArrayString(srs.getWorkingArray());
            for (int i = 0; i < 37; ++i) {
                updateComponentsArrayString(srs.getCategoryArray(i), i, srs.getCurrentDigit());
            }
            updateCurrentDigit(srs.getCurrentDigit());

        }
    }

    public void onEnterDataClicked(ActionEvent event) { // Если нажата клавиша "Ввести данные"
        boolean choice = true; // true - это строка чисел, false - строка

        System.out.println("OnEnterData:");

        String str = Text_Field.getText();
        String strArray[] = str.split(" ");
        for (int i = 0; i < strArray.length; i++) {
            if (!isDigit(strArray[i])) //Вернет true, если строка может быть преобразована в число
            {
                choice = false;
            }
        }

        // Сортируем числа
        if (choice) {
            System.out.println("    sorting numbers");

            setMode(Mode.NUMBERS);

            InitialArrayInt = new Vector<Integer>();
            for (int i = 0; i < strArray.length; ++i) {
                InitialArrayInt.add(Integer.parseInt(strArray[i]));
            }
            setCanvases(10);
            sortNumbers(InitialArrayInt);

        }

        // Сортируем строки
        else {
            System.out.println("    sorting strings");

            setMode(Mode.STRINGS);

            InitialArrayString = new Vector<String>();
            for (int i = 0; i < strArray.length; ++i) {
                InitialArrayString.add(strArray[i]);
            }
            setCanvases(36);
            sortStrings(InitialArrayString);
            updateWorkingArrayString(InitialArrayString);
            updateComponentsArrayString(InitialArrayString, 33, 3);
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
        IntRadixSorter irs = getIntRadixSorter();
        irs.load(arr);

        memorize(irs);

        updateCurrentDigit(1);
        updateWorkingArrayInteger(arr);

        System.out.println("sorting numbers " + arr.toString());
    }

    public void sortStrings(Vector<String> arr) {
        StringRadixSorter srs = getStringRadixSorter();
        srs.load(arr);

        memorize(srs);

        updateCurrentDigit(1);
        updateWorkingArrayString(arr);

        System.out.println("sorting strings " + arr.toString());
    }

    public void onClearButtonClicked(ActionEvent event) { // Если нажата клавиша "Сброс"
        MainHBox.getChildren().remove(0, vectorCanvas.size());

        clearSaves();
        updateCurrentDigit(0);
    }

    public void onFinishSortingButtonClicked(ActionEvent event) { // Если нажата клавиша "Завершить сортировку"
        if (mode == Mode.NUMBERS) {

            IntRadixSorter irs = getIntRadixSorter();
            while (irs.doStep() != -2) ;
            lightColumn(-1);

            // Теперь обновляем
            updateWorkingArrayInteger(irs.getWorkingArray());
            for (int i = 0; i < 10; ++i) {
                updateComponentsArrayInteger(irs.getCategoryArray(i), i, irs.getCurrentDigit());
            }
            updateCurrentDigit(0);
        } else if (mode == Mode.STRINGS) {
            StringRadixSorter srs = getStringRadixSorter();
            while (srs.doStep() != -2) ;

            // Теперь обновляем
            updateWorkingArrayString(srs.getWorkingArray());
            for (int i = 0; i < 37; ++i) {
                updateComponentsArrayString(srs.getCategoryArray(i), i, srs.getCurrentDigit());
            }
            updateCurrentDigit(0);
        }
    }

    public void setCanvases(int numCanvases) { // Установить количество столбцов

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
            String name;
            VBox vBox = new VBox();//Создали вертикальный слой
            Label label = new Label();
            if (i < 10) {
                Integer sign = i;
                name = sign.toString();
            } else {
                name = sym.toString();
                char ch = (char) sym;
                int n = (int) ch;
                ++n;
                ch = (char) n;
                sym = (Character) ch;
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
            Canvas canvas = new Canvas(CanvasWidth, canvasLength);//Создали холст
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

    public void setCanvasArea() {
        canvasArea = new Canvas(1880, 3000);
        scrollAreaArray.setContent(canvasArea);
    }

    public void onFillingButtonClicked(ActionEvent event) { // Если нажата клавиша "Заполнить"
        Text_Field.clear();
        final Random random = new Random();
        String strNum = TextFieldNum.getText();
        String strTill = TextFieldTill.getText();
        int count = Integer.parseInt(strNum);
        int edge = Integer.parseInt(strTill);
        for (int i = 0; i < count; ++i) {
            Integer num = random.nextInt(edge);
            Text_Field.appendText(num.toString() + " ");
        }
    }

    public void updateCurrentDigit(int digit) {
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

    public void updateWorkingArrayInteger(Vector<Integer> array) { // Заполняет Text_Area данными из вектора
        Vector<String> strings = new Vector<String>();
        for (int i = 0; i < array.size(); i++) {
            Integer num = array.get(i);
            strings.add(num.toString());
        }
        updateWorkingArrayString(strings);
    }

    public void updateWorkingArrayString(Vector<String> array) {
        GraphicsContext gc = canvasArea.getGraphicsContext2D();
        gc.clearRect(0, 0, 1900, 3000);
        gc.setFont(new Font("Consolas", fontSize));//Courier New
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.LEFT);
        int x = 5;
        int y = 30;
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i);
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
    public void updateComponentsArrayInteger(Vector<Integer> array, int column, int digit) {
            GraphicsContext gc = vectorCanvas.get(column).getGraphicsContext2D();
            setParamFont(gc,column);
            gc.setTextAlign(TextAlignment.RIGHT);
            double scrollMiss = 1.0;
            int y = canvasLength - spaceFont;
            int x = CanvasWidth - 5;
            for (int i = 0; i < array.size(); i++) {
                Integer num = array.get(i);
                String str = num.toString();
                x = CanvasWidth - 5;
                for (int j = str.length() - 1; j >= 0; j--) {
                    char ch = str.charAt(j);
                    String s = Character.toString(ch);
                    if (j == str.length() - digit) {
                        gc.setStroke(Color.RED);
                        gc.setFill(Color.RED);
                        gc.fillText(s, x, y);
                        x = x - 17; //Промежуток между символами
                        gc.setStroke(Color.GREEN);
                        gc.setFill(Color.GREEN);
                    } else {
                        gc.fillText(s, x, y);
                        x = x - 17;
                    }
                }
                y = y - fontSize - spaceFont;
                if (i % 10 == 0 && i > 9) {
                    scrollMiss = scrollMiss - 0.15;
                }
                vectorScrollPane.get(column).setVvalue(scrollMiss);
            }
    }

    public void updateComponentsArrayString(Vector<String> array, int column, int digit) {
        GraphicsContext gc = vectorCanvas.get(column).getGraphicsContext2D();
        setParamFont(gc, column);
        gc.setTextAlign(TextAlignment.LEFT);
        double scrollMiss = 1.0;
        if (column > 9 && column < 20) scrollPaneLow.setHvalue(0.385);
        if (column > 19 && column < 30) scrollPaneLow.setHvalue(0.775);
        if (column > 29) scrollPaneLow.setHvalue(1.0);
        int y = canvasLength - spaceFont - 5;
        int x = 5;
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i);
            x = 5;
            for (int j = 0; j <= str.length() - 1; j++) {
                char ch = str.charAt(j);
                String s = Character.toString(ch);
                if (j == digit - 1) {
                    gc.setStroke(Color.RED);
                    gc.setFill(Color.RED);
                    gc.fillText(s, x, y);
                    x = x + 18; //Промежуток между символами
                    gc.setStroke(Color.GREEN);
                    gc.setFill(Color.GREEN);
                } else {
                    gc.fillText(s, x, y);
                    x = x + 18;
                }
            }
            y = y - fontSize - spaceFont;
            if (i % 10 == 0 && i > 9) {
                scrollMiss = scrollMiss - 0.15;
            }
            vectorScrollPane.get(column).setVvalue(scrollMiss);
        }
    }

    public void setParamFont(GraphicsContext gc, int column)
    {
        gc.clearRect(0, 0, CanvasWidth, canvasLength);
        gc.setFont(new Font("Consolas", fontSize));//Courier New
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.GREEN);

    }

    public void lightColumn(int column) { //Подсветка столбца
        if(column < 0) {

            if(prevMark != null) {
                prevMark.setStyle("-fx-border-color: white");
            }
            prevMark = null;
            return;

        } else {

            if(mode == Mode.STRINGS && column > 36)
                return;

            if(mode == Mode.NUMBERS && column > 9)
                return;

            if (prevMark != null) {
                prevMark.setStyle("-fx-border-color: white");
            }
            vectorScrollPane.get(column).setStyle("-fx-border-color: red");
            prevMark = vectorScrollPane.get(column);
        }




    }
}