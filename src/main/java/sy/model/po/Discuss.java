package sy.model.po;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by heyh on 2017/4/6.
 */

@Entity
@Table(name = "TDiscuss")
public class Discuss {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "discussType")
    private String discussType;

    @Column(name = "discussId")
    private String discussId;

    @Column(name = "isDel")
    private int isDel;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "createUser")
    private String createUser;

    @Column(name = "createTime")
    private Date createTime;

    private String isMyself;

    public String getIsMyself() {
        return isMyself;
    }

    public void setIsMyself(String isMyself) {
        this.isMyself = isMyself;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscussType() {
        return discussType;
    }

    public void setDiscussType(String discussType) {
        this.discussType = discussType;
    }

    public String getDiscussId() {
        return discussId;
    }

    public void setDiscussId(String discussId) {
        this.discussId = discussId;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
