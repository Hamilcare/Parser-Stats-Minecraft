/**
 * ParseStatsMC
 * @author Hamilcare
 * @version 0.1
 */  




import java.io.*;

import java.util.*;


class ParserStatsMc
{



	/**
	 * Rend le Json sous forme d'une string sans retour a la ligne
	 * @author Hamilcare
	 * @param fileName le nom du fichier que l'on souhaite traiter
	 * @return String le contenu de fileName en une seule ligne
	 */ 

	public static String readFile (String fileName)
	{


		String chaine = "";



		//lecture du fichier texte 
		try
		{

			InputStream ips = new FileInputStream (fileName);

			InputStreamReader ipsr = new InputStreamReader (ips);

			BufferedReader br = new BufferedReader (ipsr);

			String ligne;



			while ((ligne = br.readLine ()) != null)
			{

				//System.out.println(ligne);
				chaine += ligne + "\n";

			}

			br.close ();

		}


		catch (Exception e)
		{

			System.out.println (e.toString ());

		}


		return chaine;

	}



	/**
	 * Formate la String rendu par readFile
	 * @author Hamilcare
	 * @param chaine le contenu du fichier json
	 * @return String une string contenant une stat par ligne
	 */ 
	public static String parseStats (String chaine)
	{


		chaine = chaine.replace ("{", "");

		chaine = chaine.replace ('"', ' ');

		chaine = chaine.replace (',', '\n');

		chaine = chaine.replace ("stat.", "");

		chaine = chaine.replace ("minecraft", "");

		chaine = chaine.replace ("minecraft", "");

		chaine = chaine.replace ("\n ", "\n");

		//Vire les biomes des stats
		chaine =
				chaine.substring (0,
						chaine.indexOf ("progress")) + "" +
						chaine.substring (chaine.indexOf ("]}\n") + 3);


		chaine = chaine.replace ("}\n", "");


		return chaine;


	}


	/**
	 * Compte le nombre de stats différentes du joueur 
	 * @author Hamilcare
	 * @param  chaine la String formatée contenant les stats
	 * @return int le nombre de stats différentes dans s
	 */ 
	public static int getNbStats (String chaine)
	{

		int occu = 0;

		for (int i = 0; i < chaine.length (); i++)
		{

			if (chaine.charAt (i) == ('\n'))
			{

				occu++;

			}

		}

		//Add +1 'cause the last one does not finish by '\n'
		return occu + 1;

	}


	/**
	 * Rend les stats parsée dans une hashMap
	 * @author Hamilcare
	 * @param  chaine la String formatée contenant les stats
	 * @return les stats dans une hashMap
	 */ 
	public static HashMap < String, Integer > getHashMapJoueur (String chaine)
	{

		HashMap < String, Integer > resul = new HashMap < String, Integer > ();


		int nbStats = getNbStats (chaine);

		String[]tab = new String[nbStats];

		tab = chaine.split ("\n");


		//System.out.println(tab[0]);
		String key = "";

		int value;


		for (int i = 0; i < tab.length; i++)
		{

			key = tab[i].substring (0, tab[i].indexOf (":"));

			try
			{

				value =
						Integer.parseInt (tab[i].substring (tab[i].indexOf (":") + 1));

			} 
			catch (NumberFormatException e)
			{

				System.out.println ("Erreur sur la stat " + key);

				System.out.println (e);

				System.out.println ("Valeur remplacée par 0");

				value = 0;

			} 
			//System.out.println("Key : "+key +"\nValue : "+value);

			resul.put (key, value);

		} 

		return resul;



	}



	/**
	 * Rend une HashMap sous forme de String
	 * @author Hamilcare
	 * @param  hm la HashMap que le souhaite afficher
	 * @return la String contenant les éléments de hm
	 */ 
	public static String readHashmap (HashMap < String, Integer > hm)
	{

		String s = "";



		for (Map.Entry < String, Integer > entry:hm.entrySet ())
		{

			String key = entry.getKey ();

			int value = entry.getValue ();

			s += key + "" + value + "\n";

		} 


		return s;

	}


	/**
	 * Ecrit les stats dans un fichier texte.
	 * @author Hamilcare
	 * @param  hm les Stats que l'on souhaite écrire dans un fichier
	 * @param filename le nom que l'on souhhaite donner au fichier
	 */ 

	public static void writeStats (String s, String filename)
	{

		File f = new File (filename);


		try 
		{

			PrintWriter pw =
					new PrintWriter (new BufferedWriter (new FileWriter (f)));




			pw.println (s);



			pw.close ();

		} 
		catch (IOException exception) 
		{

			System.out.println ("Erreur lors de la lecture : " +
					exception.getMessage ());

		} 
	} 

	
	/**
	 * Ajoute les stats contenues dans deux HashMap différentes
	 * @author Hamilcare
	 * @param  hm HashMap of the current player
	 * @param total  HashMap wich contains values of previous players
	 */ 

	public static void addHashMap (HashMap < String, Integer > hm,HashMap < String, Integer> total )

	{
		int cpt=0;

		for (Map.Entry < String, Integer > entry:hm.entrySet ())
		{
				
			String key = entry.getKey();

			int value = entry.getValue();


			if (total.containsKey(key))
			{
				System.out.println("Ok");
				int tmp = total.get(key);

				total.remove (key);

				total.put(key,(value + tmp));

			}

			else
			{
				cpt++;
				total.put (key, value);

			}

		}
		
		System.out.println(cpt);

	}









	public static void main (String[]args)
	{
		/*HashMap<String,Integer> test = new HashMap <String,Integer>();
		
		test.put("coucou",1);
		System.out.println(test.containsKey("coucou"));
		System.out.println(test.containsKey("hello"));
		*/
		HashMap < String, Integer > total = new HashMap < String, Integer > ();

		String chaine2 = "";
		String chaine ="";

		for (int i = 0; i < args.length; i++)
		{




			HashMap < String, Integer > map = new HashMap < String, Integer > ();
					


			chaine = readFile (args[i]);



			//Compte le nb de stats differentes
			chaine = parseStats (chaine);

			map = getHashMapJoueur (chaine);


			

			chaine2 = readHashmap (map);

			writeStats (chaine2,
					args[0].substring (5, args[0].indexOf (".json")));


			if (args.length > 1)
			{ addHashMap(map,total);
			System.out.println("Coucou");
			}



			System.out.println("Player comput : "+i);
			

		}








		if(args.length>1){
			String filename ="total";
			for(int i=0; i<args.length; i++){
				filename+=args[i].substring (5, args[i].indexOf (".json"));
			}

			chaine2 = readHashmap (total);
			writeStats (chaine2,filename);
		}





	}

}



