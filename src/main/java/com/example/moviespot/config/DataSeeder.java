package com.example.moviespot.config;

import com.example.moviespot.entity.*;
import com.example.moviespot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GenreRepository genreRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role userRole  = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@moviespot.com", passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Set.of(adminRole, userRole)));
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("user")) {
            User user = new User("user", "user@moviespot.com", passwordEncoder.encode("user"));
            user.setRoles(new HashSet<>(Set.of(userRole)));
            userRepository.save(user);
        }

        User nina   = createUser("nina",   "nina@moviespot.com",   "nina123",   userRole);
        User aleksa = createUser("aleksa", "aleksa@moviespot.com", "aleksa123", userRole);
        User filip  = createUser("filip",  "filip@moviespot.com",  "filip123",  userRole);
        User maja   = createUser("maja",   "maja@moviespot.com",   "maja123",   userRole);

        Genre action   = getOrCreate("Action",   "Akcioni filmovi");
        Genre drama    = getOrCreate("Drama",     "Dramski filmovi");
        Genre scifi    = getOrCreate("Sci-Fi",    "Naučna fantastika");
        Genre comedy   = getOrCreate("Comedy",    "Komedije");
        Genre horror   = getOrCreate("Horror",    "Horor filmovi");
        Genre thriller = getOrCreate("Thriller",  "Triler filmovi");

        if (movieRepository.count() == 0) {
            Movie birdBox     = saveMovie("Bird Box", "Majka i njena deca pokušavaju da stignu do utočišta u svetu napunjenom nevidljivim entitetima.", "Susanne Bier", 2018, 124, "images/posters/bird-box.jpg", Set.of(thriller, horror));
            Movie bladeRunner = saveMovie("Blade Runner 2049", "Mladi blade runner otkriva tajnu koja bi mogla izazvati haos u društvu.", "Denis Villeneuve", 2017, 164, "images/posters/blade-runner.jpg", Set.of(scifi, drama));
            Movie darkKnight  = saveMovie("The Dark Knight", "Batman se suočava sa Jokerom, kriminalnim umom koji seje haos u Gothamu.", "Christopher Nolan", 2008, 152, "images/posters/dark-knight.jpg", Set.of(action, thriller));
            Movie fightClub   = saveMovie("Fight Club", "Usamljeni bančar formira tajni klub borbe sa harizmatičnim sapunarskim prodavcem.", "David Fincher", 1999, 139, "images/posters/fight-club.jpg", Set.of(thriller, drama));
            Movie getOut      = saveMovie("Get Out", "Mladi Afroamerikanac otkriva uznemirujuće tajne tokom posete porodici svoje devojke.", "Jordan Peele", 2017, 104, "images/posters/get-out.jpg", Set.of(horror, thriller));
            Movie gladiator   = saveMovie("Gladiator", "Rimski general postaje rob i gladijator u potrazi za osvetom.", "Ridley Scott", 2000, 155, "images/posters/gladiator.jpg", Set.of(drama, action));
            Movie johnWick    = saveMovie("John Wick", "Bivši profesionalni ubica kreće u osvetu za ubojstvo svog psa.", "Chad Stahelski", 2014, 101, "images/posters/john-wick.jpg", Set.of(action, thriller));
            Movie madMax      = saveMovie("Mad Max: Fury Road", "U postapokaliptičnoj pustinji, Max i Furiosa beže od tiranina Immortana Joea.", "George Miller", 2015, 120, "images/posters/mad-max.jpg", Set.of(action));
            Movie matrix      = saveMovie("The Matrix", "Haker otkriva pravu prirodu stvarnosti i svoju ulogu u ratu protiv mašina.", "The Wachowskis", 1999, 136, "images/posters/matrix.jpg", Set.of(scifi, action));
            Movie parasite    = saveMovie("Parasite", "Siromašna porodica se infiltrira u bogato domaćinstvo sa dalekosežnim posledicama.", "Bong Joon-ho", 2019, 132, "images/posters/parasite.jpg", Set.of(thriller, drama));
            Movie quietPlace  = saveMovie("A Quiet Place", "Porodica živi u tišini da bi preživela od zvuka zavisnih čudovišta.", "John Krasinski", 2018, 90, "images/posters/quiet-place.jpg", Set.of(horror, scifi));
            Movie se7en       = saveMovie("Se7en", "Dva detektiva love serijskog ubicu koji koristi sedam smrtnih grehova kao motiv.", "David Fincher", 1995, 127, "images/posters/se7en.jpg", Set.of(thriller, horror));

            // Nina — Gladiator i Bird Box
            saveReview(gladiator, nina, "Epski film!", "Russell Crowe je fenomenalan. ", 10);
            saveReview(birdBox,   nina, "Napeto do kraja", "Sandra Bullock nas sve vreme drži na ivici. Atmosfera je savršena.", 8);

            // Aleksa — John Wick i The Matrix
            saveReview(johnWick, aleksa, "Akcija kakve nema", "Keanu Reeves nikad nije bio bolji. Borbe su neverovatne.", 10);
            saveReview(matrix,   aleksa, "Klasik koji ne stari", "Vizuelni efekti i priča su i danas impresivni. Obavezno gledanje.", 10);

            // Filip — Parasite i Se7en
            saveReview(parasite, filip, "Šokantno i briljantno", "Nikad nisam video ovakav preokret.", 10);
            saveReview(se7en,    filip, "Mračno i savršeno", "Fincher zna kako da napravi atmosferu. Kevin Spacey je zastrašujući.", 9);

            // Maja — Get Out i Blade Runner
            saveReview(getOut,      maja, "Horor koji misli", "Nije samo horor. Bravo Jordan Peele!", 9);
            saveReview(bladeRunner, maja, "Vizuelna poezija", "Svaki kadar je kao slika. Villeneuve je remek-delo napravio.", 9);
        }
    }

    private User createUser(String username, String email, String password, Role role) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            User u = new User(username, email, passwordEncoder.encode(password));
            u.setRoles(new HashSet<>(Set.of(role)));
            return userRepository.save(u);
        });
    }

    private Movie saveMovie(String title, String desc, String director, int year, int duration, String poster, Set<Genre> genres) {
        Movie m = new Movie();
        m.setTitle(title); m.setDescription(desc); m.setDirector(director);
        m.setReleaseYear(year); m.setDuration(duration); m.setPosterUrl(poster);
        m.setGenres(genres);
        return movieRepository.save(m);
    }

    private void saveReview(Movie movie, User user, String title, String content, int rating) {
        Review r = new Review();
        r.setTitle(title); r.setContent(content); r.setRating(rating);
        r.setMovie(movie); r.setUser(user);
        reviewRepository.save(r);
    }

    private Genre getOrCreate(String name, String desc) {
        return genreRepository.findByName(name).orElseGet(() -> genreRepository.save(new Genre(name, desc)));
    }
}
