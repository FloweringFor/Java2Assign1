import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.String;
import java.util.stream.Collectors;


public class MovieAnalyzer {

    public static class Movie {
        private String Series_Title;
        private Integer Released_Year;
        private String Certificate;
        private Integer Runtime;
        private String Genre;
        private Float IMDB_Rating;
        private String Overview;
        private Integer Meta_score;
        private String Director;
        private String Star1;
        private String Star2;
        private String Star3;
        private String Star4;
        private Integer No_of_Votes;
        private Integer Gross;

        public Movie(String series_Title, Integer released_Year, String certificate,
                     Integer runtime, String genre, Float IMDB_Rating, String overview,
                     Integer meta_score, String director, String star1, String star2,
                     String star3, String star4, Integer no_of_Votes, Integer gross) {
            Series_Title = series_Title;
            Released_Year = released_Year;
            Certificate = certificate;
            Runtime = runtime;
            Genre = genre;
            this.IMDB_Rating = IMDB_Rating;
            Overview = overview;
            Meta_score = meta_score;
            Director = director;
            Star1 = star1;
            Star2 = star2;
            Star3 = star3;
            Star4 = star4;
            No_of_Votes = no_of_Votes;
            Gross = gross;
        }

        public int getOverviewLength() {
            int count = 0;
            char[] chars = Overview.toCharArray();
            for (char aChar : chars) {
                if (aChar == '"') {
                    count++;
                }
            }
            return (Overview.length() + count);
        }

        public String getSeries_Title() {
            return Series_Title;
        }

        public Integer getReleased_Year() {
            return Released_Year;
        }

        public String getCertificate() {
            return Certificate;
        }

        public Integer getRuntime() {
            return Runtime;
        }

        public String getGenre() {
            return Genre;
        }

        public Float getIMDB_Rating() {
            return IMDB_Rating;
        }

        public String getOverview() {
            return Overview;
        }

        public Integer getMeta_score() {
            return Meta_score;
        }

        public String getDirector() {
            return Director;
        }

        public String getStar1() {
            return Star1;
        }

        public String getStar2() {
            return Star2;
        }

        public String getStar3() {
            return Star3;
        }

        public String getStar4() {
            return Star4;
        }

        public Integer getNo_of_Votes() {
            return No_of_Votes;
        }

        public Integer getGross() {
            return Gross;
        }
    }


    public static class GenreItem {
        String key;
        Integer value;

