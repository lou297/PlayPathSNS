package com.web.curation.model.course;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.Data;

@Data
public class CourseId implements Serializable{
    @Id
    private int feedNo;
    @Id
    private int courseOrder;
    @Id
    private String tradeName;
    @Id
    private String latitude;
    @Id
    private String longitude;
    @Id
    private String thumbnailUrl;
}
