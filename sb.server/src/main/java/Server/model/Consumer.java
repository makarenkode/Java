package Server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Consumer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "donation")
    private float donation;
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDonation(float donation) {
        this.donation = donation;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getDonation() {
        return donation;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

}
