package cc.carce.sale.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pimages")
public class PimagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED COMMENT '图片ID'")
    private Long id;

    @Column(name = "filename", columnDefinition = "VARCHAR(255) COMMENT '文件名'")
    private String filename;

    @Column(name = "filepath", columnDefinition = "VARCHAR(500) COMMENT '文件路径'")
    private String filepath;

    @Column(name = "filesize", columnDefinition = "INT UNSIGNED COMMENT '文件大小'")
    private Long filesize;

    @Column(name = "mime_type", columnDefinition = "VARCHAR(100) COMMENT 'MIME类型'")
    private String mimeType;

    @Column(name = "c_dt", columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date cDt;

    @Column(name = "u_dt", columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date uDt;
}
