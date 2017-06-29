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

/**
 * Created by Roman Zhuravlev on 29.06.2017.
 */
public class MWController {

    Vector<Integer> InitialArrayInt;
    Vector<String> InitialArrayString;

    Vector<IntRadixSorter> intRadixSortHistory;
    Vector<StringRadixSorter> stringRadixSortHistory;

    Controller control;

    /**
     * Список режимов сортировки
     */
    public enum Mode {
        STRINGS, NUMBERS
    }

    ;

    public Mode mode;



    /**
     * Устанавливает режим сортировки - числа или строки
     *
     * @param m - название режима
     */
    public void setMode(Mode m) {
        System.out.println("Setting mode to " + m.toString());
        clearSaves();
        mode = m;
    }
    /**
     * Устанавливает контроллер для данного класса
     *
     * @param с - контроллер
     */
    public void setControl(Controller c){
        this.control = c;
    }
    /**
     * Запоминает промежуточные состояния сортировки
     *
     * @param sorter - сортировщик целых чисел
     */
    public void memorize(IntRadixSorter sorter) {
        IntRadixSorter irs = new IntRadixSorter(sorter);
        intRadixSortHistory.add(irs);
    }

    /**
     * Запоминает промедуточные состояния сортировки
     *
     * @param sorter - сортировщик строк
     */
    public void memorize(StringRadixSorter sorter) {
        StringRadixSorter srs = new StringRadixSorter(sorter);
        stringRadixSortHistory.add(srs);
    }

    /**
     * Очищает все сохраненные состояния сортировщиков
     */
    public void clearSaves() {
        System.out.println("Clearing saves!");

        intRadixSortHistory = new Vector<>();
        stringRadixSortHistory = new Vector<>();

    }

    /**
     * Отдает последний использовавшийся сортировщик целых чисел
     *
     * @return
     */
    public IntRadixSorter getIntRadixSorter() {
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
    public StringRadixSorter getStringRadixSorter() {
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
    public boolean rewind() {
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

    public int onNextStepButtonClicked(){
        int col=-1;
        if (mode == Mode.NUMBERS) {

            IntRadixSorter irs = getIntRadixSorter();

            // Заносим состояние сортировщика в историю
            memorize(irs);

            irs = getIntRadixSorter();
            col = irs.doStep();

            // Теперь обновляем
            control.updateWorkingArrayInteger(irs.getWorkingArray());
            for (int i = 0; i < 10; ++i) {
                control.updateComponentsArrayInteger(irs.getCategoryArray(i), i, irs.getCurrentDigit());
            }
            control.updateCurrentDigit(irs.getCurrentDigit());
            control.lightColumn(col);

        } else if (mode == Mode.STRINGS) {

            StringRadixSorter srs = getStringRadixSorter();

            // Заносим состояние сортировщика в историю
            memorize(srs);
            srs = getStringRadixSorter();

            col = srs.doStep();
            // Теперь обновляем
            control.updateWorkingArrayString(srs.getWorkingArray());
            for (int i = 0; i < 37; ++i) {
                control.updateComponentsArrayString(srs.getCategoryArray(i), i, srs.getCurrentDigit());
            }
            control.updateCurrentDigit(srs.getCurrentDigit());
            control.lightColumn(col);
        }
        return col;
    }

    public void onPreviousStepButtonClicked() // Если нажата клавиша предыдущий шаг
    {
        if (mode == Mode.NUMBERS) {

            rewind();
            IntRadixSorter irs = getIntRadixSorter();

            // Теперь обновляем
            control.updateWorkingArrayInteger(irs.getWorkingArray());
            for (int i = 0; i < 10; ++i) {
                control.updateComponentsArrayInteger(irs.getCategoryArray(i), i, irs.getCurrentDigit());
            }
            control.updateCurrentDigit(irs.getCurrentDigit());

        } else if (mode == Mode.STRINGS) {

            rewind();
            StringRadixSorter srs = getStringRadixSorter();

            // Теперь обновляем
            control.updateWorkingArrayString(srs.getWorkingArray());
            for (int i = 0; i < 37; ++i) {
                control.updateComponentsArrayString(srs.getCategoryArray(i), i, srs.getCurrentDigit());
            }
            control.updateCurrentDigit(srs.getCurrentDigit());
        }

        control.lightColumn(-1);

    }

    public int onEnterDataClicked(String str) { // Если нажата клавиша "Ввести данные"
        boolean choice = true; // true - это строка чисел, false - строка

        System.out.println("OnEnterData:");

        String strArray[] = str.split(" ");
        for (int i = 0; i < strArray.length; i++) {
            if(strArray[i].length()>8)   return 1;
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
            control.setCanvases(10);
            sortNumbers(InitialArrayInt);

        }

        // Сортируем строки
        else {
            System.out.println("    sorting strings");

            setMode(Mode.STRINGS);
            int maxlength=0;
            for (int i = 0; i < strArray.length ; i++) {
                if(strArray[i].length()>maxlength) maxlength=strArray[i].length();
            }
            InitialArrayString = new Vector<String>();
            for (int i = 0; i < strArray.length; ++i) {
                int countLength = strArray[i].length();
                while(maxlength!=countLength)
                {
                    strArray[i]=strArray[i]+'$';
                    ++countLength;
                }
                InitialArrayString.add(strArray[i].toLowerCase());
            }
            for (int i = 0; i <InitialArrayString.size() ; i++) {
                System.out.println(InitialArrayString.get(i));
            }

            control.setCanvases(37);
            sortStrings(InitialArrayString);
        }
    return 0;
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

        control.updateCurrentDigit(1);
        control.updateWorkingArrayInteger(arr);

        System.out.println("sorting numbers " + arr.toString());
    }


    public void sortStrings(Vector<String> arr) {
        StringRadixSorter srs = getStringRadixSorter();
        srs.load(arr);

        memorize(srs);

        control.updateCurrentDigit(1);
        control.updateWorkingArrayString(arr);

        System.out.println("sorting strings " + arr.toString());
    }

    public void onFinishSortingButtonClicked() { // Если нажата клавиша "Завершить сортировку"
        if (mode == Mode.NUMBERS) {

            IntRadixSorter irs = getIntRadixSorter();
            while (irs.doStep() != -2) {

                // Заносим состояние сортировщика в историю
                memorize(irs);
            }
            control.lightColumn(-1);

            // Теперь обновляем
            control.updateWorkingArrayInteger(irs.getWorkingArray());
            for (int i = 0; i < 10; ++i) {
                control.updateComponentsArrayInteger(irs.getCategoryArray(i), i, irs.getCurrentDigit());
            }
            control.updateCurrentDigit(0);
        } else if (mode == Mode.STRINGS) {
            StringRadixSorter srs = getStringRadixSorter();
            while (srs.doStep() != -2)  {

                // Заносим состояние сортировщика в историю
                memorize(srs);
            }

            // Теперь обновляем
            control.updateWorkingArrayString(srs.getWorkingArray());
            for (int i = 0; i < 37; ++i) {
                control.updateComponentsArrayString(srs.getCategoryArray(i), i, srs.getCurrentDigit());
            }
            control.updateCurrentDigit(0);
        }
    }
}
