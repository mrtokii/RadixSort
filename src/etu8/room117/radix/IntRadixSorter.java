package etu8.room117.radix;

import java.util.Vector;

public class IntRadixSorter extends RadixSorter {

    private Vector<Integer> m_workingArray;
    private Vector<Vector<Integer>> m_categoriesArray;

    public IntRadixSorter() { super(); }

    /**
     * Конструктор копии
     * @param other
     */
    public IntRadixSorter(IntRadixSorter other) {
        m_maxDigits = other.m_maxDigits;
        m_currentDigit = other.m_currentDigit;

        m_workingArray = new Vector<>(other.m_workingArray);
        m_categoriesArray.clear();
        for(int i = 0; i < 10; ++i) {
            m_categoriesArray.add(
                        new Vector<>(
                            other.m_categoriesArray.elementAt(i)));
        }
    }

    /**
     * Загружает массив для сортировки внутрь класса
     * @param array - массив, который нужно отсортировать
     */
    public void load(Vector<Integer> array) {
        clear();
        m_workingArray = new Vector<>(array);
        m_maxDigits = maxDigits();
    }

    /**
     * Возвращает рабочий массив,
     * @return
     */
    public Vector<Integer> getWorkingArray() {
        return m_workingArray;
    }

    /**
     * Возвращает массив заданной колонки
     * @param cat - номер колонки для возврата
     * @return
     */
    public Vector<Integer> getCategoryArray(int cat) {
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
            Integer nextNumber = new Integer(m_workingArray.remove(0));
            System.out.println("Step: taking " + nextNumber);

            int moveTo;
            if (m_currentDigit > hasDigits(nextNumber)) {
                moveTo = 0;
            } else {
                moveTo = digitAt(nextNumber, m_currentDigit);
            }

            System.out.println("    moving to " + moveTo + " group");

            m_categoriesArray.elementAt(moveTo).add(nextNumber);
            return moveTo;

            // Если пустой, склеиваем все колонки в новый рабочий массив
        } else {
            System.out.println("Working array is empty: gluing all the shit together!");

            for (int i = 0; i < 10; ++i) {

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

        for(int i = 0; i < 10; ++i) {
            m_categoriesArray.add(new Vector<>());
        }
    }

    /**
     * Возвращает количество цифр в числе
     * @param number - число, в котором нужно посчитать количество цифр
     * @return
     */
    private int hasDigits(Integer number) {
        int digits = 1;
        Integer numberCpy = new Integer(number);

        while(numberCpy >= 10) {
            numberCpy /= 10;
            digits++;
        }

        return digits;
    }

    /**
     * Возвращает цифру на заданной позиции
     * @param number - число
     * @param pos - разряд, цифру которого нужно получить
     * @return
     */
    private int digitAt(Integer number, int pos) {
        Integer numberCpy = new Integer(number);

        if(pos > hasDigits(number))
            return -1;

        if(pos != 1) {
            numberCpy /= (int) Math.pow(10, pos-1);
        }
        return numberCpy % 10;
    }

    /**
     * Возвращает длину максимального числа в массиве
     * @return
     */
    private int maxDigits() {
        int max = 0;

        for(int i = 0; i < m_workingArray.size(); ++i) {
            if(hasDigits(m_workingArray.elementAt(i)) > max)
                max = hasDigits(m_workingArray.elementAt(i));
        }

        return max;
    }
}
