package de.debuglevel.greeter.person

import jakarta.inject.Singleton
import mu.KotlinLogging
import java.util.*

@Singleton
class PersonGeneratorService {
    private val logger = KotlinLogging.logger {}

    fun generateRandom(): Person {
        logger.debug { "Generating random person..." }
        val person = Person(UUID.randomUUID(), "${firstnames.random()} ${lastnames.random()}")
        logger.debug { "Generated random person: $person" }
        return person
    }

    private val lastnames = arrayOf(
        "Georgeanna",
        "Seraphim",
        "Drus",
        "Thalia",
        "Jilly",
        "Haymes",
        "Tanberg",
        "Wyly",
        "Holofernes",
        "Anastatius",
        "Middendorf",
        "Eirena",
        "Henrieta",
        "Pavyer",
        "Trevorr",
        "Edgerton",
        "Ynes",
        "Aldridge",
        "Ole",
        "Childs",
        "Baryram",
        "Henryetta",
        "Erhard",
        "Bautram",
        "New",
        "Gabbert",
        "Knuth",
        "Ignacia",
        "Hawkie",
        "Suzetta",
        "Timothy",
        "Egidius",
        "Joshuah",
        "Melmon",
        "Mick",
        "Toolis",
        "Pain",
        "Rausch",
        "Vere",
        "Pettit",
        "Kraul",
        "Lori",
        "Apfelstadt",
        "Ozmo",
        "Horlacher",
        "Ddene",
        "Askwith",
        "Darrelle",
        "Kristos",
        "September",
        "Vincentia",
        "Decamp",
        "Anagnos",
        "Yeo",
        "Camel",
        "Alleyne",
        "Halbert",
        "Isidro",
        "Brodie",
        "Pattie",
        "Atthia",
        "Greene",
        "Paule",
        "Weinert",
        "Chery",
        "Oby",
        "Machute",
        "Forbes",
        "Herzog",
        "Ratib",
        "Madel",
        "Axe",
        "Bradley",
        "Beisel",
        "Buatti",
        "Callahan",
        "Ronny",
        "Lathe",
        "Eolanda",
        "Stetson",
        "Banky",
        "Lorrimer",
        "Jacobba",
        "Landbert",
        "Connett",
        "Phyllys",
        "Chaker",
        "Peggir",
        "Arndt",
        "Kimberli",
        "Frants",
        "Ofella",
        "Mun",
        "Aekerly",
        "Raynah",
        "Om",
        "Belen",
        "Truman",
        "Henryson",
        "Fannie",
        "Dottie",
        "Klarrisa",
        "Idette",
        "Anselma",
        "Hildegaard",
        "Reinhold",
        "Boorman",
        "Bodi",
        "Pryor",
        "Oleta",
        "Denni",
        "Rothwell",
        "Grindlay",
        "Lovel",
        "Capps",
        "Ananna",
        "Alexei",
        "Ching",
        "Arria",
        "Dougy",
        "Langer",
        "Caddaric",
        "Carree",
        "Pry",
        "Britta",
        "Fan",
        "Brest",
        "Bullion",
        "Annadiana",
        "Aloisia",
        "Malory",
        "Japha",
        "Marshal",
        "Julianne",
        "Gleeson",
        "Willtrude",
        "Royd",
        "Tillinger",
        "Borgeson",
        "Kenneth",
        "Ranie",
        "Seymour",
        "Anett",
        "Philander",
        "Nicholl",
        "Wetzell",
        "Fadiman",
        "Cathleen",
        "Pinkham",
        "Dulci",
        "Saphra",
        "Jezabella",
        "Elvina",
        "Sheeran",
        "Shute",
        "Carman",
        "Krisha",
        "Une",
        "Jone",
        "Cayser",
        "Weinreb",
        "Steve",
        "Gilda",
        "Burns",
        "Bertie",
        "Blockus",
        "Myer",
        "Gilemette",
        "Kylander",
        "Libbie",
        "Anderea",
        "Kristof",
        "Janey",
        "Layney",
        "Urban",
        "Herb",
        "Nertie",
        "Salvay",
        "Bucher",
        "Soneson",
        "Zeiger",
        "Augustus",
        "Bainter",
        "Zildjian",
        "Esbenshade",
        "Madalyn",
        "Cralg",
        "Adamec",
        "Azelea",
        "Montagna",
        "Garmaise",
        "Meneau",
        "Wait",
        "Corron",
        "Warp",
        "Jude",
        "Moule",
        "Damita",
        "Guinn",
        "Ulrika",
        "Kimitri",
        "Wengert",
        "Dannel",
        "Lauer",
        "Bowie",
        "Betsy",
        "Cranston",
        "Juanne",
        "Sedberry",
        "Smail",
        "Shulamith",
        "Kiley",
        "Alyworth",
        "Lauzon",
        "Laughton",
        "Yule",
        "Quint",
        "Bonnie",
        "Clawson",
        "Paza",
        "Doty"
    )

