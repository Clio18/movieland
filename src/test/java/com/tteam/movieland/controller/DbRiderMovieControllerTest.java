package com.tteam.movieland.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DBRider
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc(addFilters = false)
class DbRiderMovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "test");
        registry.add("spring.datasource.password", () -> "test");
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test GetAll Without Parameters")
    void testGetAllWithoutParameters() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[4].id").value(5))
                .andExpect(content()
                    .json("""
                            [
                                {
                                    "id": 1,
                                    "nameUkr": "Побег из Шоушенка",
                                    "nameNative": "The Shawshank Redemption",
                                    "yearOfRelease": 1994,
                                    "description": "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                                    "price": 123.45,
                                    "rating": 8.9,
                                    "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg"
                                },
                                {
                                    "id": 2,
                                    "nameUkr": "Зеленая миля",
                                    "nameNative": "The Green Mile",
                                    "yearOfRelease": 1999,
                                    "description": "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                                    "price": 134.67,
                                    "rating": 8.9,
                                    "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTUxMzQyNjA5MF5BMl5BanBnXkFtZTYwOTU2NTY3._V1._SY209_CR0,0,140,209_.jpg"
                                },
                                {
                                    "id": 3,
                                    "nameUkr": "Форрест Гамп",
                                    "nameNative": "Forrest Gump",
                                    "yearOfRelease": 1994,
                                    "description": "От лица главного героя Форреста Гампа, слабоумного безобидного человека с благородным и открытым сердцем, рассказывается история его необыкновенной жизни.Фантастическим образом превращается он в известного футболиста, героя войны, преуспевающего бизнесмена. Он становится миллиардером, но остается таким же бесхитростным, глупым и добрым. Форреста ждет постоянный успех во всем, а он любит девочку, с которой дружил в детстве, но взаимность приходит слишком поздно.",
                                    "price": 200.6,
                                    "rating": 8.6,
                                    "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR2,0,140,209_.jpg"
                                },
                                {
                                    "id": 4,
                                    "nameUkr": "Список Шиндлера",
                                    "nameNative": "Schindler's List",
                                    "yearOfRelease": 1993,
                                    "description": "Фильм рассказывает реальную историю загадочного Оскара Шиндлера, члена нацистской партии, преуспевающего фабриканта, спасшего во время Второй мировой войны почти 1200 евреев.",
                                    "price": 150.5,
                                    "rating": 8.7,
                                    "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BNDE4OTMxMTctNmRhYy00NWE2LTg3YzItYTk3M2UwOTU5Njg4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SX140_CR0,0,140,209_.jpg"
                                },
                                {
                                    "id": 5,
                                    "nameUkr": "1+1",
                                    "nameNative": "Intouchables",
                                    "yearOfRelease": 2011,
                                    "description": "Пострадав в результате несчастного случая, богатый аристократ Филипп нанимает в помощники человека, который менее всего подходит для этой работы, — молодого жителя предместья Дрисса, только что освободившегося из тюрьмы. Несмотря на то, что Филипп прикован к инвалидному креслу, Дриссу удается привнести в размеренную жизнь аристократа дух приключений.",
                                    "price": 120.0,
                                    "rating": 8.3,
                                    "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTYxNDA3MDQwNl5BMl5BanBnXkFtZTcwNTU4Mzc1Nw@@._V1._SY209_CR0,0,140,209_.jpg"
                                }
                            ]
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test GetAll Rating Descending Order")
    void testGetAllDescRating() throws Exception {
        String sortingOrder = "desc";
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies?rating={sortingOrder}", sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(4))
                .andExpect(jsonPath("$[3].id").value(13))
                .andExpect(jsonPath("$[4].id").value(3))
                .andExpect(content()
                        .json("""
                                [
                                   {
                                       "id": 2,
                                       "nameUkr": "Зеленая миля",
                                       "nameNative": "The Green Mile",
                                       "yearOfRelease": 1999,
                                       "description": "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                                       "price": 134.67,
                                       "rating": 8.9,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTUxMzQyNjA5MF5BMl5BanBnXkFtZTYwOTU2NTY3._V1._SY209_CR0,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 1,
                                       "nameUkr": "Побег из Шоушенка",
                                       "nameNative": "The Shawshank Redemption",
                                       "yearOfRelease": 1994,
                                       "description": "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                                       "price": 123.45,
                                       "rating": 8.9,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 13,
                                       "nameUkr": "Пролетая над гнездом кукушки",
                                       "nameNative": "One Flew Over the Cuckoo's Nest",
                                       "yearOfRelease": 1975,
                                       "description": "Сымитировав помешательство в надежде избежать тюремного заключения, Рэндл Патрик МакМерфи попадает в психиатрическую клинику, где почти безраздельным хозяином является жестокосердная сестра Милдред Рэтчед. МакМерфи поражается тому, что прочие пациенты смирились с существующим положением вещей, а некоторые — даже сознательно пришли в лечебницу, прячась от пугающего внешнего мира. И решается на бунт. В одиночку.",
                                       "price": 180.0,
                                       "rating": 8.7,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BZjA0OWVhOTAtYWQxNi00YzNhLWI4ZjYtNjFjZTEyYjJlNDVlL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR0,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 4,
                                       "nameUkr": "Список Шиндлера",
                                       "nameNative": "Schindler's List",
                                       "yearOfRelease": 1993,
                                       "description": "Фильм рассказывает реальную историю загадочного Оскара Шиндлера, члена нацистской партии, преуспевающего фабриканта, спасшего во время Второй мировой войны почти 1200 евреев.",
                                       "price": 150.5,
                                       "rating": 8.7,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BNDE4OTMxMTctNmRhYy00NWE2LTg3YzItYTk3M2UwOTU5Njg4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SX140_CR0,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 3,
                                       "nameUkr": "Форрест Гамп",
                                       "nameNative": "Forrest Gump",
                                       "yearOfRelease": 1994,
                                       "description": "От лица главного героя Форреста Гампа, слабоумного безобидного человека с благородным и открытым сердцем, рассказывается история его необыкновенной жизни.Фантастическим образом превращается он в известного футболиста, героя войны, преуспевающего бизнесмена. Он становится миллиардером, но остается таким же бесхитростным, глупым и добрым. Форреста ждет постоянный успех во всем, а он любит девочку, с которой дружил в детстве, но взаимность приходит слишком поздно.",
                                       "price": 200.6,
                                       "rating": 8.6,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR2,0,140,209_.jpg"
                                   }
                               ]
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test GetAll Rating Ascending Order")
    void testGetAllAscRating() throws Exception {
        String sortingOrder = "asc";
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies?rating={sortingOrder}", sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(23))
                .andExpect(jsonPath("$[1].id").value(22))
                .andExpect(jsonPath("$[2].id").value(12))
                .andExpect(jsonPath("$[3].id").value(25))
                .andExpect(jsonPath("$[4].id").value(9))
                .andExpect(content()
                        .json("""
                                [
                                    {
                                        "id": 23,
                                        "nameUkr": "Блеф",
                                        "nameNative": "Bluff storia di truffe e di imbroglioni",
                                        "yearOfRelease": 1976,
                                        "description": "Белль Дюк имеет старые счеты с Филиппом Бэнгом, который отбывает свой срок за решёткой. Для того чтобы поквитаться с Филиппом, Белль Дюк вступает в сговор с другим аферистом по имени Феликс, чтобы тот организовал побег Филиппа Бенга из тюрьмы. Побег удаётся, но парочка заодно обманывает и Белль Дюк, исчезнув прямо из-под её носа. Выясняется, что и Филипп Бэнг, в свою очередь, не прочь отомстить Белль Дюк. Для этого он задумывает грандиозную мистификацию, сродни покерному блефу…",
                                        "price": 100.0,
                                        "rating": 7.6,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMjk5YmMxMjMtMTlkNi00YTI5LThhYTMtOTk2NmNiNzQwMzI0XkEyXkFqcGdeQXVyMTQ3Njg3MQ@@._V1._SX140_CR0,0,140,209_.jpg"
                                    },
                                    {
                                        "id": 22,
                                        "nameUkr": "Укрощение строптивого",
                                        "nameNative": "Il bisbetico domato",
                                        "yearOfRelease": 1980,
                                        "description": "Категорически не приемлющий женского общества грубоватый фермер вполне счастлив и доволен своей холостяцкой жизнью. Но неожиданно появившаяся в его жизни героиня пытается изменить его взгляды на жизнь и очаровать его. Что же из этого получится…",
                                        "price": 120.0,
                                        "rating": 7.7,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTc5NTM5OTY0Nl5BMl5BanBnXkFtZTcwNjg1MjcyMQ@@._V1._SY209_CR3,0,140,209_.jpg"
                                    },
                                    {
                                        "id": 12,
                                        "nameUkr": "Титаник",
                                        "nameNative": "Titanic",
                                        "yearOfRelease": 1997,
                                        "description": "Молодые влюбленные Джек и Роза находят друг друга в первом и последнем плавании «непотопляемого» Титаника. Они не могли знать, что шикарный лайнер столкнется с айсбергом в холодных водах Северной Атлантики, и их страстная любовь превратится в схватку со смертью…",
                                        "price": 150.0,
                                        "rating": 7.9,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1._SY209_CR0,0,140,209_.jpg"
                                    },
                                    {
                                        "id": 25,
                                        "nameUkr": "Танцующий с волками",
                                        "nameNative": "Dances with Wolves",
                                        "yearOfRelease": 1990,
                                        "description": "Действие фильма происходит в США во времена Гражданской войны. Лейтенант американской армии Джон Данбар после ранения в бою просит перевести его на новое место службы ближе к западной границе США. Место его службы отдалённый маленький форт. Непосредственный его командир покончил жизнь самоубийством, а попутчик Данбара погиб в стычке с индейцами после того, как довез героя до места назначения. Людей, знающих, что Данбар остался один в форте и должен выжить в условиях суровой природы, и в соседстве с кажущимися негостеприимными коренными обитателями Северной Америки, просто не осталось. Казалось, он покинут всеми. Постепенно лейтенант осваивается, он ведет записи в дневнике…",
                                        "price": 120.55,
                                        "rating": 8.0,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTY3OTI5NDczN15BMl5BanBnXkFtZTcwNDA0NDY3Mw@@._V1._SX140_CR0,0,140,209_.jpg"
                                    },
                                    {
                                        "id": 9,
                                        "nameUkr": "Звёздные войны: Эпизод 4 – Новая надежда",
                                        "nameNative": "Star Wars",
                                        "yearOfRelease": 1977,
                                        "description": "Татуин. Планета-пустыня. Уже постаревший рыцарь Джедай Оби Ван Кеноби спасает молодого Люка Скайуокера, когда тот пытается отыскать пропавшего дроида. С этого момента Люк осознает свое истинное назначение: он один из рыцарей Джедай. В то время как гражданская война охватила галактику, а войска повстанцев ведут бои против сил злого Императора, к Люку и Оби Вану присоединяется отчаянный пилот-наемник Хан Соло, и в сопровождении двух дроидов, R2D2 и C-3PO, этот необычный отряд отправляется на поиски предводителя повстанцев — принцессы Леи. Героям предстоит отчаянная схватка с устрашающим Дартом Вейдером — правой рукой Императора и его секретным оружием — «Звездой Смерти».",
                                        "price": 198.98,
                                        "rating": 8.1,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BYTUwNTdiMzMtNThmNS00ODUzLThlMDMtMTM5Y2JkNWJjOGQ2XkEyXkFqcGdeQXVyNzQ1ODk3MTQ@._V1._SX140_CR0,0,140,209_.jpg"
                                    }
                                ]
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get three random movies")
    void testGetThreeRandomMovies() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get Movies By Genre Id Without Parameters")
    void testGetMoviesByGenreIdWithoutParameters() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(8))
                .andExpect(content()
                        .json("""
                                [
                                   {
                                       "id": 1,
                                       "nameUkr": "Побег из Шоушенка",
                                       "nameNative": "The Shawshank Redemption",
                                       "yearOfRelease": 1994,
                                       "description": "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                                       "price": 123.45,
                                       "rating": 8.9,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 2,
                                       "nameUkr": "Зеленая миля",
                                       "nameNative": "The Green Mile",
                                       "yearOfRelease": 1999,
                                       "description": "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                                       "price": 134.67,
                                       "rating": 8.9,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTUxMzQyNjA5MF5BMl5BanBnXkFtZTYwOTU2NTY3._V1._SY209_CR0,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 8,
                                       "nameUkr": "Бойцовский клуб",
                                       "nameNative": "Fight Club",
                                       "yearOfRelease": 1999,
                                       "description": "Терзаемый хронической бессонницей и отчаянно пытающийся вырваться из мучительно скучной жизни, клерк встречает некоего Тайлера Дардена, харизматического торговца мылом с извращенной философией. Тайлер уверен, что самосовершенствование — удел слабых, а саморазрушение — единственное, ради чего стоит жить.",
                                       "price": 119.99,
                                       "rating": 8.4,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BZGY5Y2RjMmItNDg5Yy00NjUwLThjMTEtNDc2OGUzNTBiYmM1XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR0,0,140,209_.jpg"
                                   }
                               ]
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get Movies By Genre Id And Rating Descending")
    void testGetMoviesByGenreIdAndRatingDesc() throws Exception {
        String sortingOrder = "desc";
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1, sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[2].id").value(16))
                .andExpect(content()
                        .json("""
                                [
                                    {
                                        "id": 2,
                                        "nameUkr": "Зеленая миля",
                                        "nameNative": "The Green Mile",
                                        "yearOfRelease": 1999,
                                        "description": "Обвиненный в страшном преступлении, Джон Коффи оказывается в блоке смертников тюрьмы «Холодная гора». Вновь прибывший обладал поразительным ростом и был пугающе спокоен, что, впрочем, никак не влияло на отношение к нему начальника блока Пола Эджкомба, привыкшего исполнять приговор.",
                                        "price": 134.67,
                                        "rating": 8.9,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTUxMzQyNjA5MF5BMl5BanBnXkFtZTYwOTU2NTY3._V1._SY209_CR0,0,140,209_.jpg"
                                    },
                                    {
                                        "id": 1,
                                        "nameUkr": "Побег из Шоушенка",
                                        "nameNative": "The Shawshank Redemption",
                                        "yearOfRelease": 1994,
                                        "description": "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                                        "price": 123.45,
                                        "rating": 8.9,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg"
                                    },
                                    {
                                        "id": 16,
                                        "nameUkr": "Большой куш",
                                        "nameNative": "Snatch.",
                                        "yearOfRelease": 2000,
                                        "description": "Четырехпалый Френки должен был переправить краденый алмаз из Англии в США своему боссу Эви. Но вместо этого герой попадает в эпицентр больших неприятностей. Сделав ставку на подпольном боксерском поединке, Френки попадает в круговорот весьма нежелательных событий. Вокруг героя и его груза разворачивается сложная интрига с участием множества колоритных персонажей лондонского дна — русского гангстера, троих незадачливых грабителей, хитрого боксера и угрюмого громилы грозного мафиози. Каждый норовит в одиночку сорвать Большой Куш.",
                                        "price": 160.0,
                                        "rating": 8.5,
                                        "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMTA2NDYxOGYtYjU1Mi00Y2QzLTgxMTQtMWI1MGI0ZGQ5MmU4XkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1._SY209_CR1,0,140,209_.jpg"
                                    }
                                ]
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get Movies By Genre Id And Rating Ascending")
    void testGetMoviesByGenreIdAndRatingAsc() throws Exception {
        String sortingOrder = "asc";
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1, sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(23))
                .andExpect(jsonPath("$[1].id").value(19))
                .andExpect(jsonPath("$[2].id").value(8))
                .andExpect(content()
                        .json("""
                                [
                                   {
                                       "id": 23,
                                       "nameUkr": "Блеф",
                                       "nameNative": "Bluff storia di truffe e di imbroglioni",
                                       "yearOfRelease": 1976,
                                       "description": "Белль Дюк имеет старые счеты с Филиппом Бэнгом, который отбывает свой срок за решёткой. Для того чтобы поквитаться с Филиппом, Белль Дюк вступает в сговор с другим аферистом по имени Феликс, чтобы тот организовал побег Филиппа Бенга из тюрьмы. Побег удаётся, но парочка заодно обманывает и Белль Дюк, исчезнув прямо из-под её носа. Выясняется, что и Филипп Бэнг, в свою очередь, не прочь отомстить Белль Дюк. Для этого он задумывает грандиозную мистификацию, сродни покерному блефу…",
                                       "price": 100.0,
                                       "rating": 7.6,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BMjk5YmMxMjMtMTlkNi00YTI5LThhYTMtOTk2NmNiNzQwMzI0XkEyXkFqcGdeQXVyMTQ3Njg3MQ@@._V1._SX140_CR0,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 19,
                                       "nameUkr": "Молчание ягнят",
                                       "nameNative": "The Silence of the Lambs",
                                       "yearOfRelease": 1990,
                                       "description": "Психопат похищает и убивает молодых женщин по всему Среднему Западу Америки. ФБР, уверенное в том, что все преступления совершены одним и тем же человеком, поручает агенту Клариссе Старлинг встретиться с заключенным-маньяком, который мог бы объяснить следствию психологические мотивы серийного убийцы и тем самым вывести на его след.",
                                       "price": 150.5,
                                       "rating": 8.3,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BNjNhZTk0ZmEtNjJhMi00YzFlLWE1MmEtYzM1M2ZmMGMwMTU4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR1,0,140,209_.jpg"
                                   },
                                   {
                                       "id": 8,
                                       "nameUkr": "Бойцовский клуб",
                                       "nameNative": "Fight Club",
                                       "yearOfRelease": 1999,
                                       "description": "Терзаемый хронической бессонницей и отчаянно пытающийся вырваться из мучительно скучной жизни, клерк встречает некоего Тайлера Дардена, харизматического торговца мылом с извращенной философией. Тайлер уверен, что самосовершенствование — удел слабых, а саморазрушение — единственное, ради чего стоит жить.",
                                       "price": 119.99,
                                       "rating": 8.4,
                                       "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BZGY5Y2RjMmItNDg5Yy00NjUwLThjMTEtNDc2OGUzNTBiYmM1XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR0,0,140,209_.jpg"
                                   }
                               ]
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get Movie By Id")
    void testGetMovieById() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(content()
                        .json("""
                                {
                                   "id": 1,
                                   "nameUkr": "Побег из Шоушенка",
                                   "nameNative": "The Shawshank Redemption",
                                   "yearOfRelease": 1994,
                                   "description": "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                                   "price": 123.45,
                                   "rating": 8.9,
                                   "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg"
                               }
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get Movie By Id If Movie Not Found")
    void testGetMovieByIdIfMovieNotFound() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get Movie By Id With Currency Specified")
    void testGetMovieByIdWithCurrencySpecified() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}?currency={currency}", 1L, "USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(content()
                        .json("""
                                {
                                   "id": 1,
                                   "nameUkr": "Побег из Шоушенка",
                                   "nameNative": "The Shawshank Redemption",
                                   "yearOfRelease": 1994,
                                   "description": "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                                   "price": 3.38,
                                   "rating": 8.9,
                                   "poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg"
                               }
                                """));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = {"flyway_schema_history"})
    @DisplayName("Test Get Movie By Id With Currency Specified If Currency Not Found")
    void testGetMovieByIdWithCurrencySpecifiedIfCurrencyNotFound() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}?currency={currency}", 1L, "GHTR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}