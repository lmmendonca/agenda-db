package model;

public class Group {

  private Integer groupId;
  private String description;

  public Group(Integer groupId, String description) {
    this.groupId = groupId;
    this.description = description;
  }

  public Group(String description) {
    this.description = description;
  }

    public Group() {

    }

    public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Group{" + "groupId=" + groupId + ", description='" + description + '\'' + '}';
  }
}
