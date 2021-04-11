package js.jst.test.param;

import js.jst.interfaces.JEntity;
import js.jst.interfaces.JField;

@JEntity("user")
public class UserEntity {

    String id;

    String name;

    public UserEntity(){}

    public UserEntity(String id,String name){
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
