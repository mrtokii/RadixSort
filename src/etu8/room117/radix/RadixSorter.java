package etu8.room117.radix;

import java.util.Vector;

public abstract class RadixSorter {
    protected int m_currentDigit;
    protected int m_maxDigits;

    /**
     * Конструктор класса
     */
    public RadixSorter() {
        clear();
    }

    /**
     * Возвращает текущий разряд сортировки
     * @return
     */
    public int getCurrentDigit() {
        return m_currentDigit;
    }

    /**
     * Делает один шаг сортировки
     * @return Возвращает номер колонки, в которую был добавлен элемент или -1,
     * если колонки склеиваются в рабочий массив, либо -2, если сортировка закончилсь
     */
    public abstract int doStep();

    /**
     * Выполняет сортировку до конца
     */
    public void sort() {
        while(doStep() != -2) { }
    }

    /**
     * Очищает рабочее пространство класса
     */
    public void clear() {
        m_currentDigit = 0;
        m_maxDigits = 0;
    }
}
