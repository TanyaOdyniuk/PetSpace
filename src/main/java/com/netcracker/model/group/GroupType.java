package com.netcracker.model.group;

        import com.netcracker.dao.annotation.Attribute;
        import com.netcracker.dao.annotation.ObjectType;
        import com.netcracker.dao.annotation.Reference;

        import java.util.List;

@ObjectType(value = 405)
public enum GroupType {
    OPEN("Open Group"),
    CLOSED("Closed Group");

    @Attribute(value = 429)
    private String type;
    @Reference(value = 426)
    private List<Group> groups;

    GroupType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
