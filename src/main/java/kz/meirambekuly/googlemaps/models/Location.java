package kz.meirambekuly.googlemaps.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.boot.model.relational.internal.SqlStringGenerationContextImpl;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "location")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private double lat;

    @Column(name = "longitude", nullable = false)
    private double lng;

    @Column(name = "altitude", nullable = false)
    private double alt;

    private String identifier;

    @Column(name = "elapsed_timestamp", nullable = false)
    private long timestamp;

    @Column(name = "floor_label")
    private Integer floor;

    @Column(name = "horizontal_accuracy", nullable = false)
    private double horizontal;

    @Column(name = "vertical_accuracy", nullable = false)
    private double vertical;

    @Column(name = "confidence_accuracy", nullable = false)
    private double confidence;

    @Column(name = "page_number", nullable = false)
    private int pageNumber;

}