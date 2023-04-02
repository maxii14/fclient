package ru.iu3.backend.models;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "paintings")
@Access(AccessType.FIELD)
public class Painting {

    public Painting() { }
    public Painting(Long id) {
        this.id = id;
    }

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "artistid", nullable = false)
    public int artistid;

    @Column(name = "museumid", nullable = false)
    public int museumid;

    @Column(name = "year", nullable = false)
    public int year;

}
