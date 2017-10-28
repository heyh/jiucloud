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

    @Column(name = "clockinginStartTime2")
    private Time clockinginStartTime2;

    @Column(name = "clockinginEndTime")
    private Time clockinginEndTime;

    @Column(name = "clockinginEndTime2")
    private Time clockinginEndTime2;

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

    public Time getClockinginStartTime2() {
        return clockinginStartTime2;
    }

    public void setClockinginStartTime2(Time clockinginStartTime2) {
        this.clockinginStartTime2 = clockinginStartTime2;
    }

    public Time getClockinginEndTime() {
        return clockinginEndTime;
    }

    public void setClockinginEndTime(Time clockinginEndTime) {
        this.clockinginEndTime = clockinginEndTime;
    }

    public Time getClockinginEndTime2() {
        return clockinginEndTime2;
    }

    public void setClockinginEndTime2(Time clockinginEndTime2) {
        this.clockinginEndTime2 = clockinginEndTime2;
    }
}
