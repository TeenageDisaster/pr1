import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

class Person {
    private int id;
    private String name;
    private int age;

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{id=" + id + ", name='" + name + "', age=" + age + '}';
    }
}

class PersonService {
    private final List<Person> persons = new ArrayList<>();

    public void addPerson(Person person) {
        persons.add(person);
    }

    public Optional<Person> findPersonById(int id) {
        return persons.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Person> getAllPersons() {
        return persons;
    }
}
class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PersonService personService = new PersonService();

        while (true) {
            System.out.println("Оберіть опцію: \n1 - Текстовий аналізатор \n2 - Stream API \n3 - Optional (пошук користувача) \n4 - Управління особами \n5 - Вийти");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1 -> textAnalyzer(scanner);
                case 2 -> streamApiDemo(scanner);
                case 3 -> optionalDemo(scanner, personService);
                case 4 -> managePersons(scanner, personService);
                case 5 -> {
                    System.out.println("До побачення!");
                    return;
                }
                default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void textAnalyzer(Scanner scanner) {
        System.out.println("Введіть текст:");
        String text = scanner.nextLine();

        BiFunction<String, String, Boolean> findWord = (t, w) -> t.contains(w);
        BiFunction<String, String, String> replaceWord = (t, w) -> t.replaceAll(w, "***");
        Function<String, Long> countWords = t -> (long) t.split("\\s+").length;

        System.out.println("Оберіть операцію: 1 - Пошук слова, 2 - Замінити слова, 3 - Підрахунок слів:");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("Введіть слово для пошуку:");
                String word = scanner.nextLine();
                System.out.println(findWord.apply(text, word) ? "Слово знайдено!" : "Слово не знайдено.");
            }
            case 2 -> {
                System.out.println("Введіть слово для заміни:");
                String word = scanner.nextLine();
                System.out.println("Результат: " + replaceWord.apply(text, word));
            }
            case 3 -> System.out.println("Кількість слів: " + countWords.apply(text));
            default -> System.out.println("Невірна операція!");
        }
    }

    private static void streamApiDemo(Scanner scanner) {
        System.out.println("Введіть текст:");
        String text = scanner.nextLine();
        List<String> words = Arrays.asList(text.split("\\s+"));

        System.out.println("Слова у тексті:");
        words.forEach(System.out::println);

        System.out.println("Введіть літеру для фільтрації слів:");
        char letter = scanner.next().charAt(0);
        List<String> filteredWords = words.stream()
                .filter(word -> word.startsWith(String.valueOf(letter)))
                .collect(Collectors.toList());
        System.out.println("Відфільтровані слова: " + filteredWords);

        List<String> sortedWords = words.stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Відсортовані слова: " + sortedWords);

        long uniqueWordCount = words.stream().distinct().count();
        System.out.println("Кількість унікальних слів: " + uniqueWordCount);
    }

    private static void optionalDemo(Scanner scanner, PersonService personService) {
        System.out.println("Введіть ID користувача:");
        int id = scanner.nextInt();

        personService.findPersonById(id)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Користувача з таким ID не знайдено.")
                );
    }

    private static void managePersons(Scanner scanner, PersonService personService) {
        System.out.println("Оберіть дію: 1 - Додати, 2 - Знайти, 3 - Показати всіх осіб:");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("Введіть ID, ім'я та вік через пробіл:");
                int id = scanner.nextInt();
                String name = scanner.next();
                int age = scanner.nextInt();
                personService.addPerson(new Person(id, name, age));
                System.out.println("Особа додана.");
            }
            case 2 -> {
                System.out.println("Введіть ID:");
                int id = scanner.nextInt();
                personService.findPersonById(id)
                        .ifPresentOrElse(System.out::println, () -> System.out.println("Особа не знайдена."));
            }
            case 3 -> {
                List<Person> persons = personService.getAllPersons();
                if (persons.isEmpty()) {
                    System.out.println("Список осіб порожній.");
                } else {
                    persons.forEach(System.out::println);
                }
            }
            default -> System.out.println("Невірна команда!");
        }
    }
}
