package etu8.room117.radix;

import java.util.Vector;

public class StringRadixSorter extends RadixSorter {
    private Vector<String> m_workingArray;
    private Vector<Vector<String>> m_categoriesArray;

    public StringRadixSorter() { super(); }

    /**
     * Конструктор копии
     * @param other
     */
    public StringRadixSorter(StringRadixSorter other) {
        m_maxDigits = other.m_maxDigits;
        m_currentDigit = other.m_currentDigit;

        m_workingArray = new Vector<>(other.m_workingArray);
        m_categoriesArray.clear();
        for(int i = 0; i < 37; ++i) {
            m_categoriesArray.add(
                            new Vector<>(
                                    other.m_categoriesArray.elementAt(i)));
        }
    }

    /**
     * Загружает массив для сортировки внутрь класса
     * @param array - массив, который нужно отсортировать
     */
    public void load(Vector<String> array) {
        clear();
        m_workingArray = new Vector<>(array);
        m_maxDigits = maxDigits();
    }

    /**
     * Возвращает рабочий массив,
     * @return
     */
    public Vector<String> getWorkingArray() {
        return m_workingArray;
    }

    /**
     * Возвращает массив заданной колонки
     * @param cat - номер колонки для возврата
     * @return
     */
    public Vector<String> getCategoryArray(int cat) {
        return m_categoriesArray.elementAt(cat);
    }

    /**
     * Делает один шаг сортировки
     * @return По окончании сортировки возвращает false
     */
    public int doStep() {
        if (m_currentDigit == 0) {
            System.out.println("Starting the sort!");
            m_currentDigit = 1;
        } else if (m_currentDigit > m_maxDigits) {

            System.out.println("Sorting finished!");
            m_currentDigit = 0;

            return -2;
        }

        // Если рабочий массив не пустой - переносим первый его элемент в нужную колонку
        if (!m_workingArray.isEmpty()) {
            String nextString = new String(m_workingArray.remove(0));
            System.out.println("Step: taking " + nextString);

            int moveTo;
            if (m_currentDigit > nextString.length()) {
                moveTo = 0;
                System.out.println("    bug ");
            } else {
                moveTo = matchingColumn(nextString.charAt( nextString.length() - (m_currentDigit) ));
            }

            System.out.println("    moving to " + moveTo + " group");

            m_categoriesArray.elementAt(moveTo).add(nextString);
            return moveTo;

            // Если пустой, склеиваем все колонки в новый рабочий массив
        } else {
            System.out.println("Working array is empty: gluing all the shit together!");

            for (int i = 0; i < m_categoriesArray.size(); ++i) {

                for (int j = 0; j < m_categoriesArray.elementAt(i).size(); ++j) {
                    m_workingArray.addElement(m_categoriesArray.elementAt(i).elementAt(j));
                }

                m_categoriesArray.elementAt(i).clear();

            }

            m_currentDigit++;
            return -1;
        }


    }

    /**
     * Очищает рабочее пространство класса
     */
    public void clear() {
        super.clear();

        m_workingArray = new Vector<>();
        m_categoriesArray = new Vector<>();

        for(int i = 0; i < 37; ++i) {
            m_categoriesArray.add(new Vector<>());
        }
    }

    /**
     * Возвращает длину максимального числа в массиве
     * @return
     */
    private int maxDigits() {
        int max = 0;

        for(int i = 0; i < m_workingArray.size(); ++i) {
            if(m_workingArray.elementAt(i).length() > max)
                max = m_workingArray.elementAt(i).length();
        }

        return max;
    }

    /**
     * Возвращает номер колонки для определенного символа
     * @param c - символ
     * @return
     */
    private static int matchingColumn(char c) {
        switch(c) {
            case '$': return 0;
            case '0': return 1;
            case '1': return 2;
            case '2': return 3;
            case '3': return 4;
            case '4': return 5;
            case '5': return 6;
            case '6': return 7;
            case '7': return 8;
            case '8': return 9;
            case '9': return 10;

        }

        return c-'a'+11;
    }


}
