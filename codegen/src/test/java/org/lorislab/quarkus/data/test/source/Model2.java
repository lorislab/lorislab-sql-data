package org.lorislab.quarkus.data.test.source;

import org.lorislab.quarkus.data.Entity;
import org.lorislab.quarkus.data.Id;
import org.lorislab.quarkus.data.Table;

@Entity
@Table(name = "MODEL2")
public class Model2 {

    @Id
    public String id;

    public Integer version;
}