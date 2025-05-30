import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class JsonCreator {

    public static void generateJsonFile(int numberOfEntries, String outputFilePath) {

        List<Person> dataArray = new ArrayList<>();
        for (int i = 0; i < numberOfEntries; i++) {
            
            String firstName = generateRandomAvengersName();
            int age = (int) (Math.random() * 37) + 18; 
            int children = (int) (Math.random() * 5);
            int salary = ((int) (Math.random() * 10) + 1) * 1000; // Salary between 1000 and 10000
            String timestamp = new Date().toString(); // Current timestamp
            String processMonth = "202505"; // Static for this example
            String processDay = String.format("%02d", (int) (Math.random() * 31) + 1); // Day between 01 and 31

            dataArray.add(new Person(firstName, age, children, salary, timestamp, processMonth, processDay));
        }

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n \"data\": [\n");
        for (int i = 0; i < dataArray.size(); i++) {
            Person person = dataArray.get(i);
            jsonBuilder.append("  {\n");
            jsonBuilder.append("    \"firstName\": \"").append(person.getFirstName()).append("\",\n");
            jsonBuilder.append("    \"age\": ").append(person.getAge()).append(",\n");
            jsonBuilder.append("    \"children\": ").append(person.getChildren()).append(",\n");
            jsonBuilder.append("    \"salary\": ").append(person.getSalary()).append(",\n");
            jsonBuilder.append("    \"timestamp\": \"").append(person.getTimestamp()).append("\",\n");
            jsonBuilder.append("    \"processMonth\": \"").append(person.getProcessMonth()).append("\",\n");
            jsonBuilder.append("    \"processDay\": \"").append(person.getProcessDay()).append("\"\n");
            jsonBuilder.append("  }");
            if (i < dataArray.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append(" ]\n}");

        try {
            FileWriter fileWriter = new FileWriter(outputFilePath);
            fileWriter.write(jsonBuilder.toString());
            fileWriter.close();
            System.out.println("JSON file created successfully at " + outputFilePath);
        } catch (java.io.IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }

    private static String generateRandomAvengersName() {
        String[] names = {"Tony", "Steve", "Natasha", "Bruce", "Clint", "Thor", "Wanda", "Vision", "Peter", "Carol", "Sam", "Bucky", "Yelena", "John", "Bob", "Eve", "Alexei"};
        int randomIndex = (int) (Math.random() * names.length);
        return names[randomIndex];
    }

    public static void main(String[] args) {
        int numberOfEntries = 2000000; // Specify the number of entries to generate
        String outputFilePath = "C:/Users/pramosi/Desktop/MyFiles/workspace_test/W-scala/DataFrame/src/main/resources/data_aux.json"; // Output file path
        generateJsonFile(numberOfEntries, outputFilePath);
    }

    
}