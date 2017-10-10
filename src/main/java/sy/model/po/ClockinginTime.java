package sy.model.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * Created by heyh on 2017/10/9.
 */

@Entity
@Table(name = "T_ClockinginTime")
@DynamicInsert(true)
@DynamicUpdate(true)

public class ClockinginTime {
    @Id
    @Column(name = "ID", nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cid")
    private String cid;

    @Column(name = "clockinginStartTime")
    private Time clockinginStartTime;

    @Column(name = "clockinginEndTime")
    private Time clockinginEndTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Time getClockinginStartTime() {
        return clockinginStartTime;
    }

    public void setClockinginStartTime(Time clockinginStartTime) {
        this.clockinginStartTime = clockinginStartTime;
    }

    public Time getClockinginEndTime() {
        return clockinginEndTime;
    }

    public void setClockinginEndTime(Time clockinginEndTime) {
        this.clockinginEndTime = clockinginEndTime;
    }
}