    private val firstnames = arrayOf(
        "Jackqueline",
        "Dorothy",
        "Annelise",
        "Dot",
        "Joana",
        "Moll",
        "Linet",
        "Kaitlyn",
        "Ernestine",
        "Milzie",
        "Casandra",
        "Jaquelyn",
        "Pammi",
        "Laurene",
        "Kyla",
        "Zorah",
        "Anthe",
        "Jazmin",
        "Odette",
        "Jonell",
        "Cassi",
        "Venita",
        "Tuesday",
        "Robby",
        "Agretha",
        "Binnie",
        "Gipsy",
        "Eadith",
        "Ianthe",
        "Cherianne",
        "Fredia",
        "Risa",
        "Liv",
        "Faythe",
        "Stella",
        "Ginnifer",
        "Marj",
        "Lexine",
        "Joey",
        "Orelee",
        "Ethyl",
        "Sibel",
        "Shaylah",
        "Vi",
        "Brigit",
        "Noelyn",
        "Delores",
        "Madalena",
        "Clarissa",
        "Misha",
        "Teodora",
        "Anthea",
        "Lyndsie",
        "Laney",
        "Evvie",
        "Bette",
        "Caitlin",
        "Lissie",
        "Rebekah",
        "Rachelle",
        "Latrina",
        "Belva",
        "Lise",
        "Matti",
        "Liza",
        "Amalie",
        "Melli",
        "Rivalee",
        "Shana",
        "Yetta",
        "Marrissa",
        "Ninnette",
        "Robyn",
        "Matilda",
        "Norrie",
        "Darice",
        "Denyse",
        "Mirella",
        "Herta",
        "Glennie",
        "Selinda",
        "Felicdad",
        "Nomi",
        "Cassandre",
        "Tilly",
        "Anastasia",
        "Kary",
        "Breena",
        "Sharona",
        "Ailyn",
        "Tildi",
        "Georgeanne",
        "Filia",
        "Aggy",
        "Bobine",
        "Dorolice",
        "Fionna",
        "Loria",
        "Adrienne",
        "Martita",
        "Lexi",
        "Corabel",
        "Fanchette",
        "Heidi",
        "Ceil",
        "Nertie",
        "Gavra",
        "Poppy",
        "Kattie",
        "Yasmin",
        "Cassandra",
        "Bernelle",
        "Juli",
        "Cheri",
        "Dasha",
        "Sheena",
        "Fredericka",
        "Lola",
        "Barbi",
        "Katina",
        "Vanda",
        "Melitta",
        "Vallie",
        "Stace",
        "Thomasin",
        "Koral",
        "Odele",
        "Tybie",
        "Caprice",
        "Miriam",
        "Cecil",
        "Krystal",
        "Kandy",
        "Neysa",
        "Caralie",
        "Hedvige",
        "Ingaborg",
        "Daisy",
        "Shanon",
        "Chrissie",
        "Lee",
        "Kriste",
        "Virginia",
        "Maggee",
        "Kelsey",
        "Lucienne",
        "Valerie",
        "Dacie",
        "Chelsea",
        "Felice",
        "Helen-Elizabeth",
        "Alvinia",
        "Orelle",
        "Merrielle",
        "Nancie",
        "Ardisj",
        "Jocelyn",
        "Hayley",
        "Ray",
        "Thekla",
        "Shawnee",
        "Dagmar",
        "Dulcia",
        "Ofelia",
        "Fanya",
        "Maura",
        "Oliy",
        "Roanna",
        "Magdalene",
        "Mallory",
        "Concordia",
        "Candis",
        "Arliene",
        "Helge",
        "Marci",
        "Alissa",
        "Venus",
        "Anstice",
        "Nita",
        "Mavis",
        "Elsa",
        "Blake",
        "Nanni",
        "Mahalia",
        "Quintina",
        "Delilah",
        "Kristien",
        "Amandie",
        "Bridie",
        "Willow",
        "Clovis",
        "Drucie",
        "Blinny",
        "Glynnis",
        "Dodi",
        "Angelia",
        "Mella",
        "Orel",
        "Danice",
        "Edi",
        "Serene",
        "Deeanne",
        "Loreen",
        "Wenona",
        "Celina",
        "Renae",
        "Emelita",
        "Cristy",
        "Leela",
        "Cristin",
        "Lyssa",
        "Cele",
        "Daisi",
        "Kore",
        "Konstance",
        "Nettle",
        "Adina",
        "Daisie",
        "Binni",
        "Celie",
        "Beverly",
        "Laraine",
        "Milissent",
        "Edeline",
        "Antonetta",
        "Helena",
        "Collette",
        "Pepi",
        "Christi",
        "Junie",
        "Jess"
    )
}