package test;

import org.junit.Before;
import org.junit.Test;

public class TableTest {

    String template;

    String[] tables;

    @Before
    public void setUp() {
        template = "<table tableName=\"%s\" enableCountByExample=\"false\" enableUpdateByExample=\"false\"\n" +
                "               enableDeleteByExample=\"false\" enableSelectByExample=\"false\" selectByExampleQueryId=\"false\"></table>";

        tables = new String[]{
                "SYS_NGINX_LOG"
        };
    }

    @Test
    public void demo(){
        StringBuffer strb = new StringBuffer();
        for (String table : tables) {
            strb.append(String.format(template, table));
            strb.append("\r\n");
        }
        System.out.println(strb.toString());
    }

    @Test
    public void demo1(){
        for (String table : tables) {
            System.out.println(table);
        }
    }

}
