package com.dev.rohith.portfolio.service;

import com.dev.rohith.portfolio.dto.ExperienceEntry;
import com.dev.rohith.portfolio.dto.GithubUser;
import com.dev.rohith.portfolio.dto.ProjectEntry;
import com.dev.rohith.portfolio.dto.TechCategory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    public static final String GITHUB_USERNAME = "RohithKarnati";

    private static final String NAME = "Rohith Kumar";
    private static final String ROLE = "Senior Backend Engineer";
    private static final String FOCUS = "Performance & Scalability";
    private static final String LOCATION = "Bengaluru, India";
    private static final String EMAIL = "rohithkumar0902@gmail.com";
    private static final String LINKEDIN_URL = "https://www.linkedin.com/in/rohithkarnati";

    private final GithubService githubService;

    public ProfileService(GithubService githubService) {
        this.githubService = githubService;
    }

    public Map<String, Object> getProfile() {
        Map<String, Object> response = new HashMap<>();

        response.put("name", NAME);
        response.put("role", ROLE);
        response.put("focus", FOCUS);
        response.put("location", LOCATION);

        response.put("github", getGithubProfile());
        response.put("experience", getExperience());
        response.put("projects", getProjects());
        response.put("techStack", getTechStack());
        response.put("certifications", getCertifications());
        response.put("generatedAt", Instant.now());

        return response;
    }

    public String getName() {
        return NAME;
    }

    public String getRole() {
        return ROLE;
    }

    public String getFocus() {
        return FOCUS;
    }

    public String getLocation() {
        return LOCATION;
    }

    public String getEmail() {
        return EMAIL;
    }

    public String getLinkedinUrl() {
        return LINKEDIN_URL;
    }

    public GithubUser getGithubProfile() {
        return githubService.fetchGithubProfile(GITHUB_USERNAME);
    }

    /** Plain-text professional narrative, used by Developer Mode (about.txt / `about`). */
    public String getAboutText() {
        return """
                I'm Rohith Kumar, a Senior Backend Engineer focused on Java, Spring Boot,
                Microservices and DevOps automation.

                I started out learning to code through Scaler Academy with the goal of
                becoming a frontend engineer — I picked up Java early on partly because I
                mistook it for being closely related to JavaScript. While working through
                Java and data structures & algorithms, I found I genuinely enjoyed the
                problem solving and mathematical thinking involved, and that pulled me
                steadily toward the backend.

                Today my focus is backend systems: APIs, performance, scalability and the
                infrastructure that keeps them running — Linux, deployment pipelines, and
                the kind of production debugging that teaches you how systems actually
                behave under load.

                I hold a B.E. in Electronics & Communication Engineering and I'm currently
                a Senior Software Engineer working on Morgan Stanley's Datalink platform —
                a microservices data-abstraction layer serving 500+ downstream users with
                sub-150ms SLAs.""";
    }

    /** Employment / deployment history, sourced from resume. */
    public List<ExperienceEntry> getExperience() {
        return List.of(
                new ExperienceEntry(
                        "PRODUCTION",
                        "Senior Software Engineer",
                        "Morgan Stanley — FTC via Wissen Technology, Bengaluru",
                        "Jan 2024 — Present",
                        List.of(
                                "Designed a multi-layered microservices architecture serving 500+ downstream users across Tokyo, London and New York with a <150ms SLA.",
                                "Re-engineered a blocking REST endpoint into a streaming BulkService handling 10,000+ identifiers per request, delivering a 300%+ throughput improvement.",
                                "Extended OAuth2 authentication from client-credentials to Authorization Code Flow, enabling ~20k+ human users to securely query calendar and trading-hours data.",
                                "Managed 200+ production Linux hosts (160GB RAM / 16 vCPU each) supporting 1,500+ RPS, alongside end-to-end CI/CD via Docker and Jenkins across three global regions.",
                                "Shipped 20+ zero-rollback production releases supporting 50M+ daily data distributions with zero data-loss incidents.",
                                "Built OpenTelemetry and Loki observability with Autosys health monitoring to detect unhealthy instances before customer impact."
                        ),
                        false
                ),
                new ExperienceEntry(
                        "ARCHIVED",
                        "Programmer Analyst",
                        "Cognizant Technology Solutions — Client: ING Bank, Hyderabad",
                        "Jul 2021 — Aug 2023",
                        List.of(
                                "Built a Documentation workflow tab guiding users through Credit and Approval stages of loan applications.",
                                "Developed RESTful CRUD endpoints for employee and customer records with Spring MVC and stored procedures.",
                                "Applied SOLID principles and Java design patterns (Singleton, Factory, Builder) to improve maintainability.",
                                "Participated in Agile/Scrum ceremonies, including a stint as Scrum Master for story refinement and planning."
                        ),
                        false
                ),
                new ExperienceEntry(
                        "ARCHIVED",
                        "Software Testing Intern",
                        "Cognizant Technology Solutions",
                        "Mar 2021 — May 2021",
                        List.of(
                                "Completed a software testing training program covering testing methodologies, processes and tooling."
                        ),
                        false
                )
        );
    }

    /** Portfolio projects, sourced from resume + this repository itself. */
    public List<ProjectEntry> getProjects() {
        return List.of(
                new ProjectEntry(
                        "Backend Portfolio Platform",
                        "The backend behind this site — a Spring Boot service that renders a "
                                + "server-side dashboard and exposes a small REST API.",
                        "Wanted a personal site that reflects backend engineering work instead "
                                + "of a templated frontend showcase.",
                        "Server-rendered with Thymeleaf, GitHub profile data fetched via a "
                                + "reactive WebClient and cached with Caffeine to avoid hitting "
                                + "GitHub's rate limits on every page load. Clean controller / "
                                + "service / DTO separation.",
                        List.of("Java 21", "Spring Boot", "WebFlux", "Caffeine", "Thymeleaf", "Gradle"),
                        "https://github.com/RohithKarnati/backend-portfolio",
                        false
                ),
                new ProjectEntry(
                        "Book My Show Clone",
                        "A ticket-booking backend built around clean Low-Level Design and OOP principles.",
                        "Wanted to practice designing a scalable booking system with proper LLD instead of a monolithic CRUD app.",
                        "Applied Factory, Singleton and MVC design patterns; used Spring for IoC/DI, Spring JPA and JDBC "
                                + "against MySQL and SQL Server, and validated the API surface with Postman.",
                        List.of("Java", "Spring Boot", "Low-Level Design", "MySQL", "SQL Server"),
                        "https://github.com/RohithKarnati/BookMyShow_Clone",
                        false
                ),
                new ProjectEntry(
                        "Parking Lot System",
                        "A backend service that manages parking allocation and vacant-spot search for a multi-level lot.",
                        "Needed an OOP-modeled system to track vehicles, spot availability and payments without ad-hoc logic.",
                        "Modeled the domain in Java with data structures for fast spot lookup, backed by a MySQL "
                                + "schema for vehicle records and transactions.",
                        List.of("Java", "Spring Boot", "MySQL", "OOP"),
                        "https://github.com/RohithKarnati/ParkingLot",
                        false
                ),
                new ProjectEntry(
                        "Snake and Ladder",
                        "A command-line implementation of Snake and Ladder with full game-state management.",
                        "Wanted a small, self-contained project to practice clean OOP game logic end-to-end.",
                        "Built the board, dice and player-turn logic in Java, exposed through a CLI, "
                                + "with tests to catch rule-adherence bugs.",
                        List.of("Java", "Spring Boot", "OOP"),
                        "https://github.com/RohithKarnati/Snake-And-Ladder",
                        false
                )
        );
    }

    /** Tech stack, grouped by category, sourced from resume. */
    public List<TechCategory> getTechStack() {
        return List.of(
                new TechCategory("Backend", List.of("Java", "Spring Boot", "Spring MVC", "Microservices", "JPA / Hibernate")),
                new TechCategory("APIs & Security", List.of("REST APIs", "OAuth2 / OIDC", "JWT", "mTLS")),
                new TechCategory("Database", List.of("SingleStore", "MSSQL", "MongoDB", "Sybase", "MySQL")),
                new TechCategory("DevOps", List.of("Docker", "Jenkins", "CI/CD", "Linux")),
                new TechCategory("Observability", List.of("OpenTelemetry", "Loki", "Autosys")),
                new TechCategory("Engineering", List.of("DSA", "Low-Level Design", "Design Patterns", "Multithreading", "Distributed Systems"))
        );
    }

    /** Certifications, sourced from resume. */
    public List<Map<String, String>> getCertifications() {
        return List.of(
                Map.of("name", "Data Structures and Algorithms — Scaler", "url", "https://moonshot.scaler.com/s/sl/fDVWC5kDGe"),
                Map.of("name", "Java — Scaler Academy", "url", "https://moonshot.scaler.com/s/sl/imcQfqYo1F"),
                Map.of("name", "Databases and SQL — Scaler", "url", "https://moonshot.scaler.com/s/sl/zhjJkPHZCy"),
                Map.of("name", "Introduction to Back-End Development — META", "url", "https://www.coursera.org/account/accomplishments/certificate/64U7TVP9DTU7")
        );
    }

}