        GenreItem(String key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "GenreItem{"
                    + "key='" + key + '\''
                    + ", value=" + value + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            GenreItem item = (GenreItem) o;
            return Objects.equals(key, item.key) && Objects.equals(value, item.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    public static class Message<K> {
        int count;
        K total;

        public Message(int count, K total) {
            this.count = count;
            this.total = total;
        }
    }

    public static class Star {
        String name;
        Double number;

        public String getName() {
            return name;
        }

        public Double getNumber() {
            return number;
        }

        public Star(String name, Double number) {
            this.name = name;
            this.number = number;
        }
    }

    ArrayList<Movie> data = new ArrayList<>();

    public MovieAnalyzer(String dataset_path) {
        read(dataset_path);
    }


    public void read(String dataset_path) {
        data = new ArrayList<>();
        try {
            CsvReader reader = new CsvReader(dataset_path, ',', StandardCharsets.UTF_8);
            reader.readHeaders();

            String Series_Title;
            Integer Released_Year;
            String Certificate;
            Integer Runtime;

            String Genre;
            Float IMDB_Rating;
            String Overview;
            Integer Meta_score;

            String Director;
            String Star1;
            String Star2;
            String Star3;

            String Star4;
            Integer No_of_Votes;
            Integer Gross;

            while (reader.readRecord()) {
                // csvList.add(reader.getValues());
                Series_Title = reader.get("Series_Title");
                Released_Year = Integer.valueOf(reader.get("Released_Year"));
                Certificate = reader.get("Certificate");
                if (reader.get("Runtime").equals("")) {
                    Runtime = -1;
                } else {
                    Runtime = Integer.valueOf(reader.get("Runtime").replace(" min", ""));
                }
                Genre = reader.get("Genre");
                if (reader.get("IMDB_Rating").equals("")) {
                    IMDB_Rating = -1.0f;
                } else {
                    IMDB_Rating = Float.valueOf(reader.get("IMDB_Rating"));
                }
                Overview = reader.get("Overview");
                if (reader.get("Meta_score").equals("")) {
                    Meta_score = -1;
                } else {
                    Meta_score = Integer.valueOf(reader.get("Meta_score"));
                }
                Director = reader.get("Director");
                Star1 = reader.get("Star1");
                Star2 = reader.get("Star2");
                Star3 = reader.get("Star3");
                Star4 = reader.get("Star4");
                if (reader.get("No_of_Votes").equals("")) {
                    No_of_Votes = -1;
                } else {
                    No_of_Votes = Integer.valueOf(reader.get("No_of_Votes"));
                }

                if (reader.get("Gross").equals("")) {
                    Gross = -1;
                } else {
                    Gross = Integer.valueOf(reader.get("Gross").replace(",",""));
                }
                Movie movie = new Movie(Series_Title, Released_Year,Certificate,
                        Runtime,Genre,IMDB_Rating, Overview,Meta_score,
                        Director,Star1,Star2,Star3,Star4,No_of_Votes,Gross);
                data.add(movie);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Integer> getMovieCountByYear() {
        Map<Integer, Long> result = data.stream().collect(Collectors.groupingBy(Movie::getReleased_Year, Collectors.counting()));
        Map<Integer, Long> answer = result.entrySet().stream().sorted((Map.Entry.<Integer, Long>comparingByKey().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map<Integer, Integer> final_result = new LinkedHashMap<>();
        answer.forEach((key,value)->{
            final_result.put(key, value.intValue());
        });

        return final_result;
    }


    public Map<String, Integer> getMovieCountByGenre() {
        Map<String, Integer> result = new HashMap<>();
        String[] genreList;
        for (int i = 0; i < data.size(); i++) {
            genreList = data.get(i).getGenre().split(", ");
            for (int j = 0; j < genreList.length; j++) {
                if (result.containsKey(genreList[j])) {
                    Integer pre = result.get(genreList[j]);
                    result.put(genreList[j], pre+1);
                } else {
                    result.put(genreList[j], 1);
                }
            }
        }
        List<GenreItem> items = new ArrayList<>();
        result.forEach((key,value)->{
            items.add(new GenreItem(key,value));
        });

        List<GenreItem> answer = items.stream().sorted(Comparator.comparing(GenreItem::getValue)
                .reversed().thenComparing(GenreItem::getKey)).collect(Collectors.toList());
        Map<String, Integer> final_result = new LinkedHashMap<>();
        for (int i = 0; i < answer.size(); i++) {
            final_result.put(answer.get(i).key,answer.get(i).value);
        }
        return final_result;
    }


    public Map<List<String>, Integer> getCoStarCount() {
        Map<List<String>, Integer> map = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            String[] names = new String[4];
            names[0] = data.get(i).getStar1();
            names[1] = data.get(i).getStar2();
            names[2] = data.get(i).getStar3();
            names[3] = data.get(i).getStar4();
            for (int j = 0; j < names.length-1; j++) {
                for (int k = j+1; k < names.length; k++) {
                    List<String> l = new ArrayList<>();
                    if (names[j].compareTo(names[k]) < 0) {
                        l.add(names[j]);
                        l.add(names[k]);
                    } else {
                        l.add(names[k]);
                        l.add(names[j]);
                    }
                    if (map.containsKey(l)) {
                        map.put(l,map.get(l) + 1);
                    } else {
                        map.put(l,1);
                    }
                }
            }
        }

        return map;
    }


    public List<String> getTopMovies(int top_k, String by) {
        List<Movie> movies;
        if (by.equals("runtime")) {
            movies = data.stream().sorted(Comparator.comparing(Movie::getRuntime).reversed()
                    .thenComparing(Movie::getSeries_Title)).limit(top_k).collect(Collectors.toList());
        } else {
            movies = data.stream().sorted(Comparator.comparing(Movie::getOverviewLength).reversed()
                    .thenComparing(Movie::getSeries_Title)).limit(top_k).collect(Collectors.toList());
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            result.add(movies.get(i).getSeries_Title());
        }
        return result;
    }



    public List<String> getTopStars(int top_k, String by) {
        List<String> answer = new ArrayList<>();
        if (by.equals("rating")) {
            HashMap<String, Message<Double>> stars = new HashMap<>();
            for (int i = 0; i < data.size(); i++){
                String[] names = new String[4];
                names[0] = data.get(i).getStar1();
                names[1] = data.get(i).getStar2();
                names[2] = data.get(i).getStar3();
                names[3] = data.get(i).getStar4();
                if (data.get(i).getIMDB_Rating() > 0 && data.get(i).getIMDB_Rating() < 10) {
                    for (int j = 0; j < names.length; j++) {
                        if (stars.containsKey(names[j])) {
                            stars.get(names[j]).count++;
                            stars.get(names[j]).total += data.get(i).getIMDB_Rating();
                        } else {
                            Message<Double> message = new Message<>(1, (double) data.get(i).getIMDB_Rating());
                            stars.put(names[j], message);
                        }
                    }
                }
            }
            List<Star> starData = new ArrayList<>();
            for (String name : stars.keySet()) {
                starData.add(new Star(name,stars.get(name).total / stars.get(name).count));
            }
            List<Star> result = starData.stream().sorted(Comparator.comparing(Star::getNumber).reversed().thenComparing(Star::getName)).limit(top_k).collect(Collectors.toList());
            for (int i = 0; i < result.size(); i++) {
                answer.add(result.get(i).name);
            }

        } else {
            // gross
            HashMap<String, Message<Long>> stars = new HashMap<>();
            for (int i = 0; i < data.size(); i++) {
                String[] names = new String[4];
                names[0] = data.get(i).getStar1();
                names[1] = data.get(i).getStar2();
                names[2] = data.get(i).getStar3();
                names[3] = data.get(i).getStar4();
                if (data.get(i).getGross() > 0) {
                    for (int j = 0; j < names.length; j++) {
                        if (stars.containsKey(names[j])) {
                            stars.get(names[j]).count++;
                            stars.get(names[j]).total += data.get(i).getGross();
                        } else {
                            Message<Long> message = new Message<>(1, (long)data.get(i).getGross());
                            stars.put(names[j], message);
                        }
                    }
                }
            }

            List<Star> starData = new ArrayList<>();
            for (String name : stars.keySet()) {
                starData.add(new Star(name, (double) (stars.get(name).total / stars.get(name).count)));
            }
            List<Star> result = starData.stream().sorted(Comparator.comparing(Star::getNumber)
                    .reversed().thenComparing(Star::getName)).limit(top_k).collect(Collectors.toList());
            for (int i = 0; i < result.size(); i++) {
                answer.add(result.get(i).name);
            }

        }
        return answer;
    }


    public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
        List<Movie> list = new ArrayList<>();
        for (Movie datum : data) {
            if (datum.getGenre().contains(genre) && datum.getIMDB_Rating() >= min_rating
                    && datum.getIMDB_Rating() <= 10 && datum.getRuntime() <= max_runtime
                    && datum.getRuntime() > 0) {
                list.add(datum);
            }
        }

        List<Movie> result = list.stream().sorted(Comparator.comparing(Movie::getSeries_Title)).collect(Collectors.toList());
        List<String> answer = new ArrayList<>();
        for (Movie movie : result) {
            answer.add(movie.getSeries_Title());
        }
        return answer;
    }

    public static void main(String[] args) {
        MovieAnalyzer movieAnalyzer = new MovieAnalyzer("/Users/chenwenyan/IdeaProjects/Java2_A1/src/main/resources/imdb_top_500.csv");
        List<String> list = movieAnalyzer.getTopStars(15, "rating");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}



