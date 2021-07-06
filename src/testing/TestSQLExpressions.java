package testing;

import junit.framework.TestCase;
import variablenInSQL.Parser;
import variablenInSQL.SQL;

public class TestSQLExpressions extends TestCase
{
	SQL sql;;

	public void testValidWorlds()
	{
		for (String aWord : validStrings)
		{
			System.out.println("VALID QUERIES:");
			System.out.println(aWord);
			String aSQLStatement = Parser.parse(aWord + "\n");
			sql = new SQL(aSQLStatement);
			Exception e = sql.getE();
			assertTrue(e == null);
		}
	}

	public void testInvalidWorlds()
	{
		System.out.println("INVALID QUERIES:");
		for (String aWord : invalidStrings)
		{
			System.out.println(aWord + "\n");
			String aSQLStatement = Parser.parse(aWord);
			sql = new SQL(aSQLStatement);
			Exception e = sql.getE();
			assertTrue(e != null);
		}
	}

	String[] validStrings = new String[] {
			"select 1",
			"select * from kunde;",
			"select * from kunde where kundenid = 1",
			"kunden = select sum(kundenid) kunde from kunde;",
			"name1 = select * from kunde",
			"name1 = select max(kundenid) from kunde; name2 = select * from name1 where name1 = 2",
			"name1 = select max(kundenid) from kunde; name2 = select * from name1 where 2 = name1",
			"name1 = select sum(kundenid) from kunde group by kundenid; select kundenid from kunde group by kundenid having kundenid >= ALL name1;",
			"name1 = select kundenid from kunde; name2 = select * from name1 where kundenid not in name1",
			"name1 = select * from kunde; name2 = select * from kunde; name3 = select max(kundenid) from name2; name4 = select * from name1 natural join name3 where name3 = 3;",
			"select*from kunde right outer join vertrag using(vertragsid)",
			"select kundenid from kunde natural join (select * from vertrag where vertragsid = 1) a where kundenid >= ALL(SELECT kundenid FROM kunde GROUP BY kundenid HAVING MAX(kundenid) = 2) group by kundenid;",
			"name1 = select * from kunde;name2 = select * from kunde;name3 = select * from kunde;name4 = select * from name1 natural join name2 natural join name3",
			"name1 = select 1 where 1=1; name2 = select 1 where 2=2; select * from name1 natural join name2;",
			"name = select * from mitarbeiter; select mitarbeiterid, lohn from name;",
			"select kundenid, 1.5 from kunde  order   by kundenid;",
			"name = (select 1 as k, 1.0 order by k limit 1); (select k, 1.0 from name order by k limit 2);",
	};

	String[] invalidStrings = new String[] {
			"",
			";",
			"     ",
			"  ;  ;",
			"    \n    ",
			"ddsafgsdfg",
			"select * from kuu",
			"selet * from kunde",
			"select kuu from kunde",
			"select * from kunde where kunde.ayaya = 3",
			"select kundenid from kunde having max(vertragsid) = 1",
			"schuld =\r\n" + " select kundenid\r\n" + " from Betrag ;\r\n" + "\r\n" + "select kundenid\r\n" + "from schuld natural join quotient;",
			"kunde = select sum(kundenid) kunden from kunde;",
			"insert into kunde values(1000,1000)",
			"delete from kunde",
			"alter table kunde rename to kundens",
			"Alle Personen = select * from kunde;",
			"a =",
			"a = s",
			";:=H;,:",
			"END = select * from kunde; a = select * from END;",
	};
}