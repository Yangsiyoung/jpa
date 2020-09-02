package jpabook.start;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class PhysicalNamingStrategyImpl implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return null;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return null;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return null;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }


    protected static String addUnderscores(String name) {
        // . -> _ 로 바꾸는 이유는 몰까???
        final StringBuilder stringBuilder = new StringBuilder( name.replace('.', '_') );
        for (int index = 1; index < stringBuilder.length() - 1; index++) {
            if (
                    // 대문자 앞뒤로 소문자라면
                    Character.isLowerCase( stringBuilder.charAt(index - 1) ) &&
                    Character.isUpperCase( stringBuilder.charAt(index) ) &&
                    Character.isLowerCase( stringBuilder.charAt(index + 1) )
            ) {
                stringBuilder.insert(index++, '_');
            }
        }
        return stringBuilder.toString().toLowerCase(Locale.ROOT);
    }
}

