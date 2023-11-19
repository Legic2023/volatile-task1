import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger counter3 = new AtomicInteger(); // "атомарный" счетчик для строк длиной 3
    static AtomicInteger counter4 = new AtomicInteger(); // "атомарный" счетчик для строк длиной 4
    static AtomicInteger counter5 = new AtomicInteger(); // "атомарный" счетчик для строк длиной 5

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        counter3.set(0);
        counter4.set(0);
        counter5.set(0);

        Thread thread1 = new Thread(() -> {  // "палиндромный" поток
            for (String text : texts) {
                if (isPalindrome(text)) {
                    stepCount(text);
                }
            }
        });

        Thread thread2 = new Thread(() -> {  // поток для строк из одного вида символа
            for (String text : texts) {
                if (isOneChar(text)) {
                    stepCount(text);
                }
            }
        });
        Thread thread3 = new Thread(() -> { // поток для строк из символов по возрастанию
            for (String text : texts) {
                if (isAlphabetOrder(text)) {
                    stepCount(text);
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Красивых слов с длиной 3: " + counter3);
        System.out.println("Красивых слов с длиной 4: " + counter4);
        System.out.println("Красивых слов с длиной 5: " + counter5);
    }

    // функция-генератор текста
        public static String generateText (String letters,int length){
            Random random = new Random();
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < length; i++) {
                text.append(letters.charAt(random.nextInt(letters.length())));
            }
            return text.toString();
        }

    // функция проверки на "палиндромность"
        public static boolean isPalindrome (String text){
            StringBuilder stringBuilder = new StringBuilder(text);
            return stringBuilder.toString().equals(stringBuilder.reverse().toString());
        }

    // функция проверки на "односимвольность"
        public static boolean isOneChar (String text){
            return text.indexOf("a") + (text.indexOf("b") + (text.indexOf("c"))) == -2;
        }

    // функция проверки на "упорядоченность"
        public static boolean isAlphabetOrder (String text){
            char[] chars = text.toCharArray();
            Arrays.sort(chars);
            String sortedText = new String(chars);
            return text.equals(sortedText);
        }

    // функция счета в зависимости от длины строки
        public static void stepCount (String text){
            if (text.length() == 3) {
                counter3.getAndIncrement();
            }
            if (text.length() == 4) {
                counter4.getAndIncrement();
            }
            if (text.length() == 5) {
                counter5.getAndIncrement();
            }
        }

    }
