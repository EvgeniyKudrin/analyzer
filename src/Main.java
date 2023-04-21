import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        Thread textGenerator = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 100000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        textGenerator.start();

        Thread thread1 = new Thread(() -> {
            char letter = 'a';
            int maxA = findMaxCharCount(queue1, letter);
            System.out.println("Максимальное количество " + letter + " во всём тексте: " + maxA);
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            char letter = 'b';
            int maxB = findMaxCharCount(queue2, letter);
            System.out.println("Максимальное количество " + letter + " во всём тексте: " + maxB);
        });
        thread2.start();

        Thread thread3 = new Thread(() -> {
            char letter = 'c';
            int maxC = findMaxCharCount(queue3, letter);
            System.out.println("Максимальное количество " + letter + " во всём тексте: " + maxC);
        });
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }

    public static int findMaxCharCount(BlockingQueue<String> queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10000; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " был прерван");
            return -1;
        }
        return max;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}