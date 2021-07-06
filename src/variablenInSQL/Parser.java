package variablenInSQL;

public class Parser
{
	final static String keyWords = "select|distinct|all|as|from|natural|left|right|full|outer|inner|cross|join|using|on|where|is|not|in|null|and|intersect|except|exists|true|false|between|union|group|by|having|max|min|avg|sum|count|order|limit|insert|update|delete|alter|create|drop|end";
	static private int lastQuery;
	static private boolean hasSemicolon;

	public static void main(String[] args)
	{
		// String toParse = "name1 = SELECT * FROM a; name2 = SELECT name1.a FROM name1; name3 = SELECT MAX(lohn) FROM name1; name4 = SELECT a,b FROM name2 NATURAL JOIN name3 WHERE lohn >= name3;";
		// String toParse = "select * FROM angestellter; name2 = select * from lohn; select * FROM angestellter; name1 = select lohn from name2;";
		// String toParse = "name1 = select max(kundenid) as k from kunde; name2 = select * from name1 where 2 = name1";
		// String toParse = "select * from (select * from (select * from kunde) kunde) kunde where kundenid = 1;";
		// String toParse = "schuld =\r\n" + " select kundenid\r\n" + " from Betrag ;\r\n" + "\r\n" + "select kundenid\r\n" + "from schuld natural join quotient;";
		// String toParse = "kunde = select * from kunde;\r\n" + "select * from kunde";
		// String toParse = "SELECT kundenid\r\n" + "FROM kunde NATURAL INNER JOIN \r\n" + " (SELECT * \r\n" + " FROM vertrag\r\n" + " WHERE vertragsid = 1 ) a \r\n" + "WHERE kundenid >= ALL \r\n" + " (SELECT kundenid \r\n" + " FROM kunde\r\n" + " GROUP BY kundenid\r\n" + " HAVING MAX ( kundenid ) = 2 ) \r\n" + "GROUP BY kundenid\r\n" + "HAVING MIN ( kundenid ) = 1;";
		// String toParse = "Alle Personen = select * from kunde";
		// String toParse = "a = ";
		// String toParse = "hallo ; name = etwas ; ein name";
		// String toParse = "name1 = select 1 where 1=1; name2 = select 1 where 2=2; select * from name1 natural join name2;";
		// String toParse = "name = select * from mitarbeiter; select mitarbeiterid, lohn from name;";
		// String toParse = "END = select * from kunde; a = select kundenID from END;";
		// String toParse = "(select * from kunde)";
		String toParse = "AllPay = SELECT employeeID, salary+bonus AS pay FROM employee NATURAL JOIN premium;\r\n" + "AvgPay = SELECT AVG(pay) FROM AllPay;\r\n" + "EmployeeWithHighPay = SELECT employeeID, pay FROM AllPay WHERE pay > AvgPay;";

		System.out.println(toParse + "\n");
		System.out.println(Parser.parse(toParse));
	}

