package com.michaelsnowden.unit_converter;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author michael.snowden
 */
public class QualifiedNumber {
    private final double value;
    private final Map<String, Fraction> units;

    public QualifiedNumber(double value, Map<String, Fraction> units) {
        this.units = flatten(units.entrySet()
                .stream()
                .filter(e -> e.getValue().getN() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        this.value = value * convertToSi();
    }

    private double convertToSi() {
        double result = 1.0;
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            for (Map.Entry<String, Fraction> entry : units.entrySet()) {
                String unit = entry.getKey();
                Fraction power = entry.getValue();
                ResultSet resultSet;
                resultSet = statement.executeQuery("SELECT scalar, si FROM base_unit WHERE " +
                        "symbol = '" + unit + "'");
                if (resultSet.next()) {
                    double scalar = resultSet.getDouble("scalar");
                    String si = resultSet.getString("si");
                    units.remove(unit);
                    units.put(si, power);
                    result *= Math.pow(scalar, power.getDouble());
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + getClass().getClassLoader().getResource("unit_converter" +
                ".db").getPath());
    }

    private Map<String, Fraction> flatten(Map<String, Fraction> map) {
        Connection connection = null;
        try {
            Map<String, Fraction> flattened = new HashMap<>();
            connection = getConnection();
            Statement statement = connection.createStatement();
            for (Map.Entry<String, Fraction> entry : map.entrySet()) {
                String unit = entry.getKey();
                Fraction power = entry.getValue();
                ResultSet resultSet;
                resultSet = statement.executeQuery("SELECT * FROM si_derived_unit WHERE " +
                        "derived_symbol = '" + unit + "'");
                if (resultSet.next()) {
                    resultSet = statement.executeQuery("SELECT base_symbol, numerator, denominator FROM " +
                            "component WHERE derived_symbol = '" + unit + "'");
                    while (resultSet.next()) {
                        String baseSymbol = resultSet.getString("base_symbol");
                        int numerator = resultSet.getInt("numerator");
                        int denominator = resultSet.getInt("denominator");
                        flattened.put(baseSymbol, new Fraction(numerator, denominator));
                    }
                } else {
                    flattened.put(unit, power);
                }
            }
            return flattened;
        } catch (SQLException e) {
            e.printStackTrace();
            return map;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public QualifiedNumber(double value) {
        this(value, new HashMap<>());
    }

    public QualifiedNumber plus(QualifiedNumber number) {
        verifySameUnits(number);
        return new QualifiedNumber(value + number.getValue(), getUnits());
    }

    public QualifiedNumber minus(QualifiedNumber number) {
        verifySameUnits(number);
        return new QualifiedNumber(value - number.getValue(), getUnits());
    }

    public Map<String, Fraction> getUnits() {
        return units;
    }

    public double getValue() {
        return value;
    }

    boolean isUnitless() {
        return units.size() == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Fraction fraction = new Fraction(value);
        if (fraction.getD() == 1) {
            builder.append(fraction.getN());
        } else {
            builder.append(fraction);
        }
        for (String unit : units.keySet().stream().sorted().collect(Collectors.toList())) {
            Fraction power = units.get(unit);
            builder.append(" ").append(unit);
            if (!power.isOne()) {
                builder.append("^").append(power);
            }
        }
        return builder.toString();
    }

    public QualifiedNumber sine() {
        return new QualifiedNumber(Math.sin(getValue()), getUnits());
    }

    public QualifiedNumber cosine() {
        return new QualifiedNumber(Math.cos(getValue()), getUnits());
    }

    public QualifiedNumber raisedTo(QualifiedNumber number) {
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("Exponents cannot have units");
        }
        Map<String, Fraction> units = new HashMap<>();
        for (String unit : this.units.keySet()) {
            units.put(unit, this.units.get(unit).times(new Fraction(number.getValue())));
        }
        return new QualifiedNumber(Math.pow(getValue(), number.getValue()), units);
    }

    public QualifiedNumber negated() {
        return new QualifiedNumber(-getValue(), getUnits());
    }

    public QualifiedNumber dividedBy(QualifiedNumber number) {
        Map<String, Fraction> units = new HashMap<>(getUnits());
        for (String s : number.getUnits().keySet()) {
            if (units.get(s) == null) {
                units.put(s, new Fraction(0, 1));
            }
            units.put(s, units.get(s).minus(number.getUnits().get(s)));
        }
        return new QualifiedNumber(value / number.getValue(), units);
    }

    public QualifiedNumber times(QualifiedNumber number) {
        Map<String, Fraction> units = new HashMap<>(getUnits());
        for (String s : number.getUnits().keySet()) {
            if (units.get(s) == null) {
                units.put(s, new Fraction(0, 1));
            }
            units.put(s, units.get(s).plus(number.getUnits().get(s)));
        }
        return new QualifiedNumber(value * number.getValue(), units);
    }

    private void verifySameUnits(QualifiedNumber number) {
        if (getUnits().size() != number.getUnits().size()) {
            throw new IllegalArgumentException("Cannot add numbers with different dimensions");
        }
        for (String s : getUnits().keySet()) {
            if (!getUnits().get(s).equals(number.getUnits().get(s))) {
                throw new IllegalArgumentException("Cannot add numbers with different dimensions");
            }
        }
    }
}
