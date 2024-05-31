package sg.edu.np.mad.myapplication;

abstract class User {
    String name;
    String studentId;

    public User(String name, String studentId){
        this.name = name;
        this.studentId = studentId;

    }
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