	public static String parse(String query)
	{
		// Leere Anfrage
		if (query.matches("\\s+|"))
			return query;

		// Standartisierung und Aufteilung des Querys in seine Bestandteile.
		if (query.charAt(query.length() - 1) == ';')
		{
			hasSemicolon = true;
		}

		String filledUpQuery = query.replace("=", " = ").replace("<", " < ").replace(">", " > ").replace(",", " , ").replace("(", " ( ").replace(")", " ) ").replace("+", " + ").replace("-", " - ").replace("*", " * ").replace("/", " / ").replace(";", " ; ").replace("\n", " \n ").replace("\r", " \r ");
		String trimmedQuery = filledUpQuery.trim().replaceAll("\\s{2,}", " ").replace("> =", ">=").replace("< =", "<=").toLowerCase();
		String[] splitQuery = trimmedQuery.split(";");
		if (splitQuery.length == 0)
		{
			return query;
		}
		int number = splitQuery.length;
		String[] trimmedSplitQuery = new String[number];
		for (int i = 0; i < splitQuery.length; i++)
		{
			trimmedSplitQuery[i] = splitQuery[i].trim();
		}

		// Initialisierung des Arrays, welches alle Bezeichner und die Zugehörigen Subquerys enthält.
		String[][] subqueriesContentAndName = new String[number][2];
		String nameOfSubquery = "";
		String subquery = "";
		for (int i = 0; i < number; i++)
		{
			int indexOfEqual = trimmedSplitQuery[i].indexOf('=');
			if (indexOfEqual == -1)
			{
				nameOfSubquery = "";
				subquery = trimmedSplitQuery[i];
			}
			for (int j = 0; j < trimmedSplitQuery[i].length(); j++)
			{
				if (trimmedSplitQuery[i].charAt(j) == ' ')
				{
					if (trimmedSplitQuery[i].charAt(j + 1) == '=')
					{
						nameOfSubquery = trimmedSplitQuery[i].substring(0, indexOfEqual - 1);
						if (trimmedSplitQuery[i].length() >= indexOfEqual + 2)
						{
							subquery = trimmedSplitQuery[i].substring(indexOfEqual + 2, trimmedSplitQuery[i].length());
						}
					}
					else
					{
						nameOfSubquery = "";
						subquery = trimmedSplitQuery[i];
					}
					break;
				}
			}
			subqueriesContentAndName[i][0] = nameOfSubquery;
			subqueriesContentAndName[i][1] = subquery;
		}

		// Überprüfung, ob alle Bezeichner einen von den Keywörtern unterschiedlichen eindeutigen Namen besitzen.
		// Sicherstellung, dass es nicht zu zyklischen Verwesien kommen kann.
		// Filterung von Queries mit Bezeichner ohne Inhalt
		if (subqueriesContentAndName.length > 0)
		{
			for (int i = 0; i < subqueriesContentAndName.length; i++)
			{
				if (subqueriesContentAndName[i][0].equals(""))
				{
					continue;
				}
				if (subqueriesContentAndName[i][0].matches(keyWords))
				{
					return "FEHLER: Syntaxfehler\n  Keywörter dürfen nicht als Bezeichner verwendet werden";
				}
				if ((subqueriesContentAndName[i][1] + " ").contains(" " + subqueriesContentAndName[i][0] + " "))
				{
					return "FEHLER: Syntaxfehler bei »" + subqueriesContentAndName[i][0] + "«\n  Queries können weder ihren eigenen Bezeichner im Anfragetext beinhalten noch auf andere Weise zyklisch aufeinander verweisen\n  Umbenannte Spalten dürfen nicht den Namen einer Tabelle oder eines Queries besitzen ";
				}
				for (int j = i; j < subqueriesContentAndName.length; j++)
				{
					if (subqueriesContentAndName[i][0].equals(subqueriesContentAndName[j][0]) && i != j)
					{
						return "FEHLER: Syntaxfehler\n  Die Namen von Zwischengespeicherten Subqueires müssen eindeutig sein";
					}
				}
				if (!subqueriesContentAndName[i][0].equals("") && subqueriesContentAndName[i][1].equals(""))
				{
					return "FEHLER: Syntaxfehler\n  Benannte Queries brauchen einen Inhalt";
				}
			}
		}

		// Herausfinden der maximalen Arraylänge.
		int[] numOfWords = new int[number];
		for (int i = 0; i < subqueriesContentAndName.length; i++)
		{
			numOfWords[i] = subqueriesContentAndName[i][1].split(" ").length;
			subqueriesContentAndName[i][1] = subqueriesContentAndName[i][1] + " ";
		}
		int maxWords = 0;
		for (int i = 0; i < numOfWords.length; i++)
		{
			if (numOfWords[i] > maxWords)
			{
				maxWords = numOfWords[i];
			}
		}

		// Erstellung des Arrays der einzelnen Wörter eines Subqueries.
		String[][] wordsOfQuery = new String[number][maxWords];
		for (int i = 0; i < subqueriesContentAndName.length; i++)
		{
			wordsOfQuery[i] = subqueriesContentAndName[i][1].split(" ");
		}
		lastQuery = wordsOfQuery.length - 1;

		// Einfügen der Subqueries ineinander. (ich verstehe die Logik hierhinter selber nicht mehr)
		for (int k = wordsOfQuery[lastQuery].length - 1; k >= 1; k--)
		{
			for (int i = 0; i < wordsOfQuery.length - 1; i++)
			{
				if (wordsOfQuery[lastQuery][k].equals(subqueriesContentAndName[i][0]) && !wordsOfQuery[lastQuery][k - 1].contains(")"))
				{
					String[] newWords = subqueriesContentAndName[i][1].split(" ");
					int anzNewWords = newWords.length;
					boolean wurdeGefunden = false;
					for (int l = k - 1; l >= 0; l--)
					{
						if (wordsOfQuery[lastQuery][l].equals("from"))
						{
							wurdeGefunden = true;
							String[] newQuery = new String[wordsOfQuery[lastQuery].length + newWords.length + 2];
							for (int j = 0; j < k; j++)
							{
								newQuery[j] = wordsOfQuery[lastQuery][j];
							}
							newQuery[k] = "(";
							for (int j = k; j < k + newWords.length; j++)
							{
								newQuery[j + 1] = newWords[j - k];
							}
							newQuery[anzNewWords + k + 1] = ")";
							newQuery[anzNewWords + k + 2] = subqueriesContentAndName[i][0].trim();
							int m = k;
							for (int j = anzNewWords + k + 3; j < newQuery.length; j++)
							{
								newQuery[j] = wordsOfQuery[lastQuery][m + 1];
								m++;
							}
							wordsOfQuery[lastQuery] = newQuery;
							break;
						}
						else if (wordsOfQuery[lastQuery][l].equals("where") || wordsOfQuery[lastQuery][l].equals("having"))
						{
							wurdeGefunden = true;
							String[] newQuery = new String[wordsOfQuery[lastQuery].length + newWords.length + 1];
							for (int j = 0; j < k; j++)
							{
								newQuery[j] = wordsOfQuery[lastQuery][j];
							}
							newQuery[k] = "(";
							for (int j = k; j < k + newWords.length; j++)
							{
								newQuery[j + 1] = newWords[j - k];
							}
							newQuery[anzNewWords + k + 1] = ")";
							int m = k;
							for (int j = anzNewWords + k + 2; j < newQuery.length; j++)
							{
								newQuery[j] = wordsOfQuery[lastQuery][m + 1];
								m++;
							}
							wordsOfQuery[lastQuery] = newQuery;
							break;
						}
					}
					if (wurdeGefunden == false)
					{
						return "FEHLER: Syntaxfehler\n \"FROM\" oder \"WHERE\" wurde vergessen oder falsch geschrieben";
					}
					i = 0;
					k = wordsOfQuery[lastQuery].length - 1;
				}
			}
		}

		// Bezeichner müssen aus einem zusammenhängendem Wort bestehen
		for (int i = 0; i < wordsOfQuery.length; i++)
		{
			for (int j = 0; j < wordsOfQuery[i].length; j++)
			{
				if (wordsOfQuery[i][j].matches(keyWords))
				{
					break;
				}
				else if (j + 1 < wordsOfQuery[i].length && wordsOfQuery[i][j + 1].equals("=") && j != 0)
				{
					return "FEHLER: Syntaxfehler\n Bezeichner müssen aus einem zusammenhängendem Wort bestehen";
				}
			}
		}
		// Formatierung der Wörter des Queries.
		for (int i = 0; i < wordsOfQuery[lastQuery].length; i++)
		{
			if (wordsOfQuery[lastQuery][i].matches(keyWords))
			{
				wordsOfQuery[lastQuery][i] = wordsOfQuery[lastQuery][i].toUpperCase();
			}
		}
		// Formatierung des Ergebnisqueries: Episode1.
		int indexOfFirstSelect = 0;
		for (int i = 0; i < wordsOfQuery[lastQuery].length; i++)
		{
			if (wordsOfQuery[lastQuery].equals("SELECT"))
			{
				indexOfFirstSelect = i;
			}
		}
		String tabber = "";
		for (int i = indexOfFirstSelect + 1; i < wordsOfQuery[lastQuery].length; i++)
		{
			if (wordsOfQuery[lastQuery][i].matches("FROM|USING|WHERE|GROUP|HAVING|ORDER|LIMIT|UNION|INTERSECT|EXCEPT"))
			{
				wordsOfQuery[lastQuery][i] = "\n" + tabber + wordsOfQuery[lastQuery][i];
			}
			if (wordsOfQuery[lastQuery][i].matches("SELECT") && wordsOfQuery[lastQuery][i - 1].contains("("))
			{
				tabber = tabber + "\t";
				wordsOfQuery[lastQuery][i] = "\n" + tabber + wordsOfQuery[lastQuery][i];
			}
			if (wordsOfQuery[lastQuery][i].matches("SELECT") && !wordsOfQuery[lastQuery][i - 1].contains("("))
			{
				wordsOfQuery[lastQuery][i] = "\n" + tabber + wordsOfQuery[lastQuery][i];
			}
		}

		// Differenz der Anzahl von Öffnungen und Schließungen von Klammern muss 0 ergeben und immer größer gleich 0 sein.
		int bracketCounter = 0;
		for (int i = 0; i < wordsOfQuery[lastQuery].length; i++)
		{
			if (wordsOfQuery[lastQuery][i].equals("("))
			{
				bracketCounter++;
			}
			if (wordsOfQuery[lastQuery][i].equals(")"))
			{
				bracketCounter--;
				if (bracketCounter == -1)
				{
					return "FEHLER: Syntaxfehler bei »)«\n  Klammern können nicht geschlossen werden, ohne sie zuvor zu öffnen (nicht übereinstimmende Klammern)";
				}
			}
		}
		if (bracketCounter != 0)
		{
			return "FEHLER: Syntaxfehler bei »(«\n  Klammern können nicht geöffnet werden, ohne sie zu schließen (nicht übereinstimmende Klammern)";
		}

		// Formatierung des Ergebnisqueries: Episode2.
		bracketCounter = 1;
		int tabCounter = 0;
		int indexOfEnd = 0;

		for (int i = 0; i < wordsOfQuery[lastQuery].length - 1; i++)
		{
			if (wordsOfQuery[lastQuery][i].contains("(") && wordsOfQuery[lastQuery][i + 1].contains("SELECT"))
			{
				indexOfEnd = tabOnDatAss(wordsOfQuery, i, bracketCounter);
				for (int j = indexOfEnd + 1; j < wordsOfQuery[lastQuery].length; j++)
				{
					if (wordsOfQuery[lastQuery][j].contains("SELECT") || wordsOfQuery[lastQuery][j].contains("FROM") || wordsOfQuery[lastQuery][j].contains("USING") || wordsOfQuery[lastQuery][j].contains("WHERE") || wordsOfQuery[lastQuery][j].contains("GROUP") || wordsOfQuery[lastQuery][j].contains("HAVING") || wordsOfQuery[lastQuery][j].contains("ORDER") || wordsOfQuery[lastQuery][j].contains("UNION") || wordsOfQuery[lastQuery][j].contains("INTERSECT") || wordsOfQuery[lastQuery][j].contains("EXCEPT"))
					{
						wordsOfQuery[lastQuery][j] = wordsOfQuery[lastQuery][j].substring(1);
						while (wordsOfQuery[lastQuery][j].contains("\t"))
						{
							tabCounter++;
							wordsOfQuery[lastQuery][j] = wordsOfQuery[lastQuery][j].substring(1);
						}
						wordsOfQuery[lastQuery][j] = wordsOfQuery[lastQuery][j];
						while (tabCounter != 1)
						{
							wordsOfQuery[lastQuery][j] = "\t" + wordsOfQuery[lastQuery][j];
							tabCounter--;
						}
						tabCounter--;
						wordsOfQuery[lastQuery][j] = "\n" + wordsOfQuery[lastQuery][j];
					}
				}
			}
		}

		// Zusammenführen der einzelnen Wörter des Ergebnisqueries.
		query = "";
		for (int i = 0; i < wordsOfQuery[lastQuery].length; i++)
		{
			query += wordsOfQuery[lastQuery][i] + " ";
		}
		if (hasSemicolon)
		{
			query = query.trim() + ";";
		}
		query = query.replace("  ", " ").replace(" ,", ",").replace("( ", "(").replace(" )", ")");

		return query;

	}

	// Rekursive Methode zum ermitteln der Indizes an denen Zeilenumbrüche und Tabs stattfinden sollen.
	public static int tabOnDatAss(String[][] wordsOfQuery, int index, int bracketCounter)
	{
		index++;

		if (wordsOfQuery[lastQuery][index].equals("("))
		{
			bracketCounter++;
		}
		if (wordsOfQuery[lastQuery][index].equals(")"))
		{
			bracketCounter--;
		}
		if (bracketCounter == 0)
		{
			return index;
		}
		else
			return tabOnDatAss(wordsOfQuery, index, bracketCounter);
	}
}