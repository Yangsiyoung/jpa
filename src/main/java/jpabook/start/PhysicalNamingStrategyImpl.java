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

    /**
     * GenerationType.SEQUENCE 를 사용하는데, 이 코드에서 기존에 null을 리턴하도록 해놨어서...
     * "Name cannot be null" 이라는 불친절한... 어디서 문제인지 알 수 없는 예외를 만났다...
     * Hibername 5 부터 PhysicalNamingStrategy Interface 를 implements 해야하고,
     * 이에 따라 컬럼명만 Camel Case -> Snake Case 로 간단히 치환을 하려다가 이런 상황이 발생했다.
     *
     * 앞으로 내가 Sequence Name 을 Camel Case -> Snake Case 로 치환하려면 여기를 수정을 해야하고,
     * Schema 나 Catalog 를 사용하게 된다면 저 위에 있는 return null 말고 적절한 값을 리턴해줘야겠지...
     * 지금 이 메서드에서 return null 을 하고 있어서 Sequence 를 사용하는데 어려움을 겪었으니까..
     * 조심하자...
     */

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        // Camel Case -> Snake Case 로 치환 안하고 그냥 바로 해당 이름 사용
        return name;
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

