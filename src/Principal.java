import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Principal {
    public static void main(String[] args) {
        List<Funcionario> employees = new ArrayList<>();

        employees.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        employees.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        employees.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        employees.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        employees.add(new Funcionario("Alice", LocalDate.of(1995, 1, 05), new BigDecimal("2234.68"), "Recepcionista"));
        employees.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        employees.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        employees.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        employees.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        employees.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        List<Integer> months = Arrays.asList(10, 12);

        removeEmployee(employees, "João");
        printEmployees(employees);
        raiseSalary(employees, "0.10");
        mapEmployeesByRole(employees);
        printEmployeeByRole(mapEmployeesByRole(employees));
        printEmployeeByBirthMonth(mapEmployeesByRole(employees), months);
        printOldestEmployee(employees);
        printSortedEmployees(employees);
        printSumAllSalaries(employees);
        printAmountOfMinSalary(employees);
    }

    public static List<Funcionario> removeEmployee(List<Funcionario> list, String name) {
        list.removeIf(employee -> employee.getNome().equals(name));
        return list;
    }

    public static void printEmployees(List<Funcionario> list) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        DecimalFormat decimalFormat = new DecimalFormat("#, ##0.00");

        for(Funcionario employee: list) {
            System.out.println("Nome:" + employee.getNome());
            System.out.println("Data Nascimento:" + employee.getDataNascimento().format(formatter));
            System.out.println("Salário:" + decimalFormat.format(employee.getSalario()));
            System.out.println("Função:" + employee.getFuncao());
        }
    }

    public static List<Funcionario> raiseSalary(List<Funcionario> employees, String value) {
        BigDecimal raise = new BigDecimal(value);

        for(Funcionario employee: employees) {
            BigDecimal newSalary = employee.getSalario().multiply(BigDecimal.ONE.add(raise)).setScale(2, RoundingMode.HALF_UP);
            employee.setSalario(newSalary);
        }

        return employees;
    }

    public static Map<String, List<Funcionario>> mapEmployeesByRole(List<Funcionario> employees) {
        //referência: https://stackoverflow.com/questions/40772997/how-to-convert-listv-into-mapk-listv-with-java-8-streams-and-custom-list
        
        return employees.stream()
            .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }
    
    public static void printEmployeeByRole(Map<String, List<Funcionario>> map) {
        for(Map.Entry<String,List<Funcionario>> entry: map.entrySet()) {
            List<Funcionario> employees = entry.getValue();
            System.out.println("Função: " + entry.getKey());

            for(Funcionario employee: employees) {
                System.out.println("Nome: " + employee.getNome());
            }
        }
    }

    public static void printEmployeeByBirthMonth(Map<String, List<Funcionario>> map, List<Integer> months) {
        for(Map.Entry<String,List<Funcionario>> entry: map.entrySet()) {
            List<Funcionario> employees = entry.getValue();
            
            for(Funcionario employee: employees) {
                if(months.contains(employee.getDataNascimento().getMonthValue())) {
                    System.out.println("Função: " + entry.getKey());
                    System.out.println("Nome: " + employee.getNome());
                }
            }
        }
    }

    public static void printOldestEmployee(List<Funcionario> employees) {
        //referência https://stackoverflow.com/questions/39791318/how-to-get-the-earliest-date-of-a-list-in-java

        int currentYear = LocalDate.now().getYear();

        Optional<Funcionario> oldestEmployee = employees.stream()
            .min(Comparator.comparing(Funcionario::getDataNascimento));

        if(oldestEmployee.isPresent()) {
            System.out.println("O funcionário mais velho é " + oldestEmployee.get().getNome() 
                + " com " 
                + (currentYear -  oldestEmployee.get().getDataNascimento().getYear())
                + " anos"
            );
        } else {
            System.out.println("Não há um funcionário mais velho");
        }
    }

    public static void printSortedEmployees(List<Funcionario> employees) {
        //referência: https://stackoverflow.com/questions/40517977/sorting-a-list-with-stream-sorted-in-java

        List<Funcionario> sortedEmployees = employees.stream()
            .sorted((e1, e2) -> e1.getNome().compareTo(e2.getNome())).collect(Collectors.toList());

        for(Funcionario employee: sortedEmployees) {
            System.out.println(employee.getNome());
        }
    }

    public static void printSumAllSalaries(List<Funcionario> employees) {
        BigDecimal counter = BigDecimal.ZERO;

        for(Funcionario employee: employees) {
            counter = counter.add(employee.getSalario());
        }

        System.out.println(counter);
    }

    public static void printAmountOfMinSalary(List<Funcionario> employees) {
        BigDecimal minSalary = new BigDecimal("1212.00");

        for(Funcionario employee: employees) {
            System.out.println("Nome do funcionário " + employee.getNome());
            System.out.println("Quantidade de salários mínimos " + employee.getSalario().divide(minSalary, RoundingMode.HALF_UP));
        }
    }
}
