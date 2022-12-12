package com.example.demoinsert.model;







import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Audit1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    //just for old way
   // @OneToMany(mappedBy = "audit1",cascade = CascadeType.PERSIST)
    @OneToMany(mappedBy = "audit1")
    private List<Buffer1> buffers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Buffer1> getbuffers() {
        return buffers;
    }

    public void setBuffers(List<Buffer1> buffers) {
        this.buffers = buffers;
    }
}