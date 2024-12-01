package Principal;

import java.util.*;

public class tableador {
    public static void main(String[] args) {

        String input = "{10E=24, 0μ=7, 0ν=13, 18T=41, 0B=6, 10R=25, 0F=9, 10U=26, 10V=27, 50R=75, 10W=28, 54W=79, 50U=26, 50V=27, 50W=28, 35A=61, 0P=8, 15E=37, 47E=37, 0S=3, 9μ=7, 9ν=13, 23E=48, 15L=38, 9B=6, 47L=73, 9F=9, 15R=25, 47R=25, 15U=26, 23R=25, 15V=27, 35T=59, 47U=26, 15W=28, 47V=27, 91K=92, 23U=26, 47W=28, 23V=27, 23W=28, 83Q=89, 9P=21, 55W=80, 51U=76, 9S=3, 51V=27, 51W=28, 16E=39, 12E=33, 20B=45, 20C=46, 36H=64, 6μ=7, 6ν=13, 6B=6, 16R=25, 6F=9, 16U=26, 12R=25, 16V=27, 36T=62, 16W=28, 12U=26, 88T=90, 12V=27, 20S=3, 12W=28, 12X=34, 52V=77, 6P=19, 52W=28, 6S=3, 17E=40, 45B=45, 45C=71, 65E=83, 17R=25, 37Q=66, 17U=26, 17V=27, 69S=85, 17W=28, 45S=3, 81K=87, 65R=25, 65U=26, 65V=27, 53V=78, 65W=28, 53W=28}";

        String[] items = input.replaceAll("[{}]", "").split(","); // Remove the curly braces and split the items

        Map<String, Map<String, String>> table = new TreeMap<>();
        Set<String> symbols = new TreeSet<>();

        for (String item : items) {
            String[] parts = item.trim().split("="); // Split the number-symbol and content
            String numberSymbol = parts[0];
            String content = parts[1];

            String[] numberSymbolParts = numberSymbol.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"); // Split the number and
                                                                                                // symbol
            String number = numberSymbolParts[0];
            String symbol = numberSymbolParts[1];

            table.putIfAbsent(number, new TreeMap<>());
            table.get(number).put(symbol, content);
            symbols.add(symbol);
        }

        // Print the table in CSV format
        System.out.print(";");
        for (String symbol : symbols) {
            System.out.print(symbol + ";");
        }
        System.out.println();

        for (Map.Entry<String, Map<String, String>> row : table.entrySet()) {
            System.out.print(row.getKey() + ";");
            for (String symbol : symbols) {
                System.out.print(row.getValue().getOrDefault(symbol, " ") + ";");
            }
            System.out.println();
        }
    }
}
