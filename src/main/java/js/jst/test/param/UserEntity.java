package js.jst.test.param;

import js.jst.interfaces.JEntity;
import js.jst.interfaces.JField;

import java.util.List;

@JEntity("user")
public class UserEntity extends SuperEntity {

    String name;

    List<UserEntity> childrenUserEntityList;

    public UserEntity(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getChildrenUserEntityList() {
        return childrenUserEntityList;
    }

    public void setChildrenUserEntityList(List<UserEntity> childrenUserEntityList) {
        this.childrenUserEntityList = childrenUserEntityList;
    }
}
