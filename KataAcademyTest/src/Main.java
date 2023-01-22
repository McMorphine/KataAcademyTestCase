import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
/* Тестовое задание для Kata Academy, курс Java Developer
        Автор: Бычков Семен Антонович, sembychkov@mail.ru
 */

         import java.io.IOException;
         import java.util.Arrays;
         import java.util.List;
         import java.util.Scanner;
         import java.util.stream.Collectors;
         import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            while (true) {
                System.out.println("Введите выражение для расчёта:  ");

                Scanner read = new Scanner(System.in);
                String CalcLine = read.nextLine();

                System.out.println(calc(CalcLine)); //вызов основного метода в теле метода main
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка при введении выражения");
            System.exit(0);
        }


    }

    public static String calc(String input)  { //основный метод Калькулятора
        char[] CharsCalcLine = input.toCharArray();
        int i = 0;
        String result = "";
        String x = "", operator = "", y = ""; // x,y - левая и правая части выражения "нормального" вида x+y
        String CalcDataInType="";//переменная строка, принимающая значние "Arabic"/"Roman" при проверке первого символа строки
        if (Character.isDigit(CharsCalcLine[0])) {
            CalcDataInType = "Arabic";
        }
        else
        if (CheckIfLetterIsRome(String.valueOf(CharsCalcLine[0]).toUpperCase()))
            CalcDataInType = "Roman";
        else
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("Выражение введено неверно");
                System.exit(0);
            }

        switch (CalcDataInType) {
            case "Arabic": //Расчёт в случае если строка первично опредлена как выражение с арабскими цифрами
                while (Character.isDigit(CharsCalcLine[i]) && i < CharsCalcLine.length) {
                    x += Character.toString(CharsCalcLine[i]);
                    i++;
                }
                if (CheckForOperatorLegal(Character.toString(CharsCalcLine[i])) && i < CharsCalcLine.length)
                    operator += Character.toString(CharsCalcLine[i]);
                i++;
                for (i = i; i < CharsCalcLine.length; i++)
                    if (Character.isDigit(CharsCalcLine[i])) {
                        y += Character.toString(CharsCalcLine[i]);
                    }
                if (Integer.parseInt(x) <= 10&&y!="" && Integer.parseInt(y) <= 10 && CheckForOperatorLegal(operator)) {
                    result = Integer.toString(getArabicCalcuclation(x, y, operator));
                } else
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Ошибка при введении выражения");
                        System.exit(0);
                    }
                break;
            case "Roman":  {     //Расчёт в случае если строка первично опредлена как выражение с римскими цифрами

                while (CheckIfLetterIsRome(Character.toString(CharsCalcLine[i])) && i < CharsCalcLine.length) {
                    x += Character.toString(CharsCalcLine[i]);
                    i++;
                }
                x=Integer.toString(getArabicFromRome(x));
                if (CheckForOperatorLegal(Character.toString(CharsCalcLine[i])) && i < CharsCalcLine.length)
                    operator += Character.toString(CharsCalcLine[i]);
                i++;
                for (i = i; i < CharsCalcLine.length; i++)
                    if (CheckIfLetterIsRome(Character.toString(CharsCalcLine[i]))) {
                        y += Character.toString(CharsCalcLine[i]);
                    }
                    else
                        try {
                            throw new IOException();
                        } catch (IOException e) {
                            System.out.println("Не корректный ввод римского числа");
                            System.exit(0);
                        }
                y=Integer.toString(getArabicFromRome(y));
                if (Integer.parseInt(x) <= 10 && Integer.parseInt(y) <= 10 && CheckForOperatorLegal(operator)) {
                    result = Integer.toString(getArabicCalcuclation(x, y, operator));
                    result = getRomeFromArabic(result);
                } else
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Ошибка при введении выражения");
                        System.exit(0);
                    }
            }
            break;
        }
        return result;
    }
    //метод для расчёта выражение определённого в виде двух арабских чисел (x и y) и оператора operator
    // обозначающего одно из разрешённых мат.вычислений между ними
    public static int getArabicCalcuclation (String x, String y, String operator){
        int result=0;
        switch(operator)
        {
            case "+":
                result=Integer.parseInt(x)+Integer.parseInt(y);
                break;
            case "-":
                result=Integer.parseInt(x)-Integer.parseInt(y);
                break;
            case "*":
                result=Integer.parseInt(x)*Integer.parseInt(y);
                break;
            case "/":
                if (Integer.parseInt(y)==0) {
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Деление на ноль запрещено");
                        System.exit(0);
                    }
                }
                else result = Integer.parseInt(x) / Integer.parseInt(y);
                break;
        }
        return result;
    }

    //метод для проверки символа на принадлежность к группе символов определённых как математические операторы
    // (изначально список эквивалентных символов планирвоаллось реализовать так же через Enum, но поковырявшись пришёл к выводу что трудозатраты
    // на такой код не будут стоить того. Даже при теоретическом расширении функционала программы будет легче добавлять операторы в простой Array
    public static boolean CheckForOperatorLegal(String operator)
    {
        String[] LegalOperators={"+","-","*","/"};
        boolean result= Arrays.asList(LegalOperators).contains(operator);
        if (result!=true) {
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("Введён некорректный оператор");
                System.exit(0);
            }
        }
        return result;

    }
    //метод для проверки символа на принадлежность к группе символов определённых как цифры римской СС
    public static boolean CheckIfLetterIsRome(String Letter)
    {

        String str = Letter.toUpperCase();
        List<RomanNumeral> values   = RomanNumeral.getListOfValues();
        boolean result=false;
        for (int i = 0; i < values.size(); i++) {
            RomanNumeral s = values.get(i);
            if (str.startsWith(s.name()))
            {
                result=true;}
        }
        return result;
    }
    //метод для получение целого числа из последовательности символов (String x), определённых как запись числа в римской СС
    // код является модифицированной версией решения с сайта https://www.baeldung.com/java-convert-roman-arabic
    public static int getArabicFromRome(String x)
    {
        String romanNumeral = x.toUpperCase();
        int result = 0;
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        int i = 0;
        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }
        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException("Не корректный ввод римского числа");
        }
        return result;
    }
    //метод для получение последовательности символов (String sb), являющихся записью целого числа в римской СС
    // код является модифицированной версией решения с сайта https://www.baeldung.com/java-convert-roman-arabic
    public static String getRomeFromArabic(String result)
    {
        int number = Integer.parseInt(result);
        if ((number <= 0)) {
            throw new IllegalArgumentException("Попытка получить ноль или отрицательное  число в римской СС");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
    //перечисление символов, определяемых в дальнейшем как римские цифры
    // код является модифицированной версией решения с сайта https://www.baeldung.com/java-convert-roman-arabic
    enum RomanNumeral { //https://www.baeldung.com/java-convert-roman-arabic
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
        public static List<RomanNumeral> getListOfValues() {
            return Arrays.stream(values())
                    .collect(Collectors.toList());
        }
        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }
}




