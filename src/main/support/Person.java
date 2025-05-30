public class Person implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private int age;
    private int children;
    private int salary;
    private String timestamp;
    private String processMonth;
    private String processDay;

    // Constructor
    public Person(String firstName, int age, int children, int salary, String timestamp, String processMonth, String processDay) {
        this.firstName = firstName;
        this.age = age;
        this.children = children;
        this.salary = salary;
        this.timestamp = timestamp;
        this.processMonth = processMonth;
        this.processDay = processDay;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProcessMonth() {
        return processMonth;
    }

    public void setProcessMonth(String processMonth) {
        this.processMonth = processMonth;
    }

    public String getProcessDay() {
        return processDay;
    }

    public void setProcessDay(String processDay) {
        this.processDay = processDay;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", age=" + age +
                ", children=" + children +
                ", salary=" + salary +
                ", timestamp='" + timestamp + '\'' +
                ", processMonth='" + processMonth + '\'' +
                ", processDay='" + processDay + '\'' +
                '}';
    }
}
